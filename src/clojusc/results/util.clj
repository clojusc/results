(ns clojusc.results.util)

(defn getter
  [this key]
  (get (meta this) key))

(defn setter
  [this key value]
  (vary-meta this
             assoc
             key
             (concat (getter this key) value)))

(defn exception->errors-data
  [exception]
  [(or (.getMessage exception)
       (ex-data exception))])
