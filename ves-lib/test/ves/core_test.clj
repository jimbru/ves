(ns ves.core-test
  (:require [clj-time.core :as t]
            [clojure.test :refer :all]
            [ves.core :refer :all]
            [ves.fixtures :as fix])
  (:import org.postgresql.util.PSQLException))

(use-fixtures :once fix/wrap-data)

(deftest vertex-get-test
  (testing "ok"
    (is (= {:id 110
            :type fix/foo
            :data {:a 1 :b "lemon"}
            :created_at fix/now
            :updated_at fix/now
            :deleted_at nil}
           (vertex-get 110))))
  (testing "not found"
    (is (nil? (vertex-get 999)))))

(deftest vertex-create!-test
  (testing "ok"
    (let [data {:a 3 :b "grapefruit"}
          now (t/now)]
      (with-redefs [t/now (constantly now)]
        (let [result (vertex-create! fix/foo data)]
          (is (integer? (:id result)))
          (is (= {:type fix/foo
                  :data data
                  :created_at now
                  :updated_at now
                  :deleted_at nil}
                 (dissoc result :id)))))))
  (testing "invalid type"
    (is (thrown? PSQLException (vertex-create! "FOO" {:a :b})))))

(deftest vertex-update!-test
  (testing "ok"
    (let [now (t/now)]
      (with-redefs [t/now (constantly now)]
        (is (= {:id 111
                :type fix/foo
                :data {:a 99 :b "lime"}
                :created_at fix/now
                :updated_at now
                :deleted_at nil}
               (vertex-update! 111 {:a 99}))))))
  (testing "not found"
    (is (nil? (vertex-update! 999 {:a 99})))))

(deftest vertex-delete!-test
  (testing "ok"
    (is (= 110 (:id (vertex-get 110))))
    (is (nil? (vertex-delete! 110)))))
