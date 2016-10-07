(ns ves.db
  (:require clj-time.jdbc ; for side-effects
            [conf.core :as conf]
            [jdbc.pool.c3p0 :as c3p0]
            [yesql.core :refer [defqueries]]))

(def spec
  (c3p0/make-datasource-spec {:connection-uri (conf/get :database-url)}))

(defqueries "sql/ves.sql"
  {:connection spec})
