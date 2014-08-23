(ns cyphers.bitstring-test
  (:require [clojure.test :refer :all]
            [cyphers.bitstring :refer :all]))


(deftest test-partition
  (testing "splits bitstring evenly"
    (is (= '([0 1] [1 0]) (partition-bitstring 2 [0 1 1 0])))))

(deftest test-join
  (testing "joins multiple bitstrings"
    (is (= [0 1 1 0] (join '([0 1] [1 0]))))))

(deftest test-xor
  (testing "xors bitstrings"
    (is (= [0 1 1 0] (xor [0 0 1 1] [0 1 0 1])))))

(deftest test-lrotate
  (testing "rotates bitstrings"
    (is (= [0 1 1 0 1] (lrotate 2 [0 1 0 1 1])))))

(deftest test-translate
  (testing "translates bitstrings"
    (is (= [0 0 1 1] (translate [2 0 3 1] [0 1 0 1])))))

(deftest test-to-int
  (testing "converts bitstring into integer"
    (is (= 11 (to-int [1 0 1 1]))) )) ; big-endian!

(deftest test-from-int
  (testing "converts integer into bitstring"
    (is (= [0 1 0 1 1] (from-int 11 5)))) ; big-endian!
  (testing "negative integers (java char codes)"
    (is (= [1 1 1 1 1 1 1 0] (from-int -2 8)))))

(deftest test-from-byte-array
  (testing "converts byte array"
    (is (=  [1 1 0 1 0 0 0 0 1 0 1 0 1 0 0 0 1 1 0 1 0 0 0 0 1 0 1 1 1 0 0 0]
           (from-byte-array (.getBytes "Ши" "UTF-8")))))  ; too lazy for the rest of the string

  (testing "reference js string"
    (is (= [0 1 0 0 1 0 0 0  0 1 1 0 0 1 0 1  0 1 1 0 1 1 0 0  0 1 1 0 1 1 0 0  0 1 1 0 1 1 1 1  0 0 1 0 0 0 0 0  0 1 0 1 0 1 1 1  0 1 1 0 1 1 1 1]
           (from-byte-array (.getBytes "Hello Wo" "UTF-8"))))))

(deftest test-to-byte-array
  (testing "converts to byte array"
    (is (= "Ши"
           (String. (to-byte-array  [1 1 0 1 0 0 0 0 1 0 1 0 1 0 0 0 1 1 0 1 0 0 0 0 1 0 1 1 1 0 0 0]) "UTF-8")))))
