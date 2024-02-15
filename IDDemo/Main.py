import cv2
import pytesseract
import numpy as np

image = cv2.imread("..\\images\\mrp_id_crop.png")

# cv2.imshow('Original', image)

# RESIZE
# image = cv2.resize(image, None, fx=0.5, fy=0.5, interpolation=cv2.INTER_AREA)
# cv2.imshow('Resized', image)

# GRAYSCALE
# gray = cv2.cvtColor(cv2.GaussianBlur(image, (3, 3), 1), cv2.COLOR_BGR2GRAY)
gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
cv2.imshow('gray', gray)

# THRESHOLD
#     SIMPLE
# ret, thresh = cv2.threshold(gray, 172, 255, cv2.THRESH_TRUNC)
#     ADAPTIVE
# thresh = cv2.adaptiveThreshold(gray, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY, 31, 2)
#     OTSU
thresh = cv2.threshold(gray, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)[1]

cv2.imshow("Threshold", thresh)

h, w, c = image.shape

lang = "eng"
config = r"--psm 6 --oem 3"

boxes = pytesseract.image_to_boxes(image=thresh, lang=lang, config=config)

text = pytesseract.image_to_string(image=thresh, lang=lang, config=config)
print(text)

for b in boxes.splitlines():
    b = b.split(' ')
    image = cv2.rectangle(image, (int(b[1]), h - int(b[2])), (int(b[3]), h - int(b[4])), (0, 255, 0), 2)

# cv2.imshow("Boxes", image)

cv2.waitKey(0)
