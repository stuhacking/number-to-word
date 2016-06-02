(ns number-to-word.core
  (:require [clojure.string :as s]))

(def ^:const numbers
  "Table of primitive numerals as words."
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

(def ^:const powers-of-ten
  "List of powers of ten, including the first empty power."
  ["", "thousand", "million", "billion", "trillion", "quadrillion",
   "quintillion", "sextillion", "septillion", "octillion", "nonillion",
   "decillion", "undecillion", "duodecillion", "tredecillion",
   "quattuordecillion", "quindecillion", "sexdecillion", "octodecillion",
   "novemdecillion", "vigintillion"])

(defn digits [s]
  "Read a string S into a list of digits."
  (into []
        (map #(Character/getNumericValue %) (char-array (str s)))))

(defn next-multiple-of [factor number]
  "Find the next occurring multiple of `factor' after `number' iff number % factor /= 0."
  (let [r (mod number factor)]
    (if (zero? r)
      number
      (+ number (- factor r)))))

(defn pad-group
  "Add leading 0s to a digit-group G until (count G) => N."
  [g n]
  (let [p (- n (count g))]
    (concat (repeat p 0) g)))

(defn group-digits
  "Given a list of digits DIGITS, group them into subsequences
of size N."
  ([digits] (group-digits digits 1))
  ([digits n]
     (partition n n [0] (pad-group digits (next-multiple-of n (count digits))))))

(defn group->word [[fst snd thd]]
  (let [words (atom [])]
    (when (< 0 fst)
      (swap! words conj (numbers fst) "hundred")
      (when (or (< 0 snd) (< 0 thd))
        (swap! words conj "and")))
    (if (and (= 1 snd))
      (swap! words conj (numbers (+ thd (* 10 snd))))
      (do
        (when (< 0 snd)
          (swap! words conj (numbers (* 10 snd))))
        (when (< 0 thd)
          (swap! words conj (numbers thd)))))
    (s/join " " (filter (complement s/blank?) @words))))

(defn number->word
  "Convert an arabic number into readable English text."
  [number]
  (let [group-size 3]
    (loop [num-groups (group-digits (digits number) group-size)
           words []
           level (dec (count num-groups))]
      (if (empty? num-groups)
        ;; Done
        (if (empty? words)
          "zero"
          (s/trim (s/join ", " words)))
        ;; Recur
        (let [simple-number (group->word (first num-groups))
              word (if (empty? simple-number) simple-number
                       (s/join " " [simple-number (powers-of-ten level)]))]
          (recur (rest num-groups)
                 (if (not (= "" word))
                   (conj words word)
                   words)
                 (dec level)))))))
