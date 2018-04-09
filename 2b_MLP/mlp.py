#!/usr/bin/python3.5

"""
Team Green
"""

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


# Rescale data to [0,1]
X_train /= 255.0
X_test /= 255.0

# Define parameters for GridSearchCV
params = [{'learning_rate_init': np.linspace(0.1,1,2), 
	'hidden_layer_sizes':np.linspace(10,100,1, dtype=int)},
	{'random_state':[3,8,17,22,24,35,49,89]},
	{'alpha': 10.0 ** -np.arange(1,7)} ## we did not use this, due to time limitations

]

num_cv = 10

## Initialize MultilayerPerceptron 
mlp = MLPClassifier(activation='relu', solver='sgd',random_state=1,max_iter=200, learning_rate='constant', tol=0.00001)

gs_mlp = GridSearchCV(mlp,param_grid=params[0],scoring='accuracy', n_jobs=-1, cv=num_cv, refit=False)

## Train and evaluate the classifier using cross validation
gs_mlp.fit(X_train,Y_train)

### Store results of gridsearchcv
params_gs1 = gs_mlp.cv_results_['params']
score_gs1 = gs_mlp.cv_results_['mean_test_score']

### store best parameters
best_num_neur = gs_mlp.best_params_['hidden_layer_sizes']
best_learn_rate = gs_mlp.best_params_['learning_rate_init']


print("Hidden layer size | learning_rate | accuracy ")
print("------------------|---------------|----------------")
for i in range(len(params_gs1)):
	print('{:3.0f} | {:.1f} | {:.4f}'.format(params_gs1[i]['hidden_layer_sizes'], params_gs1[i]['learning_rate_init'],score_gs1[i]))


## Print best number of neurons and learning rate
#print(score_gs1)
print("best num neur: " + str(best_num_neur))
print("best learn rate: " + str(best_learn_rate))


## Create 'manually' train and test set for cross validation
## It is somehow not possible not access the classifiers attribute when using GridSearchCV
kf = KFold(n_splits=num_cv, random_state=2, shuffle=True)
acc = []
num_iter = []
loss = []

## Init plot
a = plt.subplot() 
a.set_title('Loss vs. nbr Iterations')
a.set_xlabel('nbr iterations')
a.set_ylabel('loss')

## Do crossvalidation and add loss and number of iterations to the plot
for train_idx, test_idx in kf.split(X_train):
	train, train_labels = X_train[train_idx], Y_train[train_idx]
	test, test_labels = X_train[test_idx], Y_train[test_idx]

	mlp_2 = MLPClassifier(activation='relu', solver='sgd', random_state=1, max_iter = 200, learning_rate='constant', hidden_layer_sizes=best_num_neur, learning_rate_init=best_learn_rate, tol=1e-6)
	mlp_2.fit(train, train_labels)
	preds = mlp_2.predict(test)
	acc.append(metrics.accuracy_score(preds,test_labels))
	num_iter.append(mlp_2.n_iter_)
	loss.append(mlp_2.loss_)
	a.plot(mlp_2.loss_curve_,label='cv | nbr_itr = ' + repr(mlp_2.n_iter_))

mlp_2.fit(X_train,Y_train)
preds = mlp_2.predict(X_test)
acc_ = metrics.accuracy_score(preds, Y_test)
acc.append(acc_)
a.plot(mlp_2.loss_curve_,label='all train | nbr_itr = ' + repr(mlp_2.n_iter_))
a.legend()

plt.show()


# Initialize MultilayerPerceptron for random states
mlp_3 = MLPClassifier(activation='relu', solver='sgd',max_iter=200, learning_rate='constant', tol=0.00001, hidden_layer_sizes=(100,), learning_rate_init=0.2)

gs_mlp_3 = GridSearchCV(mlp_3,param_grid=params[1],scoring='accuracy', n_jobs=-1, cv=10, refit=False)

gs_mlp_3.fit(X_train,Y_train)

## Store results of gridsearchcv
params_gs3 = gs_mlp_3.cv_results_['params']
score_gs3 = gs_mlp_3.cv_results_['mean_test_score']

## store best parameters
best_rndm = gs_mlp_3.best_params_['random_state']


plt.show()

print("optimal number of neurons: " + str(best_num_neur))
print("optimal learning rate: " + str(best_learn_rate))
print("best random init state" + str(best_rndm))
