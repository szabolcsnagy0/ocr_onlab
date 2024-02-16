from paddleocr import (PaddleOCR,
                       draw_ocr)


class Person:
    def __init__(self, name='', date_of_birth='', id=0):
        self.name = name
        self.date_of_birth = date_of_birth
        self.id = id

    def print(self):
        print("Name:\n\t", self.name, "\nDate of birth:\n\t", self.date_of_birth, "\nId number:\n\t", self.id)


def show_result(result, img_path):
    # draw result
    from PIL import Image
    current_result = result[0]
    image = Image.open(img_path).convert('RGB')
    boxes = [line[0] for line in current_result]
    texts = [line[1][0] for line in current_result]
    scores = [line[1][1] for line in current_result]
    font = '../arial.ttf'
    im_show = draw_ocr(image, boxes, texts, scores, font_path=font)
    im_show = Image.fromarray(im_show)
    im_show.show()
    # im_show.save('result.jpg')


def check_sum(string):
    weights = [7, 3, 1]
    weight_index = 0
    calculated_sum = 0
    for character in string:
        if character == '<':
            value = 0
        else:
            value = ord(character) - ord('0')
            if value > 10:
                value = value - 7
        calculated_sum = (value * weights[weight_index] + calculated_sum) % 10
        weight_index = (weight_index + 1) % 3
    return calculated_sum


paddle_ocr = PaddleOCR(use_angle_cls=True, lang='hu')

img_path = '../images/mrp_id_crop.png'
result = paddle_ocr.ocr(img_path, cls=True)
show_result(result, img_path)

person = Person()

# Process data
size = len(result[0])
mrz_lines = result[0][size-3:size]

# Checksum for the first two rows
import re
lines_concat = mrz_lines[0][1][0] + re.sub('[^0-9]', '', mrz_lines[1][1][0])
text = lines_concat[5:-1]
check = ord(lines_concat[-1])-ord('0')
if check_sum(text) != check:
    print("ERROR: MRZ first two rows! ", check_sum(text), check, "\n", text)

# Obtain ID
text = mrz_lines[0][1][0]
id = text[5:13]
check = ord(text[14]) - ord('0')

if check_sum(id) == check:
    person.id = id
else:
    print("INCORRECT ID: ", id, check_sum(id), check)

# Obtain date of birth
text = mrz_lines[1][1][0]
date_of_birth = text[0:6]
check = ord(text[6]) - ord('0')
if check_sum(date_of_birth) == check:
    person.date_of_birth = date_of_birth
else:
    print("INCORRECT DATE: ", date_of_birth, check_sum(date_of_birth), check)

# Obtain name
text = mrz_lines[2][1][0]
person.name = text.replace("<<", " ", 1).replace("<", "")

person.print()
