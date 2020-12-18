(ns server
(:require [clojure.java.io :as io]
          [server.socket :as socket]
          [player :as player]
          [commands :as commands]
          [clojure.string :as str]))

(def port (* 3 1111))
(def sideport (* 4 1111))
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
    {"x:" player/*x* "y:" player/*y*})
    (commute player/states assoc (str "player" player/*id* ":")
    {:up false :down false :left false :right false})
    )
    ;(print (commute player/streams assoc player/*id* {"x" player/*x* "y" player/*y*}))(flush))
       ;(commute player/streams assoc player/*id* *out*))
       (try (loop [input (read-line)]
             (when input
               ;;(print input)
               ;;(flush)
               (commands/execute input player/*id*)
               (.flush *err*) 
               (recur (read-line))))))))

(defn sidemire-handle-client [in out]
  (binding [*in* (io/reader in)
            *out* (io/writer out)
            *err* (io/writer System/err)]
    (dosync (print (commute player/streams merge nil))(flush))
       (loop [] (dosync (print (commute player/streams merge nil))(flush)) 
          (def per {})
          (doseq [[k v] (dosync (commute player/states merge nil)) ]
            ;(print k)(flush)
            (def XX)
            (def YY)
            (doseq [[kk vv] (dosync (commute player/streams get k)) ] 
              (if (= kk "y:")
                (def YY vv)
              )
              (if (= kk "x:")
                (def XX vv)
              )
            )
            (doseq [[kk vv] v ]
              ;;up
              (if (= kk :up)
                (if (= vv true)
                  (def YY (- YY 5))
                )
              )
              ;;down
              (if (= kk :down)
                (if (= vv true)
                  (def YY (+ YY 5))
                )
              )
              ;;left
              (if (= kk :left)
                (if (= vv true)
                  (def XX (- XX 5))
                )
              )
              ;;right
              (if (= kk :right)
                (if (= vv true)
                  (def XX (+ XX 5))
                )
              )
            )
            ;(print (str "player" (str/replace (str/replace k #":" "") #"player" "") ":") {"x:" XX "y:" YY})
            (dosync (commute player/streams assoc k {"x:" XX "y:" YY}))
            ;(dosync (commute player/streams assoc (str "player" (str/replace (str/replace k #":" "") #"player" "") ":") {"x:" XX "y:" YY}))
            ;(commute player/streams assoc 2 "February")
            ;(assoc per (str "player" (str/replace (str/replace k #":" "") #"player" "") ":") {"x:" XX "y:" YY})
            ;(print {(str "player" (str/replace (str/replace k #":" "") #"player" "") ":") {"x:" XX "y:" YY}})(flush)
          ) 
        (Thread/sleep 20) (recur))
))

(defn -main [& args]
(println "hello")
(defonce server (socket/create-server (Integer. port) mire-handle-client))
(println "Launching server on port" port)
(defonce sideserver (socket/create-server (Integer. sideport) sidemire-handle-client))
(println "Launching sideserver on port" sideport)
)