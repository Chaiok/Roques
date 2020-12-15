(ns server
(:require [clojure.java.io :as io]
          [server.socket :as socket]
          [player :as player]
          [commands :as commands]))

(def port (* 3 2111))
(def sideport (* 4 2111))
(def i 1)
(def XX 1)
(def YY 1)

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
               (flush)
               (commands/execute input)
               (.flush *err*) 
               (recur (read-line))))))))

(defn sidemire-handle-client [in out]
  (binding [*in* (io/reader in)
            *out* (io/writer out)
            *err* (io/writer System/err)]
    (dosync (print (commute player/streams merge nil))(flush))
       (loop [] (dosync (print (commute player/streams merge nil))(flush)) 
       (dosync
          (doseq [[k v] (commute player/states merge nil) ]
            (doseq [[kkk vvv] v ]
              ;;up
              (if (= kkk :up)
                (if (= vvv true)
                  (doseq [[k v] (commute player/streams merge nil) ]
                    (doseq [[kk vv] v ]
                      (if (= kk "y:")
                        (def YY (- vv 5))
                      )
                      (if (= kk "x:")
                        (def XX vv)
                      )
                    )
                    (commute player/streams assoc k
                      {"x:" XX "y:" YY}
                    )
                  )
                )
              )
              ;;down
              (if (= kkk :down)
                (if (= vvv true)
                  (doseq [[k v] (commute player/streams merge nil) ]
                    (doseq [[kk vv] v ]
                      (if (= kk "y:")
                        (def YY (+ vv 5))
                      )
                      (if (= kk "x:")
                        (def XX vv)
                      )
                    )
                    (commute player/streams assoc k
                      {"x:" XX "y:" YY}
                    )
                  )
                )
              )
              ;;left
              (if (= kkk :left)
                (if (= vvv true)
                  (doseq [[k v] (commute player/streams merge nil) ]
                    (doseq [[kk vv] v ]
                      (if (= kk "y:")
                        (def YY vv)
                      )
                      (if (= kk "x:")
                        (def XX (- vv 5))
                      )
                    )
                    (commute player/streams assoc k
                      {"x:" XX "y:" YY}
                    )
                  )
                )
              )
              ;;right
              (if (= kkk :right)
                (if (= vvv true)
                  (doseq [[k v] (commute player/streams merge nil) ]
                    (doseq [[kk vv] v ]
                      (if (= kk "y:")
                        (def YY vv)
                      )
                      (if (= kk "x:")
                        (def XX (+ vv 5))
                      )
                    )
                    (commute player/streams assoc k
                      {"x:" XX "y:" YY}
                    )
                  )
                )
              )
            )
          ) 
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