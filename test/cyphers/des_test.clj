(ns cyphers.des-test
  (:require [clojure.test :refer :all]
            [cyphers.des :refer :all]))


(def key
  [0x3b 0x38 0x98 0x37 0x15 0x20 0xf7 0x5e])

(def msg
  (.getBytes "Hello Wo"))

(def encrypted
  [0xf1 0x8e 0x33 0xd4 0x53 0x67 0x91 0xae])

(deftest test-des
  (is (= encrypted (into [] (des-encrypt key msg))))
  )
