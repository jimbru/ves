(defproject ves "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/java.jdbc "0.6.1"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.postgresql/postgresql "9.4.1210"]]
  :main ves.main
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
