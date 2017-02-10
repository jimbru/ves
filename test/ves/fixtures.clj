(ns ves.fixtures
  (:require [clj-time.core :as t]
            [clojure.java.jdbc :as jdbc]
            [ves.db :as db]))

(def now (t/date-time 2016 10 6 22 54))

(def foo 101)
(def bar 104)

(def looks-like 103)
(def smells-like 105)

(def vertex-types-data
  [[:id :name :created_at :updated_at :deleted_at]
   [foo "FOO" now now nil]
   [102 "XXX" now now now]
   [bar "BAR" now now nil]])

(def edge-types-data
  [[:id :name :created_at :updated_at :deleted_at]
   [looks-like "LOOKS_LIKE" now now nil]
   [smells-like "SMELLS_LIKE" now now nil]
   [106 "XXX" now now now]])

(def vertices-data
  [[:id :type :data :created_at :updated_at :deleted_at]
   [110 foo {:a 1 :b "lemon"} now now nil]
   [111 foo {:a 2 :b "lime"} now now nil]
   [112 foo {:a 1 :b "orange"} now now now]
   [113 bar {:x "baz" :y "quux"} now now nil]
   [114 foo {:a 3 :b "grapefruit"} now now nil]])

(def edges-data
  [[:id1 :type :id2 :data :created_at :updated_at :deleted_at]
   [110 looks-like 111 {:a 10 :b 11} now now nil]
   [110 looks-like 112 {:a 12 :b 13} now now nil]
   [110 looks-like 113 {:a 14 :b 15} now now now]
   [111 looks-like 112 {:a 16 :b 17} now now nil]
   [110 smells-like 111 {:a 18 :b 19} now now nil]
   [111 smells-like 110 {:a 20 :b 21} now now nil]
   [110 looks-like 114 {:a 22 :b 23} now now nil]])

(defn wrap-data [f]
  (let [table-data (array-map :vertex_types vertex-types-data
                              :edge_types   edge-types-data
                              :vertices     vertices-data
                              :edges        edges-data)]
    (doseq [t (reverse (keys table-data))]
      (jdbc/delete! db/spec t []))
    (doseq [[t data] table-data]
      (jdbc/insert-multi! db/spec t (first data) (rest data))))
  (f))
