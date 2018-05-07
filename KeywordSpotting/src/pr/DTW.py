import numpy as np
import mlpy
import os
from PIL import Image

#IMG_PATH = "../../data/cropped_bw_images/"
IMG_PATH = "../../data/border_cropped_bw/"
IMG_ENDING = "_bw.png"
TRANSCRIPTION = "../../data/transcription.txt"
KEYWORDS = "../../data/keywords.txt"
#KEYWORDS = "../../data/keywords_subset.txt"
TOP_N = 50


tp_total = 0
fp_total = 0
fn_total = 0


def extractFeatures(img):
    res = np.array([])
    for x in range(0, img.shape[1]):
        bCount = count = trans = 0
        for y in range(0, img.shape[0]):
            if img[y][x] == 0:
                bCount+=1  
            count+=1   
            if y > 0:
                if img[y][x] != img[y-1][x]:
                    trans+=1
        if bCount > 0:
            res = np.append(res,float(bCount)/count)
            res = np.append(res,bCount)
            res = np.append(res,trans)
  
    return res

def extractFeaturesDRAFT(img):
    res = np.array([])
    for x in range(0, img.shape[1]):
        bCount = count = trans = uCon = 0
        lCon = img.shape[0]
        for y in range(0, img.shape[0]):
            if img[y][x] == 0:
                lCon = count
                bCount+=1  
                if uCon == 0:
                    uCon = count
                    
            count+=1   
            if y > 0:
                if img[y][x] != img[y-1][x]:
                    trans+=1
        if bCount > 0:
            res = np.append(res,float(bCount)/count)
            res = np.append(res,bCount)
            #res = np.append(res,uCon)
            res = np.append(res,lCon)
            res = np.append(res,trans)
  
    return res


def calculateTestFeatures():
    dict = {}
    print "Start calculating features"
    for filename in os.listdir(IMG_PATH):
        if(int (filename[0:3]) >= 300):
            img = Image.open(IMG_PATH+filename)
            dict[filename] = extractFeatures(np.asarray(img))
    
    print "End calculating features"
    return dict


def getRecall(tp,fp,fn):
    if (tp+fn) == 0:
        return "NaN"
    else:
        return float(tp)/(tp+fn)  

def getPrecision(tp,fp,fn):
    if (tp+fp) == 0:
        return "NaN"
    else:
        return float(tp)/(tp+fp) 
                   
def evaluation(spottedDict,testSamples,totalWords,k):
    fp = 0
    fn = 0
    tp = 0
    total_samples = len(testSamples)
    for key in spottedDict:
        if key in testSamples:
            tp = tp + 1
            total_samples = total_samples - 1
        else:
            fp = fp + 1  
            
    fn =  total_samples
    
    global tp_total, fp_total, fn_total
    tp_total = tp_total + tp
    fp_total = fp_total + fp
    fn_total = fn_total + fn
    
    print "\n",k
    print "TP: ", tp
    print "FP: ", fp
    print "FN: ", fn

    print "Recall: ", getRecall(tp,fp,fn)
    print "Precision: ", getPrecision(tp,fp,fn)
    
    
    print "****************************************************************************************************"
    



def keywordSpotter(topN):
    f = open(KEYWORDS, 'r')
    keywords = f.readlines()
    f.close()
    featureDict = calculateTestFeatures()
    for k in keywords:
        trainsample = getTrainSample(k)
        #dictionary e.g.: {'302-30-07': 'h-u-n-d-r-e-d\n', '301-16-08': 'h-u-n-d-r-e-d\n'}
        testSamples = getTestSamples(k)
        img = Image.open(IMG_PATH+trainsample+IMG_ENDING)
        x = np.asarray(img)
        x = extractFeatures(x)

        distDict = {}
        totalWords = 0
        for filename in os.listdir(IMG_PATH):
           if(int (filename[0:3]) >= 300):
               totalWords +=1
               y = featureDict[filename]
               dist, cost, path = mlpy.dtw_std(x, y, dist_only=False)
               
               if len(distDict) < topN:
                   distDict[filename[0:9]] = dist
                   
               else:
                   if distDict[max(distDict, key=distDict.get)] > dist:
                       del distDict[max(distDict, key=distDict.get)]
                       distDict[filename[0:9]] = dist
          
              
           
        #print distDict  
        evaluation(distDict,testSamples,totalWords,k)
                   
                 
def getTrainSample(keyword):
    f = open(TRANSCRIPTION, 'r')
    words= f.readlines()
    f.close() 
    for w in words:
        if w[10:].lower() == keyword.lower() and int(w[0:3]) <300:
            #print w
            return w[0:9] 
            

            
def getTestSamples(keyword):
    d = {}
    f = open(TRANSCRIPTION, 'r')
    words= f.readlines()
    f.close() 
    for w in words:
        if w[10:].lower() == keyword.lower() and int(w[0:3]) >=300:
            d[w[0:9]] = keyword
    #print d        
    return d             

keywordSpotter(TOP_N)
print "\nTotal Recall & Precision:"
print "N:", TOP_N
print "Recall:", getRecall(tp_total,fp_total,fn_total)
print "Precision:", getPrecision(tp_total,fp_total,fn_total)

