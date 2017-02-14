(ns ves.schema-test
  (:require [clj-time.core :as t]
            [clojure.java.jdbc :as jdbc]
            [clojure.test :refer :all]
            [ves.db :as db]
            [ves.fixtures :as fix]
            [ves.schema :refer :all]))

(use-fixtures :each fix/wrap-data)

(defn- valid-row? [row name]
  (and (every? #(not (nil? (get row %))) [:id :created_at :updated_at])
       (= (:name row) name)
       (contains? row :deleted_at)
       (nil? (:deleted_at row))))

(deftest vertex-type-create!-test
  (let [name "VERTEX_TEST"
        _    (vertex-type-create! name)
        row  (first (jdbc/query
                     db/spec
                     ["SELECT * FROM vertex_types WHERE name = ?" name]))]
    (is (valid-row? row name))))

(deftest edge-type-create!-test
  (let [name "EDGE_TEST"
        _    (edge-type-create! name)
        row  (first (jdbc/query
                     db/spec
                     ["SELECT * FROM edge_types WHERE name = ?" name]))]
    (is (valid-row? row name))))
