(ns ves.fixtures
  (:require [clj-time.core :as t]
            [clojure.java.jdbc :as jdbc]
            [ves.db :as db]))

(def now (t/date-time 2016 10 6 22 54))

(def vertex-types-data
  [[:id :name :created_at :updated_at :deleted_at]
   [101 "FOO" now now nil]
   [102 "XXX" now now now]
   [104 "BAR" now now nil]])

(def edge-types-data
  [[:id :name :created_at :updated_at :deleted_at]
   [103 "RED" now now nil]
   [105 "BLUE" now now nil]
   [106 "XXX" now now now]])

(defn reset-table! [table data]
  (jdbc/delete! db/spec table [])
  (jdbc/insert-multi! db/spec table (first data) (rest data)))

(defn wrap-data [f]
  (jdbc/delete! db/spec :edges [])
  (jdbc/delete! db/spec :vertices [])
  (reset-table! :vertex_types vertex-types-data)
  (reset-table! :edge_types edge-types-data)
  (f))
