(ns player)

(def ^:dynamic *id*)

(def ^:dynamic *x*)
(def ^:dynamic *y*)

(def ^:dynamic *up*)
(def ^:dynamic *down*)
(def ^:dynamic *left*)
(def ^:dynamic *right*)

(def powers (ref {}))
(def colorOchki (ref {}))
(def streams (ref {"red:" {} "block:" {}} ))
(def states (ref {}))
(def streamsinfo (deref streams))