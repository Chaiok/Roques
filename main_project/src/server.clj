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
            player/*up* false
            player/*down* false
            player/*left* false
            player/*right* false
            ]
    (def i (+ i 1))
    (dosync
      (commute player/streams assoc (str "player" player/*id* ":")
      {"x:" player/*x* "y:" player/*y*})
      (commute player/states assoc (str "player" player/*id* ":")
      {:up false :down false :left false :right false})
    )

    (let [p (-> (
          (loop [] 
            ;(commands/moving) 
            (Thread/sleep 20) (recur)
          )
        ) future )]
    (-> 
      (
        (loop []
          (let [input (read-line)]
            (when input
              (commands/execute input)
              (.flush *err*) 
            )
          )
          (recur)
        )
      )
    ))
)))

(defn sidemire-handle-client [in out]
  (binding [*in* (io/reader in)
            *out* (io/writer out)
            *err* (io/writer System/err)]
    
    
    (let [f (atom 1) i (atom 300) y1 (atom 600) y2 (atom 600) x1 (atom 800) x2 (atom 800)]
      (
       (
        (loop []
          (while (pos? @y1)
            (commands/stenka 0 @y1 (str @y1 "h1")) 
            (reset! y1 (- @y1 20))
          )
          (while (pos? @y2)
            (commands/stenka 800 @y2 (str @y2 "h2")) 
            (reset! y2 (- @y2 20))
          )
          (while (pos? @x1)
            (commands/stenka @x1 0 (str @x1 "h3")) 
            (reset! x1 (- @x1 20))
          )
          (while (pos? @x2)
            (commands/stenka @x2 600 (str @x2 "h4")) 
            (reset! x2 (- @x2 20))
          )
            (while (pos? @i)
            (commands/spawnWalls 800 600 @i) 
            (reset! i (- @i 20))
          ) 
          
         (dosync (print (commute player/streams merge nil)) (flush))   
         (commands/movingall 10)
         (commands/spawnred 800 600 @f)
         (swap! f inc)
         
         (if (= @f 150)
           (reset! f 1)
           )
         (Thread/sleep 20) 
         (recur)
         )
       )
       )
      )
    

    ))

(defn -main [& args]
(println "hello")
(defonce server (socket/create-server (Integer. port) mire-handle-client))
(println "Launching server on port" port)
(defonce sideserver (socket/create-server (Integer. sideport) sidemire-handle-client))
(println "Launching sideserver on port" sideport)
)