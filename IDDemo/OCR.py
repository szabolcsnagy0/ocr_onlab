from paddleocr import (PaddleOCR, draw_ocr)
import json
import io
import math
from difflib import SequenceMatcher
import cv2


def similar(a, b):
    return SequenceMatcher(None, a, b).ratio()


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


def normalize_coordinates(ocr_data, img_shape):
    for line in ocr_data:
        for coords in line[0]:
            coords[0] = coords[0] / img_shape[1]
            coords[1] = coords[1] / img_shape[0]


def matching_box_by_text(string):
    max_percentage = 0
    best_match = None
    for item in localization["front"]:
        current_percentage = similar(item["text"], string)
        # print(item["text"], string, current_percentage)
        if current_percentage > max_percentage:
            max_percentage = current_percentage
            best_match = item

    if max_percentage > 0.5:
        return best_match
    return None


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


def find_box_of_value(reference, current_box, ocr_data):
    if len(reference["value"]) != 4:
        return None

    reference_offset = [0, 0]
    value_offset = [0, 0]
    for i in range(2):
        # Calculating the offset between the value and the text in the localization file
        value_offset[i] = reference["value"][0][i] - reference["place"][0][i]
        # Calculation the offset between the localization and ocr data
        reference_offset[i] = reference["place"][0][i] - current_box[0][0][i]

    # Calculating the theoretical coordinates of the value box
    value_coordinates = []
    for coords in current_box[0]:
        x = coords[0] + reference_offset[0] + value_offset[0]
        y = coords[1] + reference_offset[1] + value_offset[1]
        value_coordinates.append([x, y])

    # Finding the closest match
    closest_distance = float("inf")
    closest_box = None
    for item in ocr_data:
        current_distance = math.dist(item[0][0], value_coordinates[0])
        if current_distance < closest_distance:
            closest_distance = current_distance
            closest_box = item

    return closest_box


file = io.open("../text_localization_ratio.json", encoding="utf-8")
localization = json.load(file)

paddle_ocr = PaddleOCR(use_angle_cls=True, lang='hu')

# Front
img_front = "../images/id_front.jpeg"
result_front = paddle_ocr.ocr(img_front, cls=True)

print(result_front)

img = cv2.imread(img_front)
normalize_coordinates(result_front[0], img.shape)

for boxes in result_front[0]:
    match = matching_box_by_text(boxes[1][0])
    if match is None:
        continue
    print(match["text"])
    value = find_box_of_value(match, boxes, result_front[0])
    if value is not None:
        print(value[1][0])
    print("\n")


# show_result(result_front, img_front)
# data_front = process_front(result_front[0])
