from paddleocr import (PaddleOCR,
                       draw_ocr)
import re
import sys
import json


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

    def to_dictionary(self):
        return {"Name": self.name,
                "Date of birth": self.date_of_birth,
                "Document number": self.id,
                "Date of expiry": self.date_of_expiry,
                "Sex": self.sex,
                "Nationality": self.nationality}


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


# USAGE: python ocr.py --front image.jpg --back image.jpg
img_front = None
img_back = None
for i in range(len(sys.argv)):
    print(sys.argv[i])
    if sys.argv[i] == "--front" and len(sys.argv) > i + 1:
        img_front = sys.argv[i + 1]
    elif sys.argv[i] == "--back" and len(sys.argv) > i + 1:
        img_back = sys.argv[i + 1]

if img_front is None and img_back is None:
    print("ERROR: Please provide an image file!\n")
    exit()

paddle_ocr = PaddleOCR(use_angle_cls=True, lang='hu')

data_front = None
data_back = None
# Front
if img_front is not None:
    result_front = paddle_ocr.ocr(img_front, cls=True)
    data_front = process_front(result_front[0])

# Back
if img_back is not None:
    result_back = paddle_ocr.ocr(img_back, cls=True)
    data_back = process_back(result_back[0])

if data_front is not None and data_back is not None:
    Person().merge_data(data_front, data_back).print()
elif data_front is not None:
    json.dump(data_front.to_dictionary(), sys.stdout)
elif data_back is not None:
    json.dump(data_back.to_dictionary(), sys.stdout)
