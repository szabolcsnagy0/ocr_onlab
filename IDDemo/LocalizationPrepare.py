from paddleocr import (PaddleOCR, draw_ocr)
import math
import cv2

paddle_ocr = PaddleOCR(use_angle_cls=True, lang='hu')

img_path = '../images/id_back_ref.jpeg'
img = cv2.imread(img_path)
result_front = paddle_ocr.ocr(img_path, cls=True)

print(img.shape)

for line in result_front[0]:
    coords = line[0]
    print(line[1])
    for coord in coords:
        coord[0] = coord[0] / img.shape[1]
        coord[1] = coord[1] / img.shape[0]
    print(line)
    print("\n\n")
