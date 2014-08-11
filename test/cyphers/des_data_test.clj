(ns cyphers.des-data-test
  (:require [clojure.test :refer :all]
            [cyphers.des-data :refer :all]))

(deftest test-Si
  (testing "returns 15 for S1 element 3 (00011)"
    (is (= [1 1 1 1] ((nth Si 0) [1 1 0 0 0])))
    )
  )
