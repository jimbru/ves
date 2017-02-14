(ns ves.core
  "Functions for interacting with stored data."
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

(defn edge-get [id1 type id2]
  (first (db/edge-get {:id1 id1 :type type :id2 id2})))

(defn edge-count [id1 type]
  (-> (db/edge-count {:id1 id1 :type type}) first :count))

(defn edge-range
  ([id1 type limit]
   (db/edge-range {:id1 id1 :type type :limit limit}))
  ([id1 type limit id2]
   (db/edge-range-offset {:id1 id1 :type type :id2 id2 :limit limit})))

(defn edge-create! [id1 type id2 data]
  (db/edge-create<! {:id1 id1 :type type :id2 id2 :data data :now (t/now)}))

(defn edge-update! [id1 type id2 data]
  (with-db-transaction [tcon db/spec {:isolation :serializable}]
    (when-let [row (edge-get id1 type id2)]
      (when (= 1 (db/edge-update! {:id1 id1
                                   :type type
                                   :id2 id2
                                   :data (merge (:data row) data)
                                   :now (t/now)}))
        (edge-get id1 type id2)))))

(defn edge-delete! [id1 type id2]
  (when-not (= 1 (db/edge-delete! {:id1 id1 :type type :id2 id2 :now (t/now)}))
    (throw (Exception. "edge-delete! failed"))))
