(ns commands
  (:require [clojure.string :as str]
            [player :as player]
            [clojure.data.json :as json]
  )
)

(defn first-args [& args]
  (first args))

;Command functions
(defn execute [input]
  (dosync
    (commute player/states assoc (str "player" player/*id* ":")
    (json/read-str input :key-fn keyword))
  )
)

(defn moving  []
  (dosync
    (let [playerid (str "player" player/*id* ":")
          x (get (@player/streams playerid) "x:")
          y (get (@player/streams playerid) "y:")
          up (get (@player/states playerid) :up)
          down (get (@player/states playerid) :down)
          left (get (@player/states playerid) :left)
          right (get (@player/states playerid) :right)
        ]
      (if (= up true)
        (if (= left true) 
          (commute player/streams assoc playerid {"x:" (- x 5) "y:" (- y 5)}) 
          (if (= right true) 
            (commute player/streams assoc playerid {"x:" (+ x 5) "y:" (- y 5)})  
            (commute player/streams assoc playerid {"x:" x "y:" (- y 5)}) 
          )
        )
        (if (= down true) 
          (if (= left true) 
            (commute player/streams assoc playerid {"x:" (- x 5) "y:" (+ y 5)}) 
            (if (= right true) 
              (commute player/streams assoc playerid {"x:" (+ x 5) "y:" (+ y 5)})  
              (commute player/streams assoc playerid {"x:" x "y:" (+ y 5)}) 
            )
          ) 
          (if (= left true) 
            (commute player/streams assoc playerid {"x:" (- x 5) "y:" y}) 
            (if (= right true) 
              (commute player/streams assoc playerid {"x:" (+ x 5) "y:" y})  
              (commute player/streams assoc playerid {"x:" x "y:" y}) 
            )
          ) 
        )
      )
    )
  )
)