(ns clojusc.results.tests.unit.http
  "Note: this namespace is exclusively for unit tests."
  (:require
   [clojure.test :refer :all]
   [clojusc.results.core :as result]
   [clojusc.results.http :as http]))

(deftest http-generic
  (let [err (http/http-generic 403)]
    (is (= {:data nil} err))
    (is (result/errors? err))
    (is (= [{:msg {:status "HTTP error"
                   :code 403}}]
           (result/errors err)))))

(deftest not-implemented
  (let [err http/not-implemented]
    (is (= {:data nil} err))
    (is (result/errors? err))
    (is (= [{:msg {:status "This capability is not currently implemented."
                   :code 400}}]
           (result/errors err)))))

(deftest client-errors?
  (let [errs [http/not-implemented
              http/unsupported
              http/invalid-parameter]]
  (is (not (http/client-errors? [(result/create 1)])))
  (is (not (http/client-errors? [(result/create (/ 1 0))])))
  (is (http/client-errors? errs))
  (is (not (http/client-errors? [http/http-generic-server-error])))
  (is (http/client-errors? (conj errs http/http-generic-server-error)))))

(deftest server-errors?
  (let [errs [http/http-generic-server-error]]
  (is (not (http/server-errors? [(result/create 1)])))
  (is (not (http/server-errors? [(result/create (/ 1 0))])))
  (is (http/server-errors? errs))
  (is (not (http/server-errors? [http/http-generic-client-error])))
  (is (http/server-errors? (conj errs http/http-generic-client-error)))))
