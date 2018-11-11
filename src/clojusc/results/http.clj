(ns clojusc.results.http
  (:require
   [clojure.set :as set]
   [clojusc.results.core :as result]
   [clojusc.results.util :as util]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Defaults   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def default-error-code 400)
(def client-error-code 400)
(def server-error-code 500)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Utility & Support Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn client-code?
  [code]
  (and (>= code 400) (< code 500)))

(defn server-code?
  [code]
  (and (>= code 500) (< code 600)))

(defn create
  [err-data]
  (result/create err-data {:erred? true}))

(defn get-http-code
  [err]
  (get-in err [:msg :code]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Error Messages   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn http-generic
  [code]
  (create
    {:status "HTTP error"
     :code code}))

(def http-generic-client-error
  (create
    {:status "HTTP client error"
     :code client-error-code}))

(def http-generic-server-error
  (create
    {:status "HTTP server error"
     :code server-error-code}))

(def not-implemented
  (create
    {:status "This capability is not currently implemented."
     :code client-error-code}))

(def unsupported
  (create
    {:status "This capability is not currently supported."
     :code client-error-code}))

(def invalid-parameter
  (create
    {:status "One or more of the parameters provided were invalid."
     :code client-error-code}))

(defn missing-parameters
  [params]
  (create
    {:status (str "The following required parameters are missing "
                  "from the request: " params)
     :code client-error-code}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Operations on Collections of Results   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn client-errors?
  [errs]
  (->> errs
       result/collect-errors
       (map get-http-code)
       (remove nil?)
       (some client-code?)))

(defn server-errors?
  [errs]
  (->> errs
       result/collect-errors
       (map get-http-code)
       (remove nil?)
       (some server-code?)))
