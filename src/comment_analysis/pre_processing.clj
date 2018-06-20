(ns comment-analysis.pre-processing
  (:require [clojure.string :as s]
            [clojure.set :as set])
  (use [stemmer.snowball :as snowball]))

(def stems (snowball/stemmer :portuguese))

(defn get-stemmin-words
  [comment]
  (map stems (s/split comment #"\s")))
  ;(s/split comment #"\s"))

(defn get-stop-words
  "https://gist.github.com/alopes/5358189"
  []
  '("de" "a" "o" "que" "e" "do" "da" "em" "um" "para" "é" "com" "não" "uma" "os" "no" "se" "na" "por" "mais" "as" "dos" "como" "mas" "ao" "ele" "das" "à" "seu" "sua" "ou" "ser" "quando" "muito" "nos" "já" "está" "eu" "também" "só" "pelo" "pela" "até" "isso" "ela" "entre" "depois" "sem" "mesmo" "aos" "ter" "seus" "quem" "nas" "me" "esse" "eles" "você" "essa" "num" "nem" "suas" "meu" "às" "minha" "têm" "numa" "pelos" "elas" "havia" "qual" "nós" "lhe" "deles" "essas" "esses" "pelas" "este" "dele" "tu" "te" "vocês" "vos" "lhes" "meus" "minhas" "teu" "tua" "teus" "tuas" "nosso" "nossa" "nossos" "nossas" "dela" "delas" "esta" "estes" "estas" "aquele" "aquela" "aqueles" "aquelas" "isto" "aquilo" "estou" "estamos" "estão" "estive" "esteve" "estivemos" "estiveram" "estava" "estávamos" "estavam" "estivera" "estivéramos" "esteja" "estejamos" "estejam" "estivesse" "estivéssemos" "estivessem" "estiver" "estivermos" "estiverem" "hei" "há" "havemos" "hão" "houve" "houvemos" "houveram" "houvera" "houvéramos" "haja" "hajamos" "hajam" "houvesse" "houvéssemos" "houvessem" "houver" "houvermos" "houverem" "houverei" "houverá" "houveremos" "houverão" "houveria" "houveríamos" "houveriam" "sou" "somos" "são" "era" "éramos" "eram" "fui" "foi" "fomos" "foram" "fora" "fôramos" "seja" "sejamos" "sejam" "fosse" "fôssemos" "fossem" "for" "formos" "forem" "serei" "será" "seremos" "serão" "seria" "seríamos" "seriam" "tenho" "tem" "temos" "tém" "tinha" "tínhamos" "tinham" "tive" "teve" "tivemos" "tiveram" "tivera" "tivéramos" "tenha" "tenhamos" "tenham" "tivesse" "tivéssemos" "tivessem" "tiver" "tivermos" "tiverem" "terei" "terá" "teremos" "terão" "teria" "teríamos" "teriam"))

(defn- get-regex-remove-stopwords
  []
  (str "\\b(" (s/join "|" (get-stop-words)) ")\\b"))

(defn remove-stop-words
  [comment]
  (.replaceAll comment (get-regex-remove-stopwords) ""))

(defn deaccent
  [str]
  "Remove accent from string"
  ;; http://www.matt-reid.co.uk/blog_post.php?id=69
  (let [normalized (java.text.Normalizer/normalize str java.text.Normalizer$Form/NFD)]
    (clojure.string/replace normalized #"\p{InCombiningDiacriticalMarks}+" "")))

(defn remove-punctuation-and-symbols
  [comment]
  (.replaceAll comment "[!|\\?|\\/|.|,|\\-|\\(|\\)\\[|\\]|\\>|\\<|\\*|\\`|\\#|\\d|\\%|\"|\\☆|\\”|\\“|\\'|\\:|\\&]" ""))

(defn remove-unnecessary-spaces
  [comment]
  (s/trim (s/replace comment #"\s+" " ")))

(defn remove-excess-characters
  [comment]
  (s/replace comment #"([a-z])\1+" "$1"))

(defn make-bag-of-words
  [list-keys, list-return]
  (let [un (set/union list-return (first list-keys))
        outlist (rest list-keys)]
    (if (empty? outlist)
      (set un)
      (recur outlist un))))

(defn prepare-dataset
  [data-words bag-of-words]
  (mapv (fn [data]
          (mapv #(get data %) bag-of-words)) data-words))


(defn preprocess
  [comment]
  (->
    comment
    s/lower-case
    deaccent
    remove-punctuation-and-symbols
    remove-excess-characters
    remove-stop-words
    remove-unnecessary-spaces))