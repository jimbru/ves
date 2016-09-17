(ns ves.sql
  (:require [clojure.java.io :as io]))

(defn load-sql [filename]
  (slurp (io/resource (str "sql/" filename ".sql"))))

(defn format-sql [filename & args]
  (apply format (load-sql filename) args))

(defn make-migration [action & args]
  (condp = action
    :init          (load-sql "base-schema")
    :create-vertex (format-sql "create-vertex" (first args))
    :create-edge   (format-sql "create-edge" (first args))))
