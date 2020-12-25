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
  (def YY)
  (def XX)
  (dosync
            (doseq [[kk vv] (commute player/streams get (str "player" player/*id* ":")) ] 
              (if (= kk "y:")
                (def YY vv)
              )
              (if (= kk "x:")
                (def XX vv)
              )
            )
            (doseq [[kk vv] (commute player/states get (str "player" player/*id* ":"))]
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
            (commute player/streams assoc (str "player" player/*id* ":") {"x:" XX "y:" YY})
  )
)