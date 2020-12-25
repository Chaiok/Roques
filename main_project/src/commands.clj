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
    (let [playerid
          x (get (@player/streams playerid) "x:")
          y (get (@player/streams playerid) "y:")
          x1 (inc x)
          y1 (inc y)]
      (commute player/streams assoc (str "player" player/*id* ":") {"x:" x1 "y:" y1})
    )
  )
)