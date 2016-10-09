(defproject ves "0.1.0-SNAPSHOT"
  :description "ves library"
  :url "https://gitlab.com/jimbru/ves"
  :dependencies [[cheshire "5.6.3"]
                 [clj-time "0.12.0"]
                 [clojure.jdbc/clojure.jdbc-c3p0 "0.3.2"]
                 [conf "0.9.1"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/java.jdbc "0.6.1"]
                 [org.postgresql/postgresql "9.4.1210"]
                 [yesql "0.5.3"]]
  :profiles {:test {:jvm-opts ["-Dconf.env=test"]}})
