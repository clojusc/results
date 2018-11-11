(ns clojusc.results.repl
  "Results lib development namespace."
  (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.set :as set]
    [clojure.string :as string]
    [clojure.tools.namespace.repl :as repl]
    [clojusc.results.core :as result]
    [clojusc.results.error :as error]
    [clojusc.results.http :as http]
    [clojusc.results.util :as util]
    [trifl.java :refer [show-methods]])
  (:import
   (java.net URI)
   (java.nio.file Paths)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Initial Setup & Utility Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn banner
  []
  (println (slurp (io/resource "text/banner.txt")))
  :ok)
