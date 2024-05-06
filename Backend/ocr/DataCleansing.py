import re
from datetime import datetime


class Person:
    def __init__(self, data=None):
        self.name = None
        self.name_confidence = 0
        self.sex = None
        self.sex_confidence = 0
        self.nationality = None
        self.nationality_confidence = 0
        self.birth = None
        self.birth_confidence = 0
        self.expiry = None
        self.expiry_confidence = 0
        self.doc_nr = None
        self.doc_nr_confidence = 0
        self.can = None
        self.can_confidence = 0
        self.place_of_birth = None
        self.place_of_birth_confidence = 0
        self.birth_name = None
        self.birth_name_confidence = 0
        self.mother = None
        self.mother_confidence = 0
        self.authority = None
        self.authority_confidence = 0
        self.mrz = None
        self.mrz_confidence = 0
        if data is not None:
            self.add_data(data)

    def add_data(self, data):
        for key, value in data.items():
            if key == "Családi és utónév/Family name and Given name:":
                self.name = value[0]
                self.name_confidence = value[1]
            elif key == "Nem/Sex:":
                male = ("M" in value[0] or "FÉRFI" in value[0] or "FERFI" in value[0]) and (
                        "NO" not in value[0] and "NŐ" not in value[0])
                female = ("F" in value[0] or "NŐ" in value[0] or "NO" in value[0]) and (
                        "M" not in value[0] and "FÉRFI" not in value[0] and "FERFI" not in value[0])
                if male and female:
                    continue
                if male:
                    self.sex = "M"
                else:
                    self.sex = "F"
                self.sex_confidence = value[1]
            elif key == "Állampolgárság/Nationality:":
                self.nationality = value[0][-3:]
                self.nationality_confidence = value[1]
            elif key == "Születési idő/Date of birth:":
                self.birth = transform_date(value[0])
                self.birth_confidence = value[1]
            elif key == "Érvényességi idő/Date of expiry:":
                self.expiry = transform_date(value[0])
                self.expiry_confidence = value[1]
            elif key == "Okmányazonosító/Doc.No.:":
                self.doc_nr = value[0]
                self.doc_nr_confidence = value[1]
            elif key == "CAN:":
                self.can = re.sub('[^0-9]', '', value[0])
                self.can_confidence = value[1]
            elif key == "Születési hely/Place of birth:":
                self.place_of_birth = value[0]
                self.place_of_birth_confidence = value[1]
            elif key == "Születési családi és utónév/Family name and Given name at birth:":
                self.birth_name = value[0]
                self.birth_name_confidence = value[1]
            elif key == "Anyja születési neve/Mother's maiden name:":
                self.mother = value[0]
                self.mother_confidence = value[1]
            elif key == "Kiállító hatóság/Issuing authority:":
                self.authority = value[0]
                self.authority_confidence = value[1]

        if data.get("MRZ_1") is not None and data.get("MRZ_2") is not None and data.get("MRZ_3") is not None:
            self.mrz = (data["MRZ_1"], data["MRZ_2"], data["MRZ_3"])
            self.process_mrz()

    def process_mrz(self):
        # Checksum for the first two rows
        lines_concat = self.mrz[0][0] + re.sub('[^0-9]', '', self.mrz[1][0])
        text = lines_concat[5:-1]
        if not check_sum(text, lines_concat[-1]):
            print("ERROR: MRZ check digit not matching for the first two rows!\n", text)
            return

        # Name
        text = self.mrz[2][0]
        self.name = text.replace("<<", " ", 1).replace("<", "")
        self.name_confidence = self.mrz[2][1]

        # Sex
        text = self.mrz[1][0]
        self.sex = text[7]
        self.sex_confidence = self.mrz[1][1]

        # Nationality
        text = self.mrz[1][0]
        self.nationality = text[15:18]
        self.nationality_confidence = self.mrz[1][1]

        # Document number
        text = self.mrz[0][0]
        id = text[5:13]
        if check_sum(id, text[14]):
            self.doc_nr = id
            self.doc_nr_confidence = 1
        else:
            print("ERROR: MRZ check digit not matching for document number!\n", id)

        # Date of birth
        text = self.mrz[1][0]
        dob = text[0:6]
        if check_sum(dob, text[6]):
            self.birth = dob
            self.birth_confidence = 1
        else:
            print("ERROR: MRZ check digit not matching for date of birth!\n", dob)

        # Date of expiry
        text = self.mrz[1][0]
        date_of_expiry = text[8:14]
        if check_sum(date_of_expiry, text[14]):
            self.expiry = date_of_expiry
            self.expiry_confidence = 1
        else:
            print("ERROR: MRZ check digit not matching for date of expiry!\n", date_of_expiry)

    def __str__(self):
        return (f"Családi és utónév/Family name and Given name:\n\t{self.name} {self.name_confidence}"
                f"\nNem/Sex:\n\t{self.sex} {self.sex_confidence}"
                f"\nÁllampolgárság/Nationality:\n\t{self.nationality} {self.nationality_confidence}"
                f"\nSzületési idő/Date of birth:\n\t{self.birth} {self.birth_confidence}"
                f"\nÉrvényességi idő/Date of expiry:\n\t{self.expiry} {self.expiry_confidence}"
                f"\nOkmányazonosító/Doc.No.:\n\t{self.doc_nr} {self.doc_nr_confidence}"
                f"\nCAN:\n\t{self.can} {self.can_confidence}"
                f"\nSzületési hely/Place of birth:\n\t{self.place_of_birth} {self.place_of_birth_confidence}"
                f"\nSzületési családi és utónév/Family name and Given name at birth:\n\t{self.birth_name} {self.birth_name_confidence}"
                f"\nAnyja születési neve/Mother's maiden name:\n\t{self.mother} {self.mother_confidence}"
                f"\nKiállító hatóság/Issuing authority:\n\t{self.authority} {self.authority_confidence}")

    def to_dict(self):
        return {"name": self.name,
                "sex": self.sex,
                "nationality": self.nationality,
                "dateOfBirth": convert_date(self.birth),
                "dateOfExpiry": convert_date(self.expiry),
                "documentNr": self.doc_nr,
                "can": self.can,
                "placeOfBirth": self.place_of_birth,
                "nameAtBirth": self.birth_name,
                "mothersName": self.mother,
                "authority": self.authority}


