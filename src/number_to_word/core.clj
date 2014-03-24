(ns number-to-word.core)

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
        (map #(Integer/parseInt %)
             (rest (into [] 
                         (clojure.string/split (str s) #""))))))

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
     (loop [s (reverse digits)
            l ()]
       (if (empty? s)
         l
         (recur (drop n s) (conj l (pad-group (reverse (take n s)) n)))))))

;; Check if X is both non nil and > 0
(defn num? [x]
  (and x (< 0 x)))

(defn group->word [[fst snd thd] level]
  (let [words (atom [])]
    (when (num? fst)
      (swap! words conj (numbers fst) "hundred")
      (when (or (num? snd) (num? thd))
        (swap! words conj "and")))
    (if (and (num? snd) (= 1 snd))
      (swap! words conj (numbers (+ thd (* 10 snd))))
      (do 
        (when (num? snd)
          (swap! words conj (numbers (* 10 snd))))
        (when (num? thd)
          (swap! words conj (numbers thd)))))
    (when (num? (count @words))
      (swap! words conj (powers-of-ten level)))
    (clojure.string/join " " (filter #(not (= "" %)) @words))))

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
          (clojure.string/join ", " words))
        ;; Recur
        (let [word (group->word (first num-groups)
                    level)]
          (recur (rest num-groups)
                 (if (not (= "" word))
                   (conj words word)
                   words)
                 (dec level)))))))
