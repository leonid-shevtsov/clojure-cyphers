(ns cyphers.des
  (:use cyphers.des-data)
  (:require
    [cyphers.des-data :as data]
    [cyphers.bitstring :as bitstring])
  )

;;; References
;; http://csrc.nist.gov/publications/fips/fips46-3/fips46-3.pdf
;; https://en.wikipedia.org/wiki/Data_Encryption_Standard
;; https://en.wikipedia.org/wiki/DES_supplementary_material


(declare feistel KS des-round des-block des-process-bytes)

(defn des-encrypt
  "Encrypt a given byte[] string with a given key (also a byte[])"
  [key bytes]
  (des-process-bytes identity key bytes))

(defn des-decrypt
  "Decrypt a given byte[] string with a given key (also a byte[])"
  [key bytes]
  (des-process-bytes reverse key bytes))

(defn- des-process-bytes
  "DES algorithm that operates on a byte string"
  [key-schedule-mutator key bytes]
  {:pre [(= (count key) 8) (= (mod (count bytes) 8) 0)]}
  (let [
        key-bitstring (bitstring/from-byte-array key)
        key-schedule (key-schedule-mutator (KS key-bitstring))
        blocks (bitstring/partition-bitstring 64 (bitstring/from-byte-array bytes))
        encrypted-blocks (map (partial des-block key-schedule) blocks)
        ]
    (-> encrypted-blocks bitstring/join bitstring/to-byte-array)))

(defn- des-block
  "Entire DES algorithm operating on a single block, with a given key schedule
  Key schedule is an array of 16 48-bit keys"
  [key-schedule block]
  (let [
        input-halves (bitstring/partition-bitstring 32 (IP block))
        preoutput (reduce des-round input-halves key-schedule)
        ]
    (-> preoutput reverse bitstring/join FP)))

(defn- des-round
  "One round of DES transformations.
  Takes 2 32-bit half-blocks, and a 48-bit key"
  [[left-half right-half] K]
  (list right-half (bitstring/xor left-half (feistel right-half K))))

(defn- feistel
  "The feistel function. Takes a 32-bit half-block and a 48-bit key, and
  returns a 32-bit result"
  [half-block K]
  (P (bitstring/join (map (fn [s-box str] (s-box str))
                          Si
                          (bitstring/partition-bitstring 6 (bitstring/xor K (E half-block)))))))

(defn- KS
  "Generate a key schedule from a given key,
  using the forward (encryption) sequence of rotations"
  [key]
  (let [
        shift-halves (fn [halves amount]
                       (map (partial bitstring/lrotate amount) halves))
        input-key-halves (PC-1 key)
        shifted-halves-schedule (rest (reductions shift-halves
                                                  input-key-halves
                                                  data/encryption-key-rotations))
        ]
    (map (comp PC-2 bitstring/join) shifted-halves-schedule)))
