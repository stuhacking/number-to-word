(ns num2word.main
  (:require [num2word.core :refer [number->word]])
  (:gen-class))


(defn -main
  "Run from command line.
  Program will iterate the arguments converting each to its
  text representation."
  [& args]
  (doseq [x args]
    (println (number->word x))))