def transform_date(text):
    return text[-2:] + text[2:4] + text[:2]


def convert_date(date_str):
    date_obj = datetime.strptime(date_str, "%y%m%d")
    formatted_date = date_obj.strftime("%Y-%m-%d")
    return formatted_date


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


def merge(person1: Person, person2: Person):
    merged_person = Person()
    if person1.name_confidence < person2.name_confidence:
        merged_person.name = person2.name
        merged_person.name_confidence = person2.name_confidence
    else:
        merged_person.name = person1.name
        merged_person.name_confidence = person1.name_confidence

    if person1.sex_confidence < person2.sex_confidence:
        merged_person.sex = person2.sex
        merged_person.sex_confidence = person2.sex_confidence
    else:
        merged_person.sex = person1.sex
        merged_person.sex_confidence = person1.sex_confidence

    if person1.nationality_confidence < person2.nationality_confidence:
        merged_person.nationality = person2.nationality
        merged_person.nationality_confidence = person2.nationality_confidence
    else:
        merged_person.nationality = person1.nationality
        merged_person.nationality_confidence = person1.nationality_confidence

    if person1.mother_confidence < person2.mother_confidence:
        merged_person.mother = person2.mother
        merged_person.mother_confidence = person2.mother_confidence
    else:
        merged_person.mother = person1.mother
        merged_person.mother_confidence = person1.mother_confidence

    if person1.expiry_confidence < person2.expiry_confidence:
        merged_person.expiry = person2.expiry
        merged_person.expiry_confidence = person2.expiry_confidence
    else:
        merged_person.expiry = person1.expiry
        merged_person.expiry_confidence = person1.expiry_confidence

    if person1.authority_confidence < person2.authority_confidence:
        merged_person.authority = person2.authority
        merged_person.authority_confidence = person2.authority_confidence
    else:
        merged_person.authority = person1.authority
        merged_person.authority_confidence = person1.authority_confidence

    if person1.doc_nr_confidence < person2.doc_nr_confidence:
        merged_person.doc_nr = person2.doc_nr
        merged_person.doc_nr_confidence = person2.doc_nr_confidence
    else:
        merged_person.doc_nr = person1.doc_nr
        merged_person.doc_nr_confidence = person1.doc_nr_confidence

    if person1.can_confidence < person2.can_confidence:
        merged_person.can = person2.can
        merged_person.can_confidence = person2.can_confidence
    else:
        merged_person.can = person1.can
        merged_person.can_confidence = person1.can_confidence

    if person1.place_of_birth_confidence < person2.place_of_birth_confidence:
        merged_person.place_of_birth = person2.place_of_birth
        merged_person.place_of_birth_confidence = person2.place_of_birth_confidence
    else:
        merged_person.place_of_birth = person1.place_of_birth
        merged_person.place_of_birth_confidence = person1.place_of_birth_confidence

    if person1.birth_confidence < person2.birth_confidence:
        merged_person.birth = person2.birth
        merged_person.birth_confidence = person2.birth_confidence
    else:
        merged_person.birth = person1.birth
        merged_person.birth_confidence = person1.birth_confidence

    if person1.birth_name_confidence < person2.birth_name_confidence:
        merged_person.birth = person2.birth
        merged_person.birth_name_confidence = person2.birth_name_confidence
    else:
        merged_person.birth = person1.birth
        merged_person.birth_name_confidence = person1.birth_name_confidence

    return merged_person
