(ns mire.player
  (:use clojure.contrib.seq-utils))

(def *current-room*)
(def *inventory*)
(def *player-name*)
(def *id*)
(def *x*)
(def *y*)
(def *up*)
(def *down*)
(def *left*)
(def *right*)

(defn carrying?
  [thing]
  (includes? @*inventory* (keyword thing)))