(ns cyphers.rsa
  (:require [cyphers.prime :as prime])
  )

; Reference
; https://en.wikipedia.org/wiki/RSA_cipher#Key_generation
; https://en.wikipedia.org/wiki/Modular_multiplicative_inverse
; https://en.wikipedia.org/wiki/Extended_Euclidean_algorithm

(def primes (prime/euler-sieve 100000))

(def max-key-prime 200)

(defn extended-euclidean
  "Extended euclidean algorithm.
  Find gcd, x, y, such that GCD(a,b) = gcd = ax + by"
  [a b]
  (loop [r0 a
         r1 b
         q 0
         s0 1
         s1 0
         t0 0
         t1 1]
    (if (zero? r1)
      {:gcd r0, :x s0, :y t0}
      (let [q (unchecked-divide-int r0 r1)]
        (recur r1
               (mod r0 r1)
               q
               s1
               (- s0 (* q s1))
               t1
               (- t0 (* q t1)))))))

(defn modular-multiplicative-inverse
  "Find d such that (d*e) mod phi-n = 1"
  [e phi-n]
  (let [mmi (:x (extended-euclidean e phi-n))]
    (if (< mmi 0)
      (+ mmi phi-n)
      mmi)))


(defn expmod
  "Find (base^exponent) mod modulus"
  [base exponent modulus]
  (reduce (fn [val _] (mod (* val base) modulus))
          1
          (range 0 exponent)))

(defn random-keygen [] ; not a pure function
  (let [suitable-primes (take-while #(> max-key-prime %) primes)
        p (rand-nth suitable-primes)
        q (rand-nth (remove #{p} suitable-primes))
        n (* p q)
        phi-n (* (dec p) (dec q))
        ; Note: 17 < e < 200 - should be enough
        e-candidates (filter #(< 17 % n) suitable-primes)
        e (first (filter #(not= 0 (mod phi-n %)) (shuffle e-candidates)))
        d (modular-multiplicative-inverse e phi-n)]
    {:public-key {:n n, :e e}
     :private-key {:n n, :d d}}))

(defn encrypt [message-number public-key]
  {:pre [(<= 0 message-number) (< message-number (:n public-key))]}
  (expmod message-number (:e public-key) (:n public-key)))

(defn decrypt [encoded-number private-key]
  (expmod encoded-number (:d private-key) (:n private-key)))
