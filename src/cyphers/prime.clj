(ns cyphers.prime)

; Reference
; http://en.wikipedia.org/wiki/Sieve_of_Eratosthenes

(defn euler-sieve [top-limit]
  (loop [results '(2)
         sieve (range 3 top-limit 2)]
    (let [prime (first sieve)
          composites (into '#{}
                           (take-while #(>= top-limit %)
                                       (map #(* prime %) sieve)))]
      (if-not (seq composites)
        (concat (reverse results) sieve)
        (recur (cons prime results) (remove composites (rest sieve)))))))
