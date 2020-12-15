(ns commands
  (:require [clojure.string :as str]
            [player :as player]
            [clojure.data.json :as json]
  )
)

;; Command functions

(defn execute
  "Execute a command that is passed to us."
  [input]
  ;;(print (json/write-str {:a 1 :b 2}))
  (print (json/read-str input))
)