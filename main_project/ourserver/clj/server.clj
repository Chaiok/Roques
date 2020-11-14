#!/usr/bin/env clj
(ns server)
(import '[java.io BufferedReader InputStreamReader OutputStreamWriter])
(use 'clojure.contrib.server-socket)
(defn echo-server [in out]
                    (binding [*in* (BufferedReader. (InputStreamReader. in))
                              *out* (OutputStreamWriter. out)]
                      (loop []
                        (let [input (read-line)]
                          (print input)
                          (flush))
                        (recur))))

(def my-server (create-server 8080 echo-server))