import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

from sklearn import metrics
from sklearn.neural_network import MLPClassifier
from sklearn.model_selection import cross_validate
from sklearn.model_selection import GridSearchCV
from sklearn.model_selection import KFold

# Loading train data and split into features and labels
train = pd.read_csv('./train.csv', delimiter=',', dtype=int)

X_train = train.ix[:, 1:].values.astype('float32')
Y_train = train.ix[:, 0]

# Loading test data and split into features and labels
test = pd.read_csv('./test.csv', dtype=int)

X_test = test.ix[:, 1:].values.astype('float32')
Y_test = test.ix[:, 0]

# Load unllabled set
to_predict = pd.read_csv('./mnist_test.csv', delimiter=',', dtype='float32', header=None)

# Rescale data to [0,1]
X_train /= 255.0
X_test /= 255.0
to_predict /= 255.0



mlp = MLPClassifier(activation='relu', solver='sgd',random_state=17,max_iter=200, learning_rate_init=0.2, learning_rate='constant', tol=0.00001)
mlp.fit(X_train, Y_train)
preds = mlp.predict(to_predict)
for i,p in enumerate(preds):
	print(str(i+1) + "," + str(p))
