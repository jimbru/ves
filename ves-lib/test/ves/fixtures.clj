(ns ves.fixtures
  (:require [clj-time.core :as t]
            [clojure.java.jdbc :as jdbc]
            [ves.db :as db]))

(def now (t/date-time 2016 10 6 22 54))

(def foo 101)
(def bar 104)

(def vertex-types-data
  [[:id :name :created_at :updated_at :deleted_at]
   [foo "FOO" now now nil]
   [102 "XXX" now now now]
   [bar "BAR" now now nil]])

(def edge-types-data
  [[:id :name :created_at :updated_at :deleted_at]
   [103 "RED" now now nil]
   [105 "BLUE" now now nil]
   [106 "XXX" now now now]])

(def vertices-data
  [[:id :type :data :created_at :updated_at :deleted_at]
   [110 foo {:a 1 :b "lemon"} now now nil]
   [111 foo {:a 2 :b "lime"} now now nil]
   [112 foo {:a 1 :b "orange"} now now now]
   [113 bar {:x "baz" :y "quux"} now now nil]])

(defn wrap-data [f]
  (let [table-data (array-map :vertex_types vertex-types-data
                              :edge_types   edge-types-data
                              :vertices     vertices-data)]
    (doseq [t (reverse (keys table-data))]
      (jdbc/delete! db/spec t []))
    (doseq [[t data] table-data]
      (jdbc/insert-multi! db/spec t (first data) (rest data))))
  (f))
