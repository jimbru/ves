(ns ves.db-test
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.test :refer :all]
            [ves.db :as db]
            [ves.fixtures :as fix]))

(use-fixtures :once fix/wrap-data)

(deftest spec-test
  (is (= 2 (->> (jdbc/query db/spec ["SELECT 1 + 1"]) first :?column?))))

(deftest queries-test
  (is (= [{:id 101
           :name "FOO"
           :created_at fix/now
           :updated_at fix/now
           :deleted_at nil}
          {:id 104
           :name "BAR"
           :created_at fix/now
           :updated_at fix/now
           :deleted_at nil}]
         (db/select-all-vertex-types))))
