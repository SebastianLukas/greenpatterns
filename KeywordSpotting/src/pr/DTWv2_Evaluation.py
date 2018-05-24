import numpy as np
import mlpy
import os
from PIL import Image

#IMG_PATH = "data/better_cropped_bw_image/"
IMG_PATH = "../../data/border_cropped_bw/"
#IMG_PATH = "data/croppedWhite/"
IMG_ENDING = "_bw.png"
TRANSCRIPTION = "../../data/transcription.txt"
KEYWORDS = "../../data/keywords.txt"
#KEYWORDS = "data/keywords_subset.txt"
#TOP_N = 20
TOP_N = 50

tp_total = 0
fp_total = 0
fn_total = 0


def extractFeatures(img):
    res = np.array([])
    for x in range(0, img.shape[1]):
        bCount = count = trans = uc = consectuiveBlackCounter = consectuiveBlack = 0
        for y in range(0, img.shape[0]):
            if img[y][x] == 0:
                bCount += 1
                consectuiveBlackCounter += 1
                if uc == 0:
                    uc = count + 1
            count += 1
            if y > 0:
                if img[y][x] != img[y - 1][x]:
                    trans += 1
                    if (consectuiveBlackCounter > consectuiveBlack):
                        consecutiveBlack = consectuiveBlackCounter
                        consectuiveBlackCounter = 0

        if bCount > 0:
            res = np.append(res, float(bCount) / count)
            res = np.append(res, bCount)
            res = np.append(res, trans)
            res = np.append(res, uc)
            res = np.append(res, consecutiveBlack)

    return res

def calculateTestFeatures():
    dict = {}
    print "Start calculating features"
    for filename in os.listdir(IMG_PATH):
        #if (int(filename[0:3]) >= 300):
        img = Image.open(IMG_PATH + filename)
        dict[filename] = extractFeatures(np.asarray(img))

    print "End calculating features"
    return dict


def getRecall(tp, fp, fn):
    if (tp + fn) == 0:
        return "NaN"
    else:
        return float(tp) / (tp + fn)


def getPrecision(tp, fp, fn):
    if (tp + fp) == 0:
        return "NaN"
    else:
        return float(tp) / (tp + fp)


def evaluation(spottedDict, totalWords, k):
    print k[:-1]+",",
    for i in spottedDict:
        print i +",", str(spottedDict[i]) +",",
    print ""


def keywordSpotter(topN):
    f = open(KEYWORDS, 'r')
    keywords = f.readlines()
    f.close()
    featureDict = calculateTestFeatures()
    for k in keywords:
        trainsample = getTrainSample(k)
        # dictionary e.g.: {'302-30-07': 'h-u-n-d-r-e-d\n', '301-16-08': 'h-u-n-d-r-e-d\n'}
        #testSamples = getTestSamples(k)
        img = Image.open(IMG_PATH + trainsample + IMG_ENDING)
        x = np.asarray(img)
        x = extractFeatures(x)

        distDict = {}
        totalWords = 0
        for filename in os.listdir(IMG_PATH):
            #if (int(filename[0:3]) >= 300):
            if (int(filename[0:3]) >= 200): #take all
                totalWords += 1
                y = featureDict[filename]
                dist, cost, path = mlpy.dtw_std(x, y, dist_only=False)

                if len(distDict) < topN:
                    distDict[filename[0:9]] = dist

                else:
                    if distDict[max(distDict, key=distDict.get)] > dist:
                        del distDict[max(distDict, key=distDict.get)]
                        distDict[filename[0:9]] = dist

        # print distDict
        evaluation(distDict, totalWords, k)


def getTrainSample(keyword):
    f = open(TRANSCRIPTION, 'r')
    words = f.readlines()
    f.close()
    for w in words:
        if w[10:].lower() == keyword.lower():# and int(w[0:3]) < 300:
            # print w
            return w[0:9]


def getTestSamples(keyword):
    d = {}
    f = open(TRANSCRIPTION, 'r')
    words = f.readlines()
    f.close()
    for w in words:
        if w[10:].lower() == keyword.lower():# and int(w[0:3]) >= 300:
            d[w[0:9]] = keyword
    # print d
    return d


keywordSpotter(TOP_N)