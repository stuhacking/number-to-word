(ns number-to-word.core-test
  (:use [clojure.test]
        [number-to-word.core]))

(deftest should-split-a-number-into-digits []
  (is (= '(1 2 3 4 5) (digits "12345")))
  (is (= '(2 4 3 6 7 6) (digits "243676")))
  (is (= '(0 0 1) (digits "001"))))

(deftest should-pad-a-group-of-digits []
  ;; Simple cases
  (is (= '(1) (pad-group '(1) 1)))
  (is (= '(0 1) (pad-group '(1) 2)))
  (is (= '(0 0 1) (pad-group '(1) 3)))
  (is (= '(0 1 2) (pad-group '(1 2) 3)))
  (is (= '(0 0 1 3 3) (pad-group '(1 3 3) 5)))
  ;; Shouldn't pad groups if N >= pad amount
  (is (= '(1 2 3) (pad-group '(1 2 3) 3)))
  (is (= '(1 2 3 4) (pad-group '(1 2 3 4) 3))))

(deftest should-group-digits []
  (is (= '((1)) (group-digits '(1) 1)))
  (is (= '((1) (2)) (group-digits '(1 2) 1)))
  (is (= '((1 2)) (group-digits '(1 2) 2)))
  (is (= '((0 0 1) (2 3 4)) (group-digits '(1 2 3 4) 3)))
  (is (= '((0 0 1) (2 3 4) (5 6 7)) (group-digits '(1 2 3 4 5 6 7) 3)))

  ;; Default should be 1
  (is (= '((1) (2) (3)) (group-digits '(1 2 3)))))

(deftest should-auto-convert-input-type []
  (is (= "one" (number->word 1)))
  (is (= "one" (number->word "1")))
  (is (= "one" (number->word '1)))
  (is (= "one" (number->word \1))))

(deftest should-convert-number-to-text []
  (is (= "zero" (number->word 0)))
  (is (= "one" (number->word 1)))
  (is (= "one hundred" (number->word 100)))
  (is (= "one hundred and four" (number->word 104)))
  (is (= "one hundred and twenty" (number->word 120)))
  (is (= "one thousand" (number->word 1000)))
  (is (= "one million, one hundred thousand, one" (number->word 1100001)))
  (is (= "one million, two hundred and thirty four thousand, five hundred and sixty seven"
         (number->word 1234567)))
  
  ;; With spurious leading zeros
  (is (= "one" (number->word 0001)))
  (is (= "one" (number->word "00000001")))
  (is (= "eleven" (number->word "000011"))))

