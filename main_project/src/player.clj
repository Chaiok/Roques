(ns player)

(def ^:dynamic *id*)

(def ^:dynamic *x*)
(def ^:dynamic *y*)

(def ^:dynamic *up*)
(def ^:dynamic *down*)
(def ^:dynamic *left*)
(def ^:dynamic *right*)

(def streams (ref {"red:" {}}))
(def states (ref {}))
(def streamsinfo (deref streams))