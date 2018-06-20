# comment-analysis

Implementation of a comment classifier using Machine Learning presented in the AI ​​course of Computer Science course

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

