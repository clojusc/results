(ns clojusc.results.core
  (:require
    [clojusc.results.error :as error]
    [clojusc.results.util :as util]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   API Definition   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defprotocol ResultsAPI
  :extend-via-metadata true
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
    (util/setter-concat this :errors errs)))

(defn -warnings
  ([this]
    (util/getter this :warnings))
  ([this warns]
    (util/setter-concat this :warnings warns)))

(def behaviour
 {`errors -errors
  `errors? #(boolean (seq (-errors %)))
  `warnings -warnings
  `warnings? #(boolean (seq (-warnings %)))})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Constructor   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def default-options
  {:key-name :data})

(defmacro create
  ([body]
    (create &form &env body {}))
  ([body passed-opts]
   `(let [opts# (merge default-options ~passed-opts)
          key-name# (:key-name opts#)]
      (if (:erred? opts#)
        (errors
          (with-meta {key-name# nil} behaviour)
          [(error/create ~body)])
      (try (with-meta {key-name# ~body} behaviour)
        (catch Exception ex#
          (errors
            (with-meta {key-name# nil} behaviour)
            [(error/create ex#)])))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Operations on Collections of Results   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def collect-values #(mapcat vals %))
(def collect-errors #(mapcat errors %))
(def collect-warnings #(mapcat warnings %))
(def collect #(hash-map :values (collect-values %)
                        :errors (collect-errors %)
                        :warnings (collect-warnings %)))
