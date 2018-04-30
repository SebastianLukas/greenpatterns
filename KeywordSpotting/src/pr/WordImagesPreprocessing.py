import cv2 as cv
import os
import numpy as np
from PIL import Image, ImageChops

def cropWhite(filename):
    image = Image.open(os.path.join(path, filename), 'r')
    bg = Image.new(image.mode, image.size, image.getpixel((0, 0)))
    diff = ImageChops.difference(image, bg)
    bbox = diff.getbbox()
    if bbox:
        image = image.crop(bbox)
        image.save(filename)

def scale_image(filename):
    img = Image.open(os.path.join(path, filename), 'r')
    img = img.resize((200,200), )
    img.save(filename)

def read_transparent_png(filename):
    image_4channel = cv.imread(filename, cv.IMREAD_UNCHANGED)
    alpha_channel = image_4channel[:,:,3]
    rgb_channels = image_4channel[:,:,:3]

    # White Background Image
    white_background_image = np.ones_like(rgb_channels, dtype=np.uint8) * 255

    # Alpha factor
    alpha_factor = alpha_channel[:,:,np.newaxis].astype(np.float32) / 255.0
    alpha_factor = np.concatenate((alpha_factor,alpha_factor,alpha_factor), axis=2)

    # Transparent Image Rendered on White Background
    base = rgb_channels.astype(np.float32) * alpha_factor
    white = white_background_image.astype(np.float32) * (1 - alpha_factor)
    final_image = base + white
    return final_image.astype(np.uint8)

def binarize(filename):
    if filename.endswith(".png"):
        img = Image.open(os.path.join(path, filename),'r')
        width, height = img.size
        for x in range(width):
            for y in range(height):
                r, g, b, a = img.getpixel((x, y))
                if b > 205 and r > 205 and g > 205:
                    img.putpixel((x, y), 255)
        img.save(filename)

        img = read_transparent_png(os.path.join(path, filename))
        img = cv.cvtColor(img, cv.COLOR_BGR2GRAY)

        ret3, th3 = cv.threshold(img, 0, 255, cv.THRESH_OTSU)
        cv.imwrite(filename.replace('.png', '_bw.png'), th3)

# path = os.path.abspath('cropped_images');
path = os.path.abspath('../../data/better_cropped_bw_image')
for filename in os.listdir(path):
    # binarize(filename)
    scale_image(filename)
    cropWhite(filename)