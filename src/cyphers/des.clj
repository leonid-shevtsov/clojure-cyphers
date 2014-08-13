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
  "The feistel function. Takes a 32-bit half-block and a 48-bit key, and
  returns a 32-bit result"
  [half-block K]
  (P (bitstring/join (map (fn [s-box str] (s-box str))
                          Si
                          (bitstring/partition 6 (bitstring/xor K (E half-block)))))))


(defn des-round
  "One round of DES transformations.
  Takes 2 32-bit half-blocks, and a 48-bit key"
  [[left-half right-half] K]
  (list right-half (bitstring/xor left-half (feistel right-half K))))

(defn KS
  "Generate a key schedule from a given key,
  using the forward (encryption) sequence of rotations"
  [key]
  (let [
        shift-halves (fn [halves amount] (map (partial bitstring/lrotate amount) halves))
        input-key-halves (PC-1 key)
        shifted-halves-schedule (rest (reductions shift-halves input-key-halves data/encryption-key-rotations))
        ]
    (map (comp PC-2 bitstring/join) shifted-halves-schedule)))

(defn des-block
  [key-schedule block]
  (let [
        input-halves (bitstring/partition 32 (IP block))
        preoutput (reduce des-round input-halves key-schedule)
        ]
    (-> preoutput reverse bitstring/join FP)))

(defn des-process-bytes
  [key-schedule-mutator key bytes]
  (let [
        key-bitstring (bitstring/from-byte-array key)
        key-schedule (key-schedule-mutator (KS key-bitstring))
        blocks (partition 64 (bitstring/from-byte-array bytes))
        ]
    (bitstring/to-byte-array (bitstring/join (map (partial des-block key-schedule) blocks)))))

(def des-encrypt (partial des-process-bytes identity))

(def des-decrypt (partial des-process-bytes reverse))
