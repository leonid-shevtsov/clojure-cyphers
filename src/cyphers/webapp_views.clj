(ns cyphers.webapp-views
  (:use hiccup.core hiccup.element hiccup.form)
  )

(defn layout [& body]
  (html [
         :html
         [:head
          [:link {:rel "stylesheet" :href "//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css"}]
          [:script {:src "//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"}]
          ]
         [:body [:div.container
                 [:nav.navbar.navbar-default
                  [:a.navbar-brand {:href "/"} "Index"]
                  [:ul.nav.navbar-nav
                   [:li (link-to "/rot13" "ROT13")]
                   [:li (link-to "/des-encode" "DES encode")]
                   [:li (link-to "/des-decode" "DES decode")]
                   [:li (link-to "/rsa-keygen" "RSA keygen")]
                   [:li (link-to "/rsa-encode" "RSA encode")]
                   [:li (link-to "/rsa-decode" "RSA decode")]]
                  ]
                 body]]]))

(defn index []
  (layout [:h1 "Cyphers demo by Leonid Shevtsov"]))

(defn form-field [field-label field-name field-value]
                   [:div.form-group
                    (label field-name field-label)
                    (text-field {:class "form-control"} field-name (h field-value))])

(defn rot13 [data]
  (layout [:h1 "ROT13 demo"]
          (form-to {:class "form"} [:post "/rot13"]
                   (form-field "Text" "text" (:text data))
                   [:button.btn.btn-default {:type "submit"} "Encode/decode"])))

(defn des-encode [data]
  (layout [:h1 "DES encode demo"]
          (form-to {:class "form"} [:post "/des-encode"]
                   (form-field "Text to encode (multiple of 8 chars)" "text" (:text data))
                   (form-field "Key (8 characters)" "key" (:key data))
                   [:button.btn.btn-default {:type "submit"} "Encode"])))

(defn des-decode [data]
  (layout [:h1 "DES decode demo"]
          (form-to {:class "form"} [:post "/des-decode"]
                   (form-field "Cypher to decode (Base64 encoded)" "cypher" (:cypher data))
                   (form-field "Key (8 characters)" "key" (:key data))
                   [:button.btn.btn-default {:type "submit"} "Decode"])))

(defn result [data]
  (layout [:h1 (:title data)]
          [:p (:label data) ": " [:strong (h (:result data))]]
          )
  )

(defn rsa-keygen [keys]
  (layout [:h1 "RSA keygen"]
          [:p "Public key: n = "
           [:strong (h (-> keys :public-key :n))]
           ", e = "
           [:strong (h (-> keys :public-key :e))]]
          [:p "Private key: n = "
           [:strong (h (-> keys :private-key :n))]
           ", d = "
           [:strong (h (-> keys :private-key :d))]]))

(defn rsa-encode [data]
  (layout [:h1 "RSA encode demo"]
          (form-to {:class "form"} [:post "/rsa-encode"]
                   (form-field "Public key N = " "n" (-> data :public-key :n))
                   (form-field "Public key E = " "e" (-> data :public-key :e))
                   (form-field "Message to encode (number from 0 to N-1)" "message" (:message data))
                   [:button.btn.btn-default {:type "submit"} "Encode"])))

(defn rsa-decode [data]
  (layout [:h1 "RSA decode demo"]
          (form-to {:class "form"} [:post "/rsa-decode"]
                   (form-field "Private key N = " "n" (-> data :private-key :n))
                   (form-field "Private key D = " "d" (-> data :private-key :d))
                   (form-field "Encrypted message" "cypher" (:cypher data))
                   [:button.btn.btn-default {:type "submit"} "Decode"])))
