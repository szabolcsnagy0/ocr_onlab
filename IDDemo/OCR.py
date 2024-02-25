from paddleocr import (PaddleOCR, draw_ocr)
import json
import io
import math


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


def normalize_coordinates(data):
    # Finding min of x and y
    offset_x = float("inf")
    offset_y = float("inf")
    for lines in data:
        for coords in lines[0]:
            if coords[0] < offset_x:
                offset_x = coords[0]
            if coords[1] < offset_y:
                offset_y = coords[1]
    # Normalize coordinates
    for lines in data:
        for coords in lines[0]:
            coords[0] = coords[0] - offset_x
            coords[1] = coords[1] - offset_y


def find_matching_box(box_coordinates):
    difference = float("inf")
    closest_box = None
    for item in localization["front"]:
        current_distance = math.dist(item["place"][0], box_coordinates[0])
        if current_distance < difference:
            closest_box = item
            difference = current_distance
    return closest_box


paddle_ocr = PaddleOCR(use_angle_cls=True, lang='hu')

# Front
img_front = '../images/id2_front.jpeg'
result_front = paddle_ocr.ocr(img_front, cls=True)
# print(result_front[0])
# show_result(result_front, img_front)
normalize_coordinates(result_front[0])
print(result_front[0])

file = io.open("../text_localization.json", encoding="utf-8")
localization = json.load(file)
print(localization)

for boxes in result_front[0]:
    print(boxes)
    print(find_matching_box(boxes[0]))
    print("\n\n")

# data_front = process_front(result_front[0])
