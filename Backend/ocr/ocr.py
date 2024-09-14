import cv2
import io
import json
import math
import sys
from difflib import SequenceMatcher
from paddleocr import (PaddleOCR)

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
        value = find_box_of_value(match, boxes, result_data)
        if value is not None:
            result_dict[match["text"]] = (value[1][0], boxes[1][1])
    return result_dict


def map_dict_values(name, d):
    return {"documentName": name, "fieldsList": [{"title": key, "value": value[0]} for key, value in d.items()]}


# USAGE: python ocr.py --localization local.json --front image.jpg --back image.jpg
# optional argument for national id data validation: --national
# optional argument for document-name: --document-name samplename
img_front_path = None
img_back_path = None
localization_path = None
validate_data = False
document_name = None
for i in range(len(sys.argv)):
    print(sys.argv[i])
    if sys.argv[i] == "--front" and len(sys.argv) > i + 1:
        img_front_path = sys.argv[i + 1]
    elif sys.argv[i] == "--back" and len(sys.argv) > i + 1:
        img_back_path = sys.argv[i + 1]
    elif sys.argv[i] == "--localization" and len(sys.argv) > i + 1:
        localization_path = sys.argv[i + 1]
    elif sys.argv[i] == "--document-name" and len(sys.argv) > i + 1:
        document_name = sys.argv[i + 1]
    elif sys.argv[i] == "--national":
        validate_data = True

if img_front_path is None and img_back_path is None:
    print("ERROR: Please provide at least one image file!\n")
    exit()

if localization_path is None:
    print("ERROR: Please provide a localization file!\n")
    exit()

file = io.open(localization_path, encoding="utf-8")
localization = json.load(file)

paddle_ocr = PaddleOCR(use_angle_cls=True, lang='hu')

# Front
result_front = None
if img_front_path is not None:
    result_front = paddle_ocr.ocr(img_front_path, cls=True)
    # Normalize coordinates
    img_front = cv2.imread(img_front_path)
    normalize_coordinates(result_front[0], img_front.shape)

# Back
result_back = None
if img_back_path is not None:
    result_back = paddle_ocr.ocr(img_back_path, cls=True)
    # Normalize coordinates
    img_back = cv2.imread(img_back_path)
    normalize_coordinates(result_back[0], img_back.shape)

# Process front result
dict_front = None
if result_front is not None:
    dict_front = process_result(result_front[0], localization["front"])

# Process back result
dict_back = None
if result_back is not None:
    dict_back = process_result(result_back[0], localization["back"])

result = None
if dict_front is not None and dict_back is not None:
    if validate_data:
        id_front = dc.NationalId(dict_front)
        id_back = dc.NationalId(dict_back)
        result = (dc.merge(id_front, id_back)).to_dict()
    else:
        result = {**(map_dict_values(document_name, dict_front)), **(map_dict_values(document_name, dict_back))}
elif dict_front is not None:
    result = map_dict_values(document_name, dict_front)
elif dict_back is not None:
    result = map_dict_values(document_name, dict_back)

json_string = json.dumps(result, ensure_ascii=False).encode('utf8')
sys.stdout.reconfigure(encoding='utf-8')
print(json_string.decode())
