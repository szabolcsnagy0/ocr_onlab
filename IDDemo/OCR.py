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


def normalize_coordinates(data, base_size):
    # Finding min of x and y
    offset_x = float("inf")
    offset_y = float("inf")
    for lines in data:
        for coords in lines[0]:
            if coords[0] < offset_x:
                offset_x = coords[0]
            if coords[1] < offset_y:
                offset_y = coords[1]
    # Finding the height and width difference in percentage
    current_height = abs(data[0][0][0][1] - data[0][0][2][1])
    current_width = abs(data[0][0][0][0] - data[0][0][1][0])
    current_size = current_width * current_height

    size_difference = 0
    if current_size != 0:
        size_difference = base_size / current_size

    print(base_size, current_size, size_difference)

    # Normalize coordinates and resize boxes
    for lines in data:
        for coords in lines[0]:
            coords[0] = (coords[0] - offset_x) * size_difference
            coords[1] = (coords[1] - offset_y) * size_difference


def find_matching_box(box_coordinates):
    difference = float("inf")
    closest_box = None
    for item in localization["front"]:
        current_distance = math.dist(item["place"][0], box_coordinates[0])
        if len(item["value"]) > 0:
            current_distance = min(current_distance, math.dist(item["value"][0], box_coordinates[0]))
        if current_distance < difference:
            closest_box = item
            difference = current_distance
        print(item["text"], " ", current_distance)
    return closest_box


file = io.open("../text_localization.json", encoding="utf-8")
localization = json.load(file)
print(localization)
base_height = abs(localization["front"][0]["place"][0][1] - localization["front"][0]["place"][2][1])
base_width = abs(localization["front"][0]["place"][0][0] - localization["front"][0]["place"][1][0])
base_size = base_height * base_width

paddle_ocr = PaddleOCR(use_angle_cls=True, lang='hu')

# Front
img_front = '../images/id2_front.jpeg'
result_front = paddle_ocr.ocr(img_front, cls=True)
# print(result_front[0])
show_result(result_front, img_front)
normalize_coordinates(result_front[0], base_size)
# print(result_front[0])

for boxes in result_front[0]:
    print("\n\n")
    print(boxes)
    print(find_matching_box(boxes[0]))
    print("\n\n")

# data_front = process_front(result_front[0])
