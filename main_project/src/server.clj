(ns server
(:require [clojure.java.io :as io]
          [server.socket :as socket]))

(def port (* 5 1111))
(def prompt "> ")

(defn mire-handle-client [in out]
  (binding [*in* (io/reader in)
            *out* (io/writer out)
            *err* (io/writer System/err)]
  (print "\nWhat is your name? ") (flush)
       (try (loop [input (read-line)]
             (when input
               (print input)
               (.flush *err*) 
               ;(print prompt) 
               (flush)
               (recur (read-line)))))))

(defn -main
  ([port dir]
     (defonce server (socket/create-server (Integer. port) mire-handle-client))
     (println "Launching Mire server on port" port))
  ([port] (-main port "resources/rooms"))
  ([] (-main port)))
