(ns clojusc.results.http
  (:require
   [clojure.set :as set]
   [clojusc.results.util :as util]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Defaults   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def default-error-code 400)
(def client-error-code 400)
(def server-error-code 500)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Error Messages   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Generic

(def http-status-code
  "HTTP Error status code: %s.")

(def not-implemented
  "This capability is not currently implemented.")

(def unsupported
  "This capability is not currently supported.")

(def invalid-parameter
  "One or more of the parameters provided were invalid.")

(def missing-parameters
  "The following required parameters are missing from the request:")

(def status-map
  "This is a lookup data structure for how HTTP status/error codes map to CMR
  OPeNDAP errors."
  {client-error-code #{not-implemented
                       unsupported
                       invalid-parameter
                       missing-parameters}
   server-error-code #{}})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Error Handling API   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; (defn any-client-errors?
;   "In arity-1, expections a map of the form `{:errors [...]}`. In arity-2,
;   the first parameter is a status map with keys being error codes and values
;   being the error messages associated with those codes."
;   ([errors]
;     (any-client-errors? status-map errors))
;   ([errors-map errors]
;     (not
;       (nil?
;         (seq (set/intersection (get errors-map client-error-code)
;                                (set (:errors errors))))))))

; (defn any-server-errors?
;   "In arity-1, expections a map of the form `{:errors [...]}`. In arity-2,
;   the first parameter is a status map with keys being error codes and values
;   being the error messages associated with those codes."
;   ([errors]
;     (any-server-errors? status-map errors))
;   ([errors-map errors]
;     (not
;       (nil?
;         (seq (set/intersection (get errors-map server-error-code)
;                                (set (:errors errors))))))))

; (defn check
;   ""
;   [& msgs]
;   (remove nil? (map (fn [[check-fn value msg]] (when (check-fn value) msg))
;                     msgs)))
