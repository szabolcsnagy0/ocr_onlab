import cv2
import numpy as np
import sys
import json

def order_points(pts):
    '''Rearrange coordinates to order:
      top-left, top-right, bottom-right, bottom-left'''
    rect = np.zeros((4, 2), dtype='float32')
    pts = np.array(pts)
    s = pts.sum(axis=1)
    # Top-left point will have the smallest sum.
    rect[0] = pts[np.argmin(s)]
    # Bottom-right point will have the largest sum.
    rect[2] = pts[np.argmax(s)]

    diff = np.diff(pts, axis=1)
    # Top-right point will have the smallest difference.
    rect[1] = pts[np.argmin(diff)]
    # Bottom-left will have the largest difference.
    rect[3] = pts[np.argmax(diff)]
    # Return the ordered coordinates.
    return rect.astype('int').tolist()

def corrigate_coordinates(points):
    corrected_points = []
    for point in points:
        x = int(point[0] * RESIZE_FACTOR)
        y = int(point[1] * RESIZE_FACTOR)
        corrected_points.append([x, y])
    return corrected_points

def coordinates_ratio(points, img_size):
    rations = []
    for point in points:
        x = point[0] / img_size[1]
        y = point[1] / img_size[0]
        rations.append([x, y])
    return rations

# USAGE: python corners.py image.jpg
if len(sys.argv) != 2:
    print("ERROR: Not enough arguments!\n")
    exit()

img = sys.argv[1]

if img is None:
    print("ERROR: Please provide an image file!\n")
    exit()

img = cv2.imread(img)
RESIZE_FACTOR = 0.15
img = cv2.resize(img, None, fx=RESIZE_FACTOR, fy=RESIZE_FACTOR, interpolation=cv2.INTER_AREA)
original_image = img

# Remove text
kernel = np.ones((4, 4), np.uint8)
img = cv2.morphologyEx(img, cv2.MORPH_CLOSE, kernel, iterations=3)

# Background
mask = np.zeros(img.shape[:2], np.uint8)
bgdModel = np.zeros((1, 65), np.float64)
fgdModel = np.zeros((1, 65), np.float64)
rect = (20, 20, img.shape[1] - 20, img.shape[0] - 20)
cv2.grabCut(img, mask, rect, bgdModel, fgdModel, 10, cv2.GC_INIT_WITH_RECT)
mask2 = np.where((mask == 2) | (mask == 0), 0, 1).astype('uint8')
img = img * mask2[:, :, np.newaxis]

# Edge Detection
gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
gray = cv2.GaussianBlur(gray, (11, 11), 0)
canny = cv2.Canny(gray, 0, 200)
canny = cv2.dilate(canny, cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (5, 5)))

# Contour Detection
con = np.zeros_like(img)
# Finding contours for the detected edges.
contours, hierarchy = cv2.findContours(canny, cv2.RETR_LIST, cv2.CHAIN_APPROX_NONE)
# Keeping only the largest detected contour.
page = sorted(contours, key=cv2.contourArea, reverse=True)[:5]

# Corners
# Blank canvas.
con = np.zeros_like(img)
# Loop over the contours.
for c in page:
    # Approximate the contour.
    epsilon = 0.02 * cv2.arcLength(c, True)
    corners = cv2.approxPolyDP(c, epsilon, True)
    # If our approximated contour has four points
    if len(corners) == 4:
        break

# Sorting the corners and converting them to desired shape.
corners = sorted(np.concatenate(corners).tolist())

corners = order_points(corners)
# corners = corrigate_coordinates(corners)
ration = coordinates_ratio(corners, img.shape)

json.dump(ration, sys.stdout)
