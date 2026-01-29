(ns num2word.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [num2word.core :refer :all]))

(deftest should-split-a-number-into-digits []
  (testing "digits"
    (is (= '(1 2 3 4 5) (digits "12345")))
    (is (= '(2 4 3 6 7 6) (digits "243676")))
    (is (= '(0 0 1) (digits "001")))))

(deftest should-find-next-multiple-of []
  (testing "next-multiple-of"
    (is (= 6 (next-multiple-of 3 4)))
    (is (= 6 (next-multiple-of 6 6)))
    (is (= 14 (next-multiple-of 7 8)))))

(deftest should-pad-a-group-of-digits []
  ;; Simple cases
  (testing "pad-group"
    (is (= '(1) (pad-group '(1) 1)))
    (is (= '(0 1) (pad-group '(1) 2)))
    (is (= '(0 0 1) (pad-group '(1) 3)))
    (is (= '(0 1 2) (pad-group '(1 2) 3)))
    (is (= '(0 0 1 3 3) (pad-group '(1 3 3) 5))))
  ;; Shouldn't pad groups if len(coll) >= pad amount
  (testing "pad-group"
    (is (= '(1 2 3) (pad-group '(1 2 3) 3)))
    (is (= '(1 2 3 4) (pad-group '(1 2 3 4) 3)))))

(deftest should-group-digits []
  (testing "group-digits"
    (is (= '((1)) (group-digits '(1) 1)))
    (is (= '((1) (2)) (group-digits '(1 2) 1)))
    (is (= '((1 2)) (group-digits '(1 2) 2)))
    (is (= '((0 0 1) (2 3 4)) (group-digits '(1 2 3 4) 3)))
    (is (= '((0 0 1) (2 3 4) (5 6 7)) (group-digits '(1 2 3 4 5 6 7) 3))))

  ;; Default should be 1
  (testing "group-digits"
    (is (= '((1) (2) (3)) (group-digits '(1 2 3))))))

(deftest should-auto-convert-input-type []
  (testing "number->word"
    (is (= "one" (number->word 1)))
    (is (= "one" (number->word "1")))
    (is (= "one" (number->word '1)))
    (is (= "one" (number->word \1)))))

(deftest should-convert-number-to-text []
  (testing "number->word"
    (is (= "zero" (number->word 0)))
    (is (= "one" (number->word 1)))
    (is (= "one hundred" (number->word 100)))
    (is (= "one hundred and four" (number->word 104)))
    (is (= "one hundred and twenty" (number->word 120)))
    (is (= "one thousand" (number->word 1000)))
    (is (= "one million, one hundred thousand, one" (number->word 1100001)))
    (is (= "one million, two hundred and thirty four thousand, five hundred and sixty seven"
           (number->word 1234567))))

  ;; With spurious leading zeros
  (testing "number->word"
    (is (= "one" (number->word 0001)))
    (is (= "one" (number->word "00000001")))
    (is (= "eleven" (number->word "000011")))))
