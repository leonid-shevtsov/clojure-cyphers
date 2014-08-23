(ns cyphers.bitstring)

;; For this non-optimized demo a bitstring is represented just as vector of 0 or 1 integers
;; Yep, not very effective - but easy to understand and to debug

;; Because bitstrings are often reshuffled, they must be vectors, not lists

(defn partition-bitstring [n bitstring]
  {:pre [(= 0 (mod (count bitstring) n))]}
  (map vec (partition n bitstring)))

(defn join [bitstrings]
  (vec (apply concat bitstrings)))

(defn xor [bitstring1 bitstring2]
  (vec (map bit-xor bitstring1 bitstring2)))

(defn lrotate [amount bitstring]
  (vec (apply concat (reverse (split-at amount bitstring)))))

(defn translate [function bitstring]
  (vec (map #(nth bitstring %) function)))

(defn from-int [int-value width]
  (loop [
         value int-value
         result []]
    (if (= width (count result))
      result
      (recur (bit-shift-right value 1) (cons (byte (bit-and value 1)) result)))))

(defn to-int [bitstring]
  (reduce #(bit-or (bit-shift-left %1 1) %2) bitstring))

(defn from-byte-array [byte-array]
  (join (map #(from-int % 8) byte-array)))

(defn clamp-byte [int-val]
  ; OPTIMIZE Horrible performance
  (if (<= int-val 128)
    int-val
    (- int-val 256)))


(defn to-byte-array [bitstring]
  ; OPTIMIZE Terribly ineffective. But oh well
  (into-array Byte/TYPE (map #(clamp-byte (to-int %)) (partition-bitstring 8 bitstring))))
