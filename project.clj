(defproject comment-analysis "0.1.0-SNAPSHOT"
  :description "Project presented as work on AI discipline of Computer Science Course"
  :url "jciel.github.io"
  :license {:name "MIT"
            :url ""}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [snowball-stemmer "0.1.0"]
                 [cc.artifice/clj-ml "0.8.7"]]
  :main comment-analysis.core/main)
