(ns cyphers.des-test
  (:require [clojure.test :refer :all]
            [cyphers.des :refer :all]
            [cyphers.bitstring :as bitstring]
            ))


(def des-key
  [0x3b 0x38 0x98 0x37 0x15 0x20 0xf7 0x5e])

(def msg
  (into [] (.getBytes "Hello Wo")))

(def encrypted
  (map bitstring/clamp-byte [0xf1 0x8e 0x33 0xd4 0x53 0x67 0x91 0xae]))

(deftest test-des-encrypt
  (is (= encrypted (into [] (des-encrypt des-key msg)))))

(deftest test-des-decrypt
  (is (= msg (into [] (des-decrypt des-key encrypted)))))
