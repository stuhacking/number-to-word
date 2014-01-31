(ns number-to-word.cmd
  (:require [number-to-word.core :refer [number->word]])
  (:gen-class :main true))

(defn -main
  "Run from command line.
Program will iterate the arguments converting each to its
text representation."
  [& args]
  (doseq [x args]
    (println (number->word x))))
