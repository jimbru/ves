(ns ves.schema
  "Functions for interacting with the storage schema."
  (:require [clj-time.core :as t]
            [ves.db :as db]))

(defn vertex-type-create! [typename]
  (db/schema-vertex-type-create<! {:name typename :now (t/now)}))

(defn edge-type-create! [typename]
  (db/schema-edge-type-create<! {:name typename :now (t/now)}))
