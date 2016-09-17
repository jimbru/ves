(ns ves.dump
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.pprint :refer [pprint]]
            [clojure.set :refer [difference]]
            [clojure.string :as string]
            [clojure.tools.cli :refer [parse-opts]]))

(def cli-options
  [["-h" "--host hostname" "Host name of the machine on which the server is running."
    :default "localhost"]
   ["-p" "--port port" "TCP port on which the server is listening for connections."
    :default 5432
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-u" "--username username" "Connect to the database as this user."
    :default (System/getProperty "user.name")]])

(defn missing-opts [opts]
  (let [diff (difference #{:dbname :username} (set (keys opts)))]
    (when (not (empty? diff))
      diff)))

(defn usage [options-summary]
  (->> ["usage: ves dump [<options>] <dbname>"
        ""
        "options:"
        options-summary
        ""]
       (string/join \newline)))

(defn error-msg [errors]
  (str "error:\n" (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn dbspec [host port username dbname]
  {:classname   "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname     (str "//" host ":" port "/" dbname)
   :user        username})

(defn query-types [spec]
  (let [qq (fn [sql]
             (->> (jdbc/query spec [sql])
                  (map #(vector (:id %) (:name %)))))]
    {:vertices (qq "SELECT * FROM vertex_types ORDER BY created_at ASC")
     :edges    (qq "SELECT * FROM edge_types ORDER BY created_at ASC")}))

(defn run [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (= (count arguments) 0) (exit 1 (usage summary))
      errors                  (exit 1 (error-msg errors)))
    (pprint
     (query-types (dbspec (:hostname options)
                          (:port options)
                          (:username options)
                          (first arguments))))))
