# comment-analysis

Implementation of a comment classifier using Machine Learning presented in the AI ​​discipline discipline of computer science course.  
Created a comment classifier using Machine Learning with the Naive Bayes algorithm, with the objective of classifying 
comments in two classes, Positive (1) and Negative (0). For more details visit [blog](http://127.0.0.1:4000/2018/Clojure-Classificacao-de-comentarios-com-machine-learning/)(in portuguese)

## Usage

First you must have Clojure and Leiningen installed [Clojure](https://clojure.org/guides/getting_started) and
[Leiningen](https://leiningen.org/#install).


In a directory in the root of the project called 'comments' you have the files where the comments are that will be used 
for training separated in two files.  
- CommentsNegative
- CommentsPositive

The comments are in the following format  
```
"Coments are;0"
```
Where the ';' is the delimiter between the comment and its class  
0 - Is class Negative  
1 - Is class Positive  


In the tests performed using 1317 positive and negative comments, ~70% for training and ~30% for testing, I obtained the 
following results.
```
{
    :average-cost 0.0,
    :incorrect 282.0,
    :roc-area {
        :0 0.7298043566982961,
        :1 0.7350174727068667
    },
    :false-positive-rate {
        :0 0.4696969696969697,
        :1 0.24242424242424243
    },
    :unclassified 0.0,
    :sf-entropy-gain -31651.27976575737,
    :kb-mean-information 0.2968905922187232,
    :kb-information 235.1373490372288,
    :percentage-incorrect 35.60606060606061,
    :root-relative-squared-error 105.09300991727571,
    :precision {
        :0 0.6172839506172839,
        :1 0.6862745098039216
    },
    :error-rate 0.3560606060606061,
    :percentage-unclassified 0.0,
    :recall {
        :0 0.7575757575757576,
        :1 0.5303030303030303
    },
    :correlation-coefficient {
        :nan Can't compute correlation coefficient: class is nominal!
    },
    :mean-absolute-error 0.35087054809357776,
    
    :summary 
    Correctly Classified Instances         510               64.3939 %
    Incorrectly Classified Instances       282               35.6061 %
    Kappa statistic                          0.2879
    Mean absolute error                      0.3509
    Root mean squared error                  0.5255
    Relative absolute error                 70.1741 %
    Root relative squared error            105.093  %
    Coverage of cases (0.95 level)          84.0909 %
    Mean rel. region size (0.95 level)      68.8763 %
    Total Number of Instances              792,

    :kb-relative-information 23513.73490372288,
    :false-negative-rate {
        :0 0.24242424242424243,
        :1 0.4696969696969697
    },
    :relative-absolute-error 70.17410961871555,
    :root-mean-squared-error 0.5254650495863785,
    :sf-mean-entropy-gain -39.963737077976475,
    :evaluation-object #object[weka.classifiers.Evaluation 0x687a762c weka.classifiers.Evaluation@687a762c],
    
    :confusion-matrix === Confusion Matrix ===
    a   b   <-- classified as
    300  96 |   a = 0
    186 210 |   b = 1,
    
    :kappa 0.28787878787878785,
    :f-measure {
        :0 0.6802721088435375,
        :1 0.5982905982905983
    },
    :percentage-correct 64.39393939393939,
    :correct 510.0
}
```

### Considerations  
I observed that some comments with class "Negative" were positive comments and the opposite also happened, this may 
cause some inconsistencies in the algorithm, which may cause poor performance.


