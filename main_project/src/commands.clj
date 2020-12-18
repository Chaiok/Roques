(ns commands
  (:require [clojure.string :as str]
            [player :as player]
            [clojure.data.json :as json]
            [clojure.java.io :as io]
            ;[clojure.walk :as walk]
            ;[cheshire.core :refer :all]
  )
)
(defn first-args [& args]
  (first args))
;; Command functions
(def per "st")
(defn execute
  [input & id]
  ;(print (json/read-str input))(flush)
  ;(print (apply first-args id))(flush)
  (dosync (print
    (commute player/states assoc (str "player" (apply first-args id) ":")
    (json/read-str input :key-fn keyword))
  )(flush))
)