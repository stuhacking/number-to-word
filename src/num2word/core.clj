(ns num2word.core
  (:require [clojure.string :as s]))

(def ^:const primitive-numbers
  "Map of primitive numeric terms."
  {1 "one"
   2 "two"
   3 "three"
   4 "four"
   5 "five"
   6 "six"
   7 "seven"
   8 "eight"
   9 "nine"
   10 "ten"
   11 "eleven"
   12 "twelve"
   13 "thirteen"
   14 "fourteen"
   15 "fifteen"
   16 "sixteen"
   17 "seventeen"
   18 "eighteen"
   19 "nineteen"
   20 "twenty"
   30 "thirty"
   40 "forty"
   50 "fifty"
   60 "sixty"
   70 "seventy"
   80 "eighty"
   90 "ninety"})

(def ^:const orders-of-magnitude
  "List of orders of magnitude terms. The first entry is empty to avoid special
  case handling for the lowest order."
  ["", "thousand", "million", "billion", "trillion", "quadrillion",
   "quintillion", "sextillion", "septillion", "octillion", "nonillion",
   "decillion", "undecillion", "duodecillion", "tredecillion",
   "quattuordecillion", "quindecillion", "sexdecillion", "octodecillion",
   "novemdecillion", "vigintillion"])


(defn digits
  "Convert a numeric string `s` into a vector of digits."
  [s]
  (vec (map #(Character/getNumericValue %) (char-array (str s)))))


(defn next-multiple-of
  "Find the next occurring multiple of `factor` after `number`, iff number %
  factor =/= 0."
  [factor number]
  (let [r (mod number factor)]
    (if (zero? r)
      number
      (+ number (- factor r)))))


(defn pad-group
  "Add leading 0s to a digit-group `coll` until (count `coll`) = `n`."
  [coll n]
  (let [pad (- n (count coll))]
    (concat (repeat pad 0) coll)))


(defn group-digits
  "Given a list of `digits`, group them into subsequences of size `n`."
  ([digits] (group-digits digits 1))
  ([digits n]
   (->> (count digits)
        (next-multiple-of n)
        (pad-group digits)
        (partition n n [0]))))


(defn group->word
  "Convert a triplet of digits into a string of words."
  [[first second third]]
  (let [words (atom [])]
    (when (< 0 first)
      (swap! words conj (primitive-numbers first) "hundred")
      (when (or (< 0 second) (< 0 third))
        (swap! words conj "and")))
    (if (and (= 1 second))
      (swap! words conj (primitive-numbers (+ third (* 10 second))))
      (do
        (when (< 0 second)
          (swap! words conj (primitive-numbers (* 10 second))))
        (when (< 0 third)
          (swap! words conj (primitive-numbers third)))))
    (s/join " " (filter (complement s/blank?) @words))))


(defn number->word
  "Convert an arabic number into readable English text."
  [number]
  (loop [triplets (group-digits (digits number) 3)
         words []
         order (dec (count triplets))]
    (if (empty? triplets)
      ;; Done
      (if (empty? words)
        "zero"
        (s/trim (s/join ", " words)))
      ;; Recur
      (let [simple-number (group->word (first triplets))
            word (if (empty? simple-number) simple-number
                     (s/join " " [simple-number (orders-of-magnitude order)]))]
        (recur (rest triplets)
               (if (not (= "" word))
                 (conj words word)
                 words)
               (dec order))))))
