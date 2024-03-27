import sys

from paddleocr import (PaddleOCR, draw_ocr)
import json
import io
import math
from difflib import SequenceMatcher
import cv2
import DataCleansing as dc


def similar(a, b):
    return SequenceMatcher(None, a, b).ratio()


def normalize_coordinates(ocr_data, img_shape):
    for line in ocr_data:
        for coords in line[0]:
            coords[0] = coords[0] / img_shape[1]
            coords[1] = coords[1] / img_shape[0]


def matching_box_by_text(string, localization_data):
    max_percentage = 0
    best_match = None
    for item in localization_data:
        current_percentage = similar(item["text"], string)
        # print(item["text"], string, current_percentage)
        if current_percentage > max_percentage:
            max_percentage = current_percentage
            best_match = item

    if max_percentage > 0.5:
        return best_match
    return None


def matching_box_by_place(ocr_data, localization_data):
    closest_distance = float("inf")
    best_match = None
    for item in localization_data:
        current_distance = math.dist(ocr_data[0], item["place"][0])
        if current_distance < closest_distance:
            closest_distance = current_distance
            best_match = item
    return best_match


def find_matching_box(ocr_data, localization_data):
    best_match = matching_box_by_text(ocr_data[1][0], localization_data)
    if best_match is not None:
        return best_match
    return matching_box_by_place(ocr_data[0], localization_data)


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


def process_result(result_data, localization_data):
    result_dict = {}
    for boxes in result_data:
        match = find_matching_box(boxes, localization_data)
        if match is None:
            continue
        # print(match["text"])
        value = find_box_of_value(match, boxes, result_data)
        if value is not None:
            # print(value[1][0])
            result_dict[match["text"]] = (value[1][0], boxes[1][1])
        # print("\n")
    return result_dict


file = io.open("../text_localization_ratio.json", encoding="utf-8")
localization = json.load(file)

paddle_ocr = PaddleOCR(use_angle_cls=True, lang='hu')

# Front
img_front_path = "../images/id_front.jpeg"
result_front = paddle_ocr.ocr(img_front_path, cls=True)

# Back
img_back_path = "../images/id_back.png"
result_back = paddle_ocr.ocr(img_back_path, cls=True)

# Normalize coordinates
img_front = cv2.imread(img_front_path)
normalize_coordinates(result_front[0], img_front.shape)
img_back = cv2.imread(img_back_path)
normalize_coordinates(result_back[0], img_back.shape)

# Process front result
dict_front = process_result(result_front[0], localization["front"])
# Process back result
dict_back = process_result(result_back[0], localization["back"])

person_front = dc.Person(dict_front)
person_back = dc.Person(dict_back)
merged = dc.merge(person_front, person_back)
# print(dc.merge(person_front, person_back))

# print(merged.to_dict())
json.dump(merged.to_dict(), sys.stdout, ensure_ascii=False)

# json.dump(dict_front, sys.stdout, ensure_ascii=False)
# print()
# json.dump(dict_back, sys.stdout, ensure_ascii=False)

