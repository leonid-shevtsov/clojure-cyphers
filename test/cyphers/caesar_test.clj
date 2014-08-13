(ns cyphers.caesar-test
  (:require [clojure.test :refer :all]
            [cyphers.caesar :refer :all]))

(deftest test-char-add-mod
  (testing "With positive values"
    (is (= \K (#'cyphers.caesar/char-add-mod \A \I 2))))
  (testing "With negative values"
    (is (= \i (#'cyphers.caesar/char-add-mod \a \k -2)))))

(deftest test-encode-char
  (testing "For uppercase letters"
    (is (= \K (#'cyphers.caesar/encode-char 2 \I))))

  (testing "For lowercase letters"
    (is (= \k (#'cyphers.caesar/encode-char 2 \i))))

  (testing "For non-letters"
    (is (= \1 (#'cyphers.caesar/encode-char 2 \1)))))

(deftest test-encode-string
  (is (= "XYZABCDEFGHIJKLMNOPQRSTUVW" (encode -3 "ABCDEFGHIJKLMNOPQRSTUVWXYZ"))))

(deftest test-decode-string
  (is (= "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG" (decode -3 "QEB NRFZH YOLTK CLU GRJMP LSBO QEB IXWV ALD"))))

(deftest test-rot13
  (testing "ROT13"
    (is (= "To get to the other side!" (rot13 "Gb trg gb gur bgure fvqr!")))))
