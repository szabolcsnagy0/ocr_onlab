from paddleocr import (PaddleOCR,
                       draw_ocr)
import re


class Person:
    def __init__(self):
        self.date_of_expiry = None
        self.date_of_expiry_confidence = 0
        self.nationality = None
        self.nationality_confidence = 0
        self.sex = None
        self.sex_confidence = 0
        self.id_confidence = 0
        self.date_of_birth_confidence = 0
        self.name_confidence = 0
        self.name = None
        self.date_of_birth = None
        self.id = None

    def merge_data(self, person1, person2):
        if person1.date_of_expiry_confidence >= person2.date_of_expiry_confidence:
            self.date_of_expiry = person1.date_of_expiry
            self.date_of_expiry_confidence = person1.date_of_expiry_confidence
        else:
            self.date_of_expiry = person2.date_of_expiry
            self.date_of_expiry_confidence = person2.date_of_expiry_confidence
        if person1.nationality_confidence >= person2.nationality_confidence:
            self.nationality = person1.nationality
            self.nationality_confidence = person1.nationality_confidence
        else:
            self.nationality = person2.nationality
            self.nationality_confidence = person2.nationality_confidence
        if person1.sex_confidence >= person2.sex_confidence:
            self.sex = person1.sex
            self.sex_confidence = person1.sex_confidence
        else:
            self.sex = person2.sex
            self.sex_confidence = person2.sex_confidence
        if person1.id_confidence < person2.id_confidence:
            self.id = person2.id
            self.id_confidence = person2.id_confidence
        else:
            self.id = person1.id
            self.id_confidence = person1.id_confidence
        if person1.date_of_birth_confidence < person2.date_of_birth_confidence:
            self.date_of_birth = person2.date_of_birth
            self.date_of_birth_confidence = person2.date_of_birth_confidence
        else:
            self.date_of_birth = person1.date_of_birth
            self.date_of_birth_confidence = person1.date_of_birth_confidence
        if person1.name_confidence < person2.name_confidence:
            self.name = person2.name
            self.name_confidence = person2.name_confidence
        else:
            self.name = person1.name
            self.name_confidence = person1.name_confidence
        return self

    def print(self):
        print("Name:\n\t", self.name, " ", self.name_confidence,
              "\nDate of birth:\n\t", self.date_of_birth, " ", self.date_of_birth_confidence,
              "\nDocument number:\n\t", self.id, " ", self.id_confidence,
              "\nDate of expiry:\n\t", self.date_of_expiry, " ", self.date_of_expiry_confidence,
              "\nSex:\n\t", self.sex, " ", self.sex_confidence,
              "\nNationality:\n\t", self.nationality, " ", self.nationality_confidence, "\n\n")


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


def check_sum(string, check):
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
    return calculated_sum == (ord(check) - ord('0'))


def process_front(ocr_result):
    person = Person()

    # Extract data
    # Name
    person.name = ocr_result[3][1][0]
    person.name_confidence = ocr_result[3][1][1]

    # Sex
    text = ocr_result[5][1][0]
    person.sex_confidence = ocr_result[5][1][1]
    male = ("M" in text or "FÉRFI" in text or "FERFI" in text) and ("NO" not in text and "NŐ" not in text)
    female = ("F" in text or "NŐ" in text or "NO" in text) and (
            "M" not in text and "FÉRFI" not in text and "FERFI" not in text)
    if male and female:
        person.sex_confidence = 0
    else:
        if male:
            person.sex = "M"
        else:
            person.sex = "F"

    # Nationality
    text = ocr_result[6][1][0]
    person.nationality = text[-3:]
    person.nationality_confidence = ocr_result[6][1][1]

    # Date of birth
    text = ocr_result[8][1][0]
    person.date_of_birth = text[-2:] + text[2:4] + text[:2]
    person.date_of_birth_confidence = ocr_result[8][1][1]

    # Document number
    id1 = ocr_result[4][1][0]
    id1_conf = ocr_result[4][1][1]
    id2 = ocr_result[12][1][0]
    id2_conf = ocr_result[12][1][1]
    if id1_conf > id2_conf:
        person.id = id1
        person.id_confidence = id1_conf
    else:
        person.id = id2
        person.id_confidence = id2_conf

    # Expiry
    text = ocr_result[10][1][0]
    person.date_of_expiry = text[-2:] + text[2:4] + text[:2]
    person.date_of_expiry_confidence = ocr_result[10][1][1]

    return person


def process_back(ocr_result):
    person = Person()

    # Process data
    mrz_lines = ocr_result[-3:]

    # Sex
    text = mrz_lines[1][1][0]
    person.sex = text[7]
    person.sex_confidence = mrz_lines[1][1][1]

    # Name
    text = mrz_lines[2][1][0]
    person.name = text.replace("<<", " ", 1).replace("<", "")
    person.name_confidence = mrz_lines[2][1][1]

    # Nationality
    text = mrz_lines[1][1][0]
    person.nationality = text[15:18]
    person.nationality_confidence = mrz_lines[1][1][1]

    # Checksum for the first two rows
    lines_concat = mrz_lines[0][1][0] + re.sub('[^0-9]', '', mrz_lines[1][1][0])
    text = lines_concat[5:-1]
    if not check_sum(text, lines_concat[-1]):
        print("ERROR: MRZ check digit not matching for the first two rows!\n", text)
        return person

    # Document number
    text = mrz_lines[0][1][0]
    id = text[5:13]
    if check_sum(id, text[14]):
        person.id = id
        person.id_confidence = 1
    else:
        print("ERROR: MRZ check digit not matching for document number!\n", id)

    # Date of birth
    text = mrz_lines[1][1][0]
    date_of_birth = text[0:6]
    if check_sum(date_of_birth, text[6]):
        person.date_of_birth = date_of_birth
        person.date_of_birth_confidence = 1
    else:
        print("ERROR: MRZ check digit not matching for date of birth!\n", date_of_birth)

    # Date of expiry
    text = mrz_lines[1][1][0]
    date_of_expiry = text[8:14]
    if check_sum(date_of_expiry, text[14]):
        person.date_of_expiry = date_of_expiry
        person.date_of_expiry_confidence = 1
    else:
        print("ERROR: MRZ check digit not matching for date of expiry!\n", date_of_expiry)
    return person


paddle_ocr = PaddleOCR(use_angle_cls=True, lang='hu')

# Front
img_front = '../images/id_front.jpeg'
result_front = paddle_ocr.ocr(img_front, cls=True)
data_front = process_front(result_front[0])

# Back
img_back = '../images/id_back.png'
result_back = paddle_ocr.ocr(img_back, cls=True)
data_back = process_back(result_back[0])

print("\n\n----------FRONT----------")
data_front.print()
print("\n\n----------BACK----------")
data_back.print()
print("\n\n----------RESULT----------")
Person().merge_data(data_front, data_back).print()

# Show images
show_result(result_front, img_front)
show_result(result_back, img_back)
