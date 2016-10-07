(ns ves.db-test
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.test :refer :all]
            [ves.db :as db]
            [ves.fixtures :as fixtures]))

(use-fixtures :once fixtures/wrap-data)

(deftest spec-test
  (is (= 2 (->> (jdbc/query db/spec ["SELECT 1 + 1"]) first :?column?))))

(deftest queries-test
  (is (= [{:id 101
           :name "FOO"
           :created_at fixtures/now
           :updated_at fixtures/now
           :deleted_at nil}
          {:id 104
           :name "BAR"
           :created_at fixtures/now
           :updated_at fixtures/now
           :deleted_at nil}]
         (db/select-all-vertex-types))))
