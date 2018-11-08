(ns clojusc.results.core
  (:require
    [clojusc.results.util :as util]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   API Definition   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defprotocol ResultsAPI
  (errors [this] [this errs]
    "Return any errors that have been set on the result. If a collection is
    passed, concat the collection to the object's `:errors` metadata.")
  (errors? [this]
    "Return `true` if any errors have been set on the result.")
  (warnings [this] [this warns]
    "Return any warnings that have been set on the result. If a collection is
    passed, concat the collection to the object's `:warnings` metadata.")
  (warnings? [this]
    "Return `true` if any warnings have been set on the result."))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Implementation   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn -errors
  ([this]
    (util/getter this :errors))
  ([this errs]
    (util/setter this :errors errs)))

(defn -warnings
  ([this]
    (util/getter this :warnings))
  ([this warns]
    (util/setter this :warnings warns)))

(def behaviour
 {`errors -errors
  `errors? #(boolean (seq (-errors %)))
  `warnings -warnings
  `warnings? #(boolean (seq (-warnings %)))})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Constructor   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create
  [data]
  (with-meta {:data data} behaviour))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Operations on Collections of Results   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def collect-errors #(mapcat errors %))
(def collect-warnings #(mapcat warnings %))
(def collect #(hash-map :errors (collect-errors %)
                        :warnings (collect-warnings %)))
