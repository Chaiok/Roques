(ns server
(:require [clojure.java.io :as io]
          [server.socket :as socket]
          [player :as player]))

(def port (* 2 2122))
(def sideport (* 3 2122))
(def i 1)

(defn mire-handle-client [in out]
  (binding [*in* (io/reader in)
            *out* (io/writer out)
            *err* (io/writer System/err)]
  ;(print "\nWhat is your name? ") (flush)
   (binding [
            player/*id* i
            player/*x* 300
            player/*y* 300
            ]
            (def i (+ i 1))
       (try (loop [input (read-line)]
             (when input
               (print input)
               (.flush *err*) 
               (flush)
               (recur (read-line))))))))

(defn sidemire-handle-client [in out]
  (binding [*in* (io/reader in)
            *out* (io/writer out)
            *err* (io/writer System/err)]
  ;(print "\nWhat is your name? ") (flush)
       (try (loop [input (read-line)]
             (when input
               (print input)
               (.flush *err*) 
               (flush)
               (recur (read-line)))))))



(defn -main [& args]
(println "hello")
(defonce server (socket/create-server (Integer. port) mire-handle-client))
(println "Launching server on port" port)
(defonce sideserver (socket/create-server (Integer. sideport) sidemire-handle-client))
(println "Launching sideserver on port" sideport)
)

