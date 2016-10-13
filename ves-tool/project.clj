(defproject ves/tool "0.1.0"
  :description "ves tool"
  :url "https://github.com/jimbru/ves"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/java.jdbc "0.6.1"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.postgresql/postgresql "9.4.1211"]]
  :main ves.tool.main
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
