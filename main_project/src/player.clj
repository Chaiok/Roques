(ns player)

(def ^:dynamic *id*)
(def ^:dynamic *x*)
(def ^:dynamic *y*)

(def streams (ref {}))
(def streamsinfo (deref streams))