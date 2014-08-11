(ns cyphers.bitstring
  )

;; For this non-optimized demo a bitstring is represented just as vector of 0 or 1 integers
;; Yep, not very effective - but easy to understand and to debug

;; Because bitstrings are often reshuffled, they must be vectors, not lists

(defn partition [n bitstring]
  (map #(into [] %) (clojure.core/partition n bitstring))
  )

(defn join [bitstrings]
  (into [] (apply concat bitstrings))
  )

(defn xor [bitstring1 bitstring2]
  (into [] (map bit-xor bitstring1 bitstring2))
  )

(defn lrotate [amount bitstring]
  (into [] (apply concat (reverse (split-at amount bitstring))))
  )

(defn translate [function bitstring]
  (into [] (map bitstring function))
  )

(defn from-int [int-value width]
  (reverse (loop [
         value int-value
         result []
         ]
    (if (= width (count result))
      result
      (recur (bit-shift-right value 1) (cons (byte (bit-and value 1)) result))
      )
    ))
  )

(defn to-int [bitstring]
  (reduce #(bit-or (bit-shift-left %1 1) %2) (reverse bitstring))
  )

(defn from-byte-array [byte-array]
  (join (map #(from-int % 8) byte-array))
  )

(defn clamp-byte [int-val]
  ; Horrible
  (if (<= int-val 128)
    int-val
    (- int-val 256)
    )
  )


(defn to-byte-array [bitstring]
  ; Terribly ineffective. But oh well
  (into-array Byte/TYPE (map #(clamp-byte (to-int %)) (partition 8 bitstring)))
  )
