import numpy as np
import cv2
import sys

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

def find_dest(pts):
    (tl, tr, br, bl) = pts
    # Finding the maximum width.
    widthA = np.sqrt(((br[0] - bl[0]) ** 2) + ((br[1] - bl[1]) ** 2))
    widthB = np.sqrt(((tr[0] - tl[0]) ** 2) + ((tr[1] - tl[1]) ** 2))
    maxWidth = max(int(widthA), int(widthB))

    # Finding the maximum height.
    heightA = np.sqrt(((tr[0] - br[0]) ** 2) + ((tr[1] - br[1]) ** 2))
    heightB = np.sqrt(((tl[0] - bl[0]) ** 2) + ((tl[1] - bl[1]) ** 2))
    maxHeight = max(int(heightA), int(heightB))
    # Final destination co-ordinates.
    destination_corners = [[0, 0], [maxWidth, 0], [maxWidth, maxHeight], [0, maxHeight]]

    return order_points(destination_corners)

def corrigate_coordinates(points):
    corrected_points = []
    for point in points:
        x = int(point[0] / RESIZE_FACTOR)
        y = int(point[1] / RESIZE_FACTOR)
        corrected_points.append([x, y])
    return corrected_points

def coordinates_ratio(ratios, img_size):
    points = []
    for ratio in ratios:
        x = ratio[0] * img_size[0]
        y = ratio[1] * img_size[1]
        points.append([x, y])
    return points

# USAGE: python crop.py --src image.jpg --corners 5 5 3 3 2 2 1 1

img_path = None
corners = []
print(sys.argv)
for i in range(len(sys.argv)):
    if sys.argv[i] == "--src" and len(sys.argv) > i + 1:
        img_path = sys.argv[i + 1]
    elif sys.argv[i] == "--corners" and len(sys.argv) > i + 8:
        corners_all = sys.argv[i+1:]
        corners.append([float(corners_all[0]), float(corners_all[1])])
        corners.append([float(corners_all[2]), float(corners_all[3])])
        corners.append([float(corners_all[4]), float(corners_all[5])])
        corners.append([float(corners_all[6]), float(corners_all[7])])

if img_path is None or corners is None or len(corners) != 4:
    print("ERROR: Please provide the required parameters!\n")
    exit()

print(corners)
img = cv2.imread(img_path)
RESIZE_FACTOR = 0.3
img = cv2.resize(img, None, fx=RESIZE_FACTOR, fy=RESIZE_FACTOR, interpolation=cv2.INTER_AREA)

# corners = corrigate_coordinates(corners)
corners = coordinates_ratio(corners, img.shape)
corners = order_points(corners)
print(corners)
destination_corners = find_dest(corners)

h, w = img.shape[:2]
# Getting the homography.
M = cv2.getPerspectiveTransform(np.float32(corners), np.float32(destination_corners))
# Perspective transform using homography.
final = cv2.warpPerspective(img, M, (destination_corners[2][0], destination_corners[2][1]),
                            flags=cv2.INTER_LINEAR)
cv2.imwrite(img_path, final)