(defproject cyphers "0.1.0-SNAPSHOT"
  :description "Demo Implementations of cyphers in Clojure"
  :url "http://leonid.shevtsov.me"
  :dependencies [
                 [org.clojure/clojure "1.6.0"]
                 [compojure "1.1.8"]
                 [hiccup "1.0.5"]
                 [org.clojure/data.codec "0.1.0"]]
  :plugins [[lein-ring "0.8.11"]]
  :ring {:handler cyphers.webapp/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
