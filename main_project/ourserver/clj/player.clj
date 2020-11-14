(ns player
  (:use clojure.contrib.seq-utils))

(def *current-room*)
(def *inventory*)
(def *player-name*)
(def *id*)

(defn carrying?
  [thing]
  (includes? @*inventory* (keyword thing)))