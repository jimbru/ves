(ns ves.core-test
  (:require [clj-time.core :as t]
            [clojure.test :refer :all]
            [ves.core :refer :all]
            [ves.fixtures :as fix])
  (:import org.postgresql.util.PSQLException))

(use-fixtures :each fix/wrap-data)

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
    (is (nil? (vertex-delete! 110))))
  (testing "not found"
    (is (thrown? Exception (vertex-delete! 999)))))

(deftest edge-get-test
  (testing "ok"
    (is (= {:id1 110
            :type fix/looks-like
            :id2 111
            :data {:a 10 :b 11}
            :created_at fix/now
            :updated_at fix/now
            :deleted_at nil}
           (edge-get 110 fix/looks-like 111))))
  (testing "not found"
    (is (nil? (edge-get 110 fix/looks-like 999)))
    (is (nil? (edge-get 110 fix/looks-like 113)))
    (is (nil? (edge-get 110 999 111)))
    (is (nil? (edge-get 999 fix/looks-like 111)))))

(deftest edge-count-test
  (testing "ok"
    (is (= 3 (edge-count 110 fix/looks-like))))
  (testing "not found"
    (is (= 0 (edge-count 110 999)))
    (is (= 0 (edge-count 999 fix/looks-like)))))

(deftest edge-range-test
  (testing "basic"
    (is (= [{:id1 110
             :type fix/looks-like
             :id2 114
             :data {:a 22 :b 23}
             :created_at fix/now
             :updated_at fix/now
             :deleted_at nil}
            {:id1 110
             :type fix/looks-like
             :id2 112
             :data {:a 12 :b 13}
             :created_at fix/now
             :updated_at fix/now
             :deleted_at nil}]
           (edge-range 110 fix/looks-like 2)))
    (is (= [{:id1 111
             :type fix/smells-like
             :id2 110
             :data {:a 20 :b 21}
             :created_at fix/now
             :updated_at fix/now
             :deleted_at nil}]
           (edge-range 111 fix/smells-like 5))))
  (testing "paged"
    (is (= [{:id1 110
             :type fix/looks-like
             :id2 112
             :data {:a 12 :b 13}
             :created_at fix/now
             :updated_at fix/now
             :deleted_at nil}
            {:id1 110
             :type fix/looks-like
             :id2 111
             :data {:a 10 :b 11}
             :created_at fix/now
             :updated_at fix/now
             :deleted_at nil}]
           (edge-range 110 fix/looks-like 2 112)))
    (is (= [{:id1 110
             :type fix/looks-like
             :id2 111
             :data {:a 10 :b 11}
             :created_at fix/now
             :updated_at fix/now
             :deleted_at nil}]
           (edge-range 110 fix/looks-like 2 111)))
    (is (= [] (edge-range 110 fix/looks-like 2 110)))))

(deftest edge-create!-test
  (testing "ok"
    (let [now (t/now)]
      (with-redefs [t/now (constantly now)]
        (is (= {:id1 110
                :type fix/smells-like
                :id2 112
                :data {:a 1}
                :created_at now
                :updated_at now
                :deleted_at nil}
               (edge-create! 110 fix/smells-like 112 {:a 1}))))))
  (testing "invalid type"
    (is (thrown? PSQLException (edge-create! 110 999 111 {:a 1}))))
  (testing "duplicate edge"
    (is (thrown? PSQLException (edge-create! 110 fix/looks-like 111 {:a 1})))))

(deftest edge-update!-test
  (testing "ok"
    (let [now (t/now)]
      (with-redefs [t/now (constantly now)]
        (is (= {:id1 110
                :type fix/looks-like
                :id2 111
                :data {:a 99 :b 11 :x "hello"}
                :created_at fix/now
                :updated_at now
                :deleted_at nil}
               (edge-update! 110 fix/looks-like 111 {:a 99 :x "hello"}))))))
  (testing "not found"
    (is (nil? (edge-update! 110 fix/looks-like 999 {:a 1})))
    (is (nil? (edge-update! 110 999 111 {:a 1})))))

(deftest edge-delete!-test
  (testing "ok"
    (is (nil? (edge-delete! 110 fix/looks-like 111)))
    (is (nil? (edge-get 110 fix/looks-like 111))))
  (testing "not found"
    (is (thrown? Exception (edge-delete! 110 fix/looks-like 999)))))
