(ns cyphers.rsa-test
  (:require [clojure.test :refer :all]
            [cyphers.rsa :refer :all]))

(deftest text-extended-euclidean
  (testing "Example from Wikipedia"
    (is (= {:gcd 2 :x -9 :y 47} (extended-euclidean 240 46)))))

(deftest text-modular-multiplicative-inverse
  (testing "Corrects negative values of Bezout coefficient"
    (is (= 14 (modular-multiplicative-inverse 120 23)))))


(deftest test-expmod
  (testing "Example from Wikipedia"
    (is (= 445 (expmod 4 13 497)))))

(deftest test-encrypt
  (testing "Example from Wikipedia"
    (is (= 2790 (encrypt 65 {:n 3233 :e 17})))))

(deftest test-decrypt
  (testing "Example from Wikipedia"
    (is (= 65 (decrypt 2790 {:n 3233 :d 2753})))))
