(ns clojusc.results.error
  (:require
    [clojusc.results.util :as util])
  (:refer-clojure :exclude [type]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   API Definition   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defprotocol ErrorAPI
  (cause [this]
    "Return the exception cause (or nil, if there isn't one) of the associated
    exception.")
  (exception [this] [this ex]
    "Return the exception (or nil, if there isn't one) associated with the
    error. If an exception is passed, set the exception in the `:exception`
    metadata.")
  (stacktrace [this]
    "Return the stacktrace (or nil, if there isn't one) of the associated
    exception.")
  (suppressed [this]
    "Return the exceptions (or nil, if there isn't one) that were suppressed.")
  (type [this]
    "Return the exception type (or nil, if there isn't one) of the associated
    exception."))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Implementation   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn -exception
  ([this]
    (util/getter this :exception))
  ([this ex]
    (util/setter this :exception ex)))

(def behaviour
 {`cause #(.getCause (exception %))
  `exception -exception
  `stacktrace #(vec (.getStackTrace (exception %)))
  `suppressed #(vec (.getSuppressed (exception %)))
  `type #(symbol (.getName (clojure.core/type (exception %))))})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Constructor   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create
  [msg-or-ex]
  (if (isa? (clojure.core/type msg-or-ex) Exception)
    (let [err (with-meta {:msg (.getMessage msg-or-ex)} behaviour)]
      (exception err msg-or-ex))
    (with-meta {:msg msg-or-ex} behaviour)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Operations on Collections of Errors   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def causes #(mapv cause %))
(def exceptions #(mapv exception %))
(def stacktraces #(mapv stacktrace %))
(def suppresseds #(mapv suppressed %))
(def types #(mapv type %))
