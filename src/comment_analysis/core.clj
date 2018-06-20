(ns comment-analysis.core
  (:require [comment-analysis.pre-processing :as preprocessing]
            [clojure.string :as s]
            [clojure.set :as set])
  (:use [clj-ml classifiers data filters]))

(defn read-file-comments-negative
  []
  (s/split (slurp "comments/CommentsNegative") #"\n"))

(defn read-file-comments-positive
  []
  (s/split (slurp "comments/CommentsPositive") #"\n"))

(defn get-comment-and-class
  [comment]
  (let [comment-class (s/split comment #"(;)")
        comment (preprocessing/preprocess (first comment-class))
        class (Integer/parseInt (last comment-class))]
    {:comment comment
     :class class}))

(defn get-frequencies-words
  [comments-class]
  (let [class (:class comments-class)
        comment (:comment comments-class)
        frequencies-words (frequencies (map keyword (preprocessing/get-stemmin-words comment)))]
    (conj {:class class} frequencies-words)))

(defn define-data-words-negative
  []
  (let [comments (read-file-comments-negative)
        comments-class (mapv get-comment-and-class comments)]
    (mapv get-frequencies-words comments-class)))

(defn define-data-words-positive
  []
  (let [comments (read-file-comments-positive)
        comments-class (mapv get-comment-and-class comments)]
    (mapv get-frequencies-words comments-class)))

(defn divide-dataset
  [ds]
  (let [total (count ds)
        train (int (* 0.7 total))
        test (- total train)]
    {:train (into [] (take train ds))
     :test (into [] (take-last test ds))}))

(defn define-data-words
  []
  (let [data-words-negative (->
                              (shuffle (define-data-words-negative))
                              (divide-dataset))
        data-words-positive (->
                              (shuffle (define-data-words-positive))
                              (divide-dataset))]
    {:train (set/union (data-words-negative :train) (data-words-positive :train))
     :test (set/union (data-words-negative :test) (data-words-positive :test))}))


(defn make-train-dataset
  [bag-of-words train-predataset]
  (let [train-dataset (make-dataset "train-dataset" bag-of-words train-predataset)
        train-dataset (make-apply-filter :numeric-to-nominal {:attributes [:class]} train-dataset)
        train-dataset (dataset-set-class train-dataset :class)]
    train-dataset))

(defn make-test-dataset
  [bag-of-words test-predataset]
  (let [test-dataset (make-dataset "train-dataset" bag-of-words test-predataset)
        test-dataset (make-apply-filter :numeric-to-nominal {:attributes [:class]} test-dataset)
        test-dataset (dataset-set-class test-dataset :class)]
    test-dataset))


(defn train-and-evaluate-classifier
  [train-dataset test-dataset]
  (let [classifier (->
                     (make-classifier :bayes :naive {:supervised-discretization true})
                     (classifier-train train-dataset))
        evaluation (classifier-evaluate classifier :dataset train-dataset test-dataset)]
    (println evaluation)))


(defn main
  []
  (let [data-words (define-data-words)
        bag-of-words (vec (preprocessing/make-bag-of-words (mapv keys (data-words :train)) {}))
        train-predataset (preprocessing/prepare-dataset (data-words :train) bag-of-words)
        test-predataset (preprocessing/prepare-dataset (data-words :test) bag-of-words)
        train-dataset (make-train-dataset bag-of-words train-predataset)
        test-dataset (make-test-dataset bag-of-words test-predataset)]

    (println bag-of-words)
    (train-and-evaluate-classifier train-dataset test-dataset)))


;(def classifier (make-classifier :bayes :naive [:supervised-discretization true]))
;(classifier-train classifier train-dataset)
;(def evaluation (classifier-evaluate classifier :dataset train-dataset test-dataset))
;(println evaluation)
;(println (dissoc evaluation :summary :confusion-matrix))

;(def train-dataset (->
;                     (make-dataset "train-dataset" bag-of-words train-predataset)
;                     (make-apply-filter :numeric-to-nominal {:attributes [:class]})
;                     (dataset-set-class :class)))


;(def train-dataset (make-dataset "train-dataset" bag-of-words train-predataset))
;(def train-dataset (make-apply-filter :numeric-to-nominal {:attributes [:class]} train-dataset))
;(def train-dataset (dataset-set-class train-dataset :class))
;
;
;(def test-dataset (make-dataset "train-dataset" bag-of-words test-predataset))
;(def test-dataset (make-apply-filter :numeric-to-nominal {:attributes [:class]} test-dataset))
;(def test-dataset (dataset-set-class test-dataset :class))