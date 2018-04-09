# Pattern Recognition - 2b MLP
In this project we train a MultilayerPerceptron Classifier with a single hidden layer to classify handwritten digits. We use the [MNIST](http://yann.lecun.com/exdb/mnist/) data set to train and evaluate our models. By changing the size of the hidden layer, the learning rate and the initial random state, we try to optimize the performance regarding prediction accuracy.

**Requirements**
- pandas 0.22.0
- numpy 1.14.2
- scikit-learn  0.19.1


**Parameters and Accuracy**

|Num Neurons  | learning rate | acc.|
|--|--|--|
100 | 0.2 |0.9721
100 | 0.3 |0.9718
100 | 0.1| 0.9710
 90 | 0.3 |0.9709
 90 | 0.2| 0.9706
...|...|...
20 |0.9| 0.1422
30 |1.0 |0.1387
10 | 0.8 | 0.1379
10 |0.9 | 0.1067
10 |1.0 | 0.1033

Loss vs number of trainings iteration
```
![Plot for MLP - Loss vs. number of trainings interation](/loss_vs_nbr_iter_00001.png)
```

**Best parameters:**
- hidden layer size: 100
- learning rate: 0.2
- random_state: 17 

**Final accuracy [%]: 97.334**

Execution time [s]: 14.981 

