from paddleocr import (PaddleOCR,
                       draw_ocr)

paddle_ocr = PaddleOCR(use_angle_cls=True, lang='en')

img_path = '../images/mrp_id_crop.png'
result = paddle_ocr.ocr(img_path, cls=True)
for idx in range(len(result)):
    res = result[idx]
    for line in res:
        print(line)


# draw result
from PIL import Image
result = result[0]
image = Image.open(img_path).convert('RGB')
boxes = [line[0] for line in result]
txts = [line[1][0] for line in result]
scores = [line[1][1] for line in result]
font = '../arial.ttf'
im_show = draw_ocr(image, boxes, txts, scores, font_path=font)
im_show = Image.fromarray(im_show)
im_show.save('result.jpg')