#!/usr/bin/python3.5

"""
Team Green
"""

import time
import pandas as pd

from sklearn.neural_network import MLPClassifier
from sklearn import metrics


# Loading train data and split into features and labels
train = pd.read_csv('./train.csv', delimiter=',', dtype=int)

X_train = train.ix[:, 1:].values.astype('float32')
Y_train = train.ix[:, 0]

# Loading test data and split into features and labels
test = pd.read_csv('./test.csv', dtype=int)

X_test = test.ix[:, 1:].values.astype('float32') 
Y_test = test.ix[:, 0]

#Y_train, X_train = read_data('./train.csv')
#Y_test, X_test = read_data('./test.csv')

# Rescale data to [0,1]
X_train /= 255.0
X_test /= 255.0

start = time.time()

mlp = MLPClassifier(activation='relu', solver='sgd', learning_rate='constant', learning_rate_init = 0.2, hidden_layer_sizes=(100,), random_state=17)

mlp.fit(X_train,Y_train)

preds = mlp.predict(X_test)

end = time.time()

acc = metrics.accuracy_score(preds,Y_test)

print("Execution time [s]: " + str(end-start))
print("Accuracy [%]: " + str(acc*100))
print("\nConfusion matrix")
print(metrics.confusion_matrix(Y_test,preds))

[print(real,pred) for real, pred in zip(Y_test,preds)]
