(ns ves.core
  (:require [clj-time.core :as t]
            [clojure.java.jdbc :refer [with-db-transaction]]
            [ves.db :as db]))

(defn vertex-get [id]
  (first (db/vertex-get {:id id})))

(defn vertex-create! [type data]
  (db/vertex-create<! {:type type :data data :now (t/now)}))

(defn vertex-update! [id data]
  (with-db-transaction [tcon db/spec {:isolation :serializable}]
    (when-let [row (vertex-get id)]
      (when (= 1 (db/vertex-update! {:id id
                                     :data (merge (:data row) data)
                                     :now (t/now)}))
        (vertex-get id)))))

(defn vertex-delete! [id]
  (when-not (= 1 (db/vertex-delete! {:id id :now (t/now)}))
    (throw (Exception. "vertex-delete! failed"))))
