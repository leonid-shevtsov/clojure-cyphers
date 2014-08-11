(ns cyphers.des
  (:use cyphers.des-data)
  (:require
    [cyphers.des-data :as data]
    [cyphers.bitstring :as bitstring])
  )

;; Reference
;; http://csrc.nist.gov/publications/fips/fips46-3/fips46-3.pdf
;; https://en.wikipedia.org/wiki/Data_Encryption_Standard
;; https://en.wikipedia.org/wiki/DES_supplementary_material

(defn feistel
  [half-block K]
  (P (bitstring/join (map #(apply %1 %2) Si (bitstring/partition 6 (bitstring/xor K (E half-block))))))
  )


(defn des-round
  [[left-half right-half] K]
  '(right-half (bitstring/xor left-half (feistel right-half key-function)))
  )

(defn KS [key]
  (let [
        input-key-halves '( (apply (:left PC-1) key) (apply (:right PC-1) key) )
        shift-halves (fn [halves amount] (map (partial bitstring/lrotate amount) halves))
        shifted-halves-seq (rest (reductions shift-halves input-key-halves data/left-shifts-per-round))
        ]
    (map #(PC-2 (bitstring/join %)) shifted-halves-seq)
    )
  )

(defn des-block
  [key block]
  (let [
        Ki (map (partial KS key) (range 0 16))
        input-halves (bitstring/partition 32 (IP block))
        preoutput (reduce #(des-round %1 %2) input-halves Ki)
        ]
    (-> preoutput bitstring/join FP)
    )
  )

(defn des
  [key bytes]
  (let [key-bitstring (bitstring/from-byte-array key)
        blocks (partition 64 (bitstring/from-byte-array bytes))]
    (bitstring/to-byte-array (bitstring/join (map (partial des-block key-bistring) blocks)))
    )
  )
