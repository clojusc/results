(ns clojusc.results.util)

(defn getter
  [this key]
  (get (meta this) key))

(defn setter
  [this key value]
  (vary-meta this
             assoc
             key
             value))

(defn setter-concat
  [this key value]
  (setter this
          key
          (concat (getter this key) value)))
