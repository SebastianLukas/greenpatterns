Greengroup
Readme and Report on exercise Keyword Spotting

Brief Project description and results
------------------------------------------------------------------------------
Python module/library
------------------------------------------------------------------------------
mlpy 3.5 (http://mlpy.sourceforge.net/) requirements etc. are documented in mlpy.pdf
PIL 1.1.7 (Pillow)
------------------------------------------------------------------------------
Sourcecode
------------------------------------------------------------------------------
Keyword Spotting Relevant classes: 
src/pr/DTW.py
toolkit.dtw.WordCutter (java)

------------------------------------------------------------------------------
Preprocessing
------------------------------------------------------------------------------
First we cut out all words with the WordCutter (java implementation). 

------------------------------------------------------------------------------
Feature Extraction (implemented in DTW.py)
------------------------------------------------------------------------------
For every pixel column of the image we calculated 3 features:
	- Number of black pixels
	- Number of black pixels divided by the total number of pixels (per column)
	- Number of black/white transitions

Thus we extracted a global feature vector for each image.
------------------------------------------------------------------------------
DTW Evaluation
------------------------------------------------------------------------------
In our DTW application we loop through the keyword list. First we search for one
word example in the train set.
After that we spot the N nearest words to our word example in the test set by using mlpy's DTW implementation.

Evaluation:
N: 50
Recall: 0.77027027027
Precision: 0.0106542056075

N: 15
Recall: 0.594594594595
Precision: 0.0274143302181

N: 10
Recall: 0.567567567568
Precision: 0.0392523364486


Downside of spotting N words is that a very low precision results. Furthermore there are many words in the keyword list
which doesn't occur in the test set.