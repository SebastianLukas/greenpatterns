Greengroup
Readme and Report on exercise 02_a - SVM

Brief Project description and results
------------------------------------------------------------------------------
Libraries
------------------------------------------------------------------------------
libsvm 3.22

------------------------------------------------------------------------------
Sourcecode
------------------------------------------------------------------------------
SVM Relevant classes: 
toolkit.classifier.GridSearch
toolkit.classifier.Trainer.java 
toolkit.classifier.Predictor.java

Trainer and Predictor classes are more or less barbone wrapping classes to use libsvm. 

------------------------------------------------------------------------------
Grid Search
------------------------------------------------------------------------------
We investigated all 4 Kernels with performing a grid search in order to compute
good parameters.
We did grid search on a sample of 3000 instances, 10 fold crossvalidation
and probing for each cost C {4,8,..,32} gamma values {2^-10, 2^-9, ..., 1}  

Results with Cross Validation Accuracies are in
OKNNow\data\reports\gridsearch

------------------------------------------------------------------------------
Feature Selection and other processing on the data
------------------------------------------------------------------------------
Feature Selection did not prove useful to the extent we tried it.
The only processing we did on the data is scaling the 0 to 255 values into 0 to 1 
decimal values, this was required for the use with libsvm. 

------------------------------------------------------------------------------
Investigated Kernels 
------------------------------------------------------------------------------
We investigated the Polinomial and the RBF Kernels, those were the most promising
out of the grid search phase.

(details in OKNNow\data\reports\tests)

With RBF Kernel our best result is:
c=4, gamma=0.03125
Accuracy = 98.04013065795614% (14707/15001) (classification)
Total nSV = 9820

With Pol Kernel our best result is:
with c=32, gamma=0.5 degree=2
Accuracy = 97.48016798880074% (14623/15001) (classification)
Total nSV = 5165

Values are computed using the entire datasets

For RBF we did also run a classification on 10 fold crossvalidation and the 
success rate did really well
C=4, gamma=0.03125, 10 fold crossvalidation on entire dataset
Cross Validation Accuracy = 97.94792013927473%
Total nSV = 9161