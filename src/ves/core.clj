(ns ves.core
  (:require [clojure.set :refer [difference]]
            [clojure.string :as string]
            [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def cli-options
  [["-d" "--dbname dbname" "Name of the database to connect to."]
   ["-h" "--host hostname" "Host name of the machine on which the server is running."
    :default "localhost"]
   ["-p" "--port port" "TCP port on which the server is listening for connections."
    :default 5432
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-v" "--verbose" "Verbose!"
    :default 0
    :assoc-fn (fn [m k _] (update-in m [k] inc))]
   ["-U" "--username username" "Connect to the database as this user."]
   ["-?" "--help" "Show help."]
   ])

(defn missing-opts [opts]
  (let [diff (difference #{:dbname :username} (set (keys opts)))]
    (when (not (empty? diff))
      diff)))

(defn usage [options-summary]
  (->> ["ves"
        ""
        "Usage: ves [options] action"
        ""
        "Options:"
        options-summary
        ""
        "Actions:"
        "  foo    Do a foo."
        "  bar    Do a bar."
        "  help   Show help."
        ""
        "Run `man ves` for more information."]
       (string/join \newline)))

(defn error-msg [errors]
  (str "Error:\n" (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)
        missing (missing-opts options)]
    (cond
      (or (:help options) (= "help" (first arguments))) (exit 0 (usage summary))
      (not= (count arguments) 1) (exit 1 (usage summary))
      errors (exit 1 (error-msg errors))
      missing (exit 1 (error-msg (map #(str "Missing required option: " (name %)) missing))))
    (case (first arguments)
      "foo" (println "done a foo")
      "bar" (println "done a bar" options)
      (exit 1 "Please specify an action (or run `ves help` for help)."))))
