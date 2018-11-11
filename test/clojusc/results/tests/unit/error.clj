(ns clojusc.results.tests.unit.error
  "Note: this namespace is exclusively for unit tests."
  (:require
   [clojure.test :refer :all]
   [clojusc.results.error :as error]))

(deftest create-with-message
  (let [err (error/create "oops")]
    (is (= "oops"
           (:msg err)))
    (is (= nil
           (error/exception err)))))

(deftest create-with-exception
  (let [err (try (/ 1 0)
              (catch Exception ex (error/create ex)))]
    (is (= "Divide by zero"
           (:msg err)))
    (is (not (nil? (error/exception err))))
    (is (= 'java.lang.ArithmeticException
           (error/type err)))))
