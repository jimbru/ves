(ns ves.db
  (:require [cheshire.core :as json]
            clj-time.jdbc ; for side-effects
            [clojure.java.jdbc :as jdbc]
            [conf.core :as conf]
            [jdbc.pool.c3p0 :as c3p0]
            [yesql.core :refer [defqueries]])
  (:import clojure.lang.IPersistentMap
           org.postgresql.util.PGobject))

(def spec
  (c3p0/make-datasource-spec
   {:connection-uri    (or (conf/get :ves-database-url) (conf/get :database-url))
    :initial-pool-size (or (conf/get :ves-initial-pool-size) 3)}))

(defqueries "sql/ves.sql"
  {:connection spec})

(extend-protocol jdbc/ISQLValue
  IPersistentMap
  (sql-value [value]
    (doto (PGobject.)
      (.setType "jsonb")
      (.setValue (json/generate-string value)))))

(extend-protocol jdbc/IResultSetReadColumn
  PGobject
  (result-set-read-column [pgobj metadata idx]
    (let [type (.getType pgobj)
          value (.getValue pgobj)]
      (case type
        "jsonb" (json/parse-string value true)
        :else value))))
