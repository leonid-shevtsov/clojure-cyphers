(defproject cyphers "0.1.0-SNAPSHOT"
  :min-lein-version "2.0.0"
  :description "Demo Implementations of cyphers in Clojure"
  :url "http://leonid.shevtsov.me"
  :dependencies [
                 [org.clojure/clojure "1.6.0"]
                 [compojure "1.1.8"]
                 [hiccup "1.0.5"]
                 [org.clojure/data.codec "0.1.0"]
                 [ring/ring-jetty-adapter "1.2.1"]]
  :exclusions [org.mortbay.jetty/servlet-api]
  :plugins [[lein-ring "0.8.11"]]
  :ring {:handler cyphers.webapp/app}
  :main cyphers.webapp
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
