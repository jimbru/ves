(ns ves.schema
  (:require [clojure.string :as string]
            [ves.sql :as sql]))

(defn usage [command options-summary]
  (->> [(format "usage: ves %s [<options>] <name>" command)
        ""
        "options:"
        options-summary
        ""]
       (string/join \newline)))

(defn init [args]
  (println (sql/make-migration :init)))

(defn create-vertex [args]
  (when (not= (count args) 1)
    (usage "create-vertex" "TODO"))
  (println (sql/make-migration :create-vertex (first args))))

(defn create-edge [args]
  (when (not= (count args) 1)
    (usage "create-vertex" "TODO"))
  (println (sql/make-migration :create-edge (first args))))
