;;; Num 2 Word main entrypoint
;;
;; SPDX-FileCopyrightText: 2014 Stu Hacking <stuhacking@gmail.com>
;; SPDX-License-Identifier: MIT

;;; Code:
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
