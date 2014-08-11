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
  {:pre [(= 32 (count half-block)) (= 48 (count K))]
   :post [(= 32 (count %))]
   }
  (P (bitstring/join (map (fn [s-box str] (s-box str)) Si (bitstring/partition 6 (bitstring/xor K (E half-block))))))
  )


(defn des-round
  [[left-half right-half] K]
  {:pre [(= 32 (count left-half)) (= 32 (count left-half)) (= 48 (count K))]
   :post [(= 32 (count (first %))) (= 32 (count (second %)))]
   }
  (list right-half (bitstring/xor left-half (feistel right-half K)))
  )

(defn KS [key]
  (let [
        input-key-halves (list ((:left PC-1) key) ((:right PC-1) key) )
        shift-halves (fn [halves amount] (map (partial bitstring/lrotate amount) halves))
        shifted-halves-seq (rest (reductions shift-halves input-key-halves data/left-shifts-per-round))
        ]
    (map #(PC-2 (bitstring/join %)) shifted-halves-seq)
    )
  )

(defn des-block
  [key block]
  (let [
        Ki (KS key)
        input-halves (bitstring/partition 32 (IP block))
        preoutput (reduce #(des-round %1 %2) input-halves Ki)
        ]
    (-> preoutput bitstring/join FP)
    )
  )

(defn des-encrypt
  [key bytes]
  (let [key-bitstring (bitstring/from-byte-array key)
        blocks (partition 64 (bitstring/from-byte-array bytes))]
    (bitstring/to-byte-array (bitstring/join (map (partial des-block key-bitstring) blocks)))
    )
  )
