(ns cyphers.caesar
  (:require [clojure.string :as s])
  )

(declare char-add-mod encode-char)

(defn encode
  "Encodes every latin character in the string"
  [key-amount string]
  (s/join (map (partial encode-char key-amount) string)))

(defn decode
  [key-amount string]
  (encode (- key-amount) string))

(def rot13 (partial encode 13))


(defn- char-add-mod
  [base-char the-char add-value]
  (let [
        base-char-code (int base-char)
        the-char-code (int the-char)
        rel-char-code (- the-char-code base-char-code)
        new-rel-char-code (mod (+ rel-char-code add-value) 26)]
    (char (+ base-char-code new-rel-char-code))))

(defn- encode-char
  [key-amount the-char]
  (cond (Character/isUpperCase the-char) (char-add-mod \A the-char key-amount)
        (Character/isLowerCase the-char) (char-add-mod \a the-char key-amount)
        :else                            the-char))

