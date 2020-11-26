(ns server
(:require [clojure.java.io :as io]
          [server.socket :as socket]
          [player :as player]))

(def port (* 3 2221))
(def sideport (* 4 2221))
(def i 1)

(defn mire-handle-client [in out]
  (binding [*in* (io/reader in)
            *out* (io/writer out)
            *err* (io/writer System/err)]
   (binding [
            player/*id* i
            player/*x* (* i 100)
            player/*y* 300
            ]
    (def i (+ i 1))
    (dosync
    (commute player/streams assoc (str "player" player/*id* ":") ;player/*x*))
    {"x:" player/*x* "y:" player/*y*}))
    ;(print (commute player/streams assoc player/*id* {"x" player/*x* "y" player/*y*}))(flush))
       ;(commute player/streams assoc player/*id* *out*))
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
    (dosync (print (commute player/streams merge nil))(flush))
       (loop [] (dosync (print (commute player/streams merge nil))(flush)) (Thread/sleep 20) (recur))
))

(defn -main [& args]
(println "hello")
(defonce server (socket/create-server (Integer. port) mire-handle-client))
(println "Launching server on port" port)
(defonce sideserver (socket/create-server (Integer. sideport) sidemire-handle-client))
(println "Launching sideserver on port" sideport)
)

