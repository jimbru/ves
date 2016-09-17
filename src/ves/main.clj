(ns ves.main
  (:require [clojure.string :as string]
            [ves.dump :as dump]
            [ves.schema :as schema])
  (:gen-class))

(def usage
  (->> ["usage: ves <command> [<args>]"
        ""
        "commands:"
        "  init             Generate base schema."
        "  create-vertex    Add a new vertex type."
        "  create-edge      Add a new edge type."
        "  dump             Dumps the current schema."
        "  help             Show help."
        ""
        "See 'ves help <command>' to read about a specific subcommand."]
       (string/join \newline)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main [& args]
  (when (= (count args) 0)
    (exit 1 usage))
  (condp = (first args)
    "help"          (exit 0 usage)
    "init"          (schema/init)
    "create-vertex" (schema/create-vertex (rest args))
    "create-edge"   (schema/create-edge (rest args))
    "dump"          (dump/run (rest args))
    (exit 1 (format "ves: '%s' is not a ves command. See 'ves help'." (first args)))))
