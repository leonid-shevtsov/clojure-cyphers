(ns cyphers.webapp
  (:use
    compojure.core
    ring.middleware.params
    ring.adapter.jetty)
  (:require [compojure.route :as route]
            [cyphers.webapp-views :as views]
            [clojure.data.codec.base64 :as b64]
            [cyphers.caesar :as caesar]
            [cyphers.des :as des]
            [cyphers.rsa :as rsa]))

(defroutes app-routes
  (GET "/" [] (views/index))

  ;; ROT13 demo
  (GET "/rot13" [] (views/rot13 {}))
  (POST "/rot13" [text]
        (views/rot13 {:text (caesar/rot13 text)}))

  ;; DES demo
  (GET "/des-encode" [] (views/des-encode {:text "DES test", :key "abcdefgh"}))

  (POST "/des-encode" [text key]
        (let [text-bytes (.getBytes text)
              key-bytes (.getBytes key)
              encoded (String. (b64/encode (des/des-encrypt key-bytes text-bytes)))]
          (views/result {:title "DES encoding result", :label "This is Base64 encoded", :result encoded})))

  (GET "/des-decode" [] (views/des-decode {:cypher "0R0XnVEsJ/k=", :key "abcdefgh"}))
  (POST "/des-decode" [cypher key]
        (let [cypher-bytes (b64/decode (.getBytes cypher))
              key-bytes (.getBytes key)
              decoded (String. (des/des-decrypt key-bytes cypher-bytes))]
          (views/result {:title "DES decoding result", :label "Open text", :result decoded})))

  ;; RSA demo
  (GET "/rsa-keygen" []
       (let [keys (rsa/random-keygen)]
         (views/rsa-keygen keys)))

  (GET "/rsa-encode" [] (views/rsa-encode {:public-key {:n 15853, :e 109}}))
  (POST "/rsa-encode" [n e message]
        (let [public-key {:n (Integer. n), :e (Integer. e)}
              cypher (rsa/encrypt (Integer. message) public-key)]
          (views/result {:title "RSA cypher", :label "Encrypted message", :result cypher})))

  (GET "/rsa-decode" [] (views/rsa-decode {:private-key {:n 15853, :d 11149}}))
  (POST "/rsa-decode" [n d cypher]
        (let [private-key {:n (Integer. n), :d (Integer. d)}
              message (rsa/decrypt (Integer. cypher) private-key)]
          (views/result {:title "RSA cypher", :label "Decrypted message", :result message})))

  (route/not-found "<h1>Page not found</h1>"))

(def app (wrap-params app-routes))

(defn -main []
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
    (run-jetty app {:port port})))
