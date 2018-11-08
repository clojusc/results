(ns clojusc.results.tests.unit.core
  "Note: this namespace is exclusively for unit tests."
  (:require
   [clojure.test :refer :all]
   [clojusc.results.core :as results]))

(def test-result-1 (results/create 1))
(def test-result-2 (results/errors test-result-1 ["Oops"]))
(def test-result-3 (results/warnings test-result-1 ["Be careful ..."]))
(def test-result-4 (results/errors
                    test-result-2
                    ["Uh-oh ..." "*BOOM*"]))
(def test-result-5 (results/warnings
                    test-result-3
                    ["Watch out!" "I have a bad feeling about this ..."]))

(deftest create
  (is (= {:data nil} (results/create nil)))
  (is (= {:data 0} (results/create 0)))
  (is (= {:data 1} test-result-1))
  (let [r (results/create (/ 1 0))]
    (is (= {:data nil} r))
    (is (= ["Divide by zero"] (results/errors r)))))

(deftest errors?
  (is (not (results/errors? test-result-1)))
  (is (results/errors? test-result-2))
  (is (not (results/errors? test-result-3)))
  (is (results/errors? test-result-4))
  (is (not (results/errors? test-result-5))))

(deftest warnings?
  (is (not (results/warnings? test-result-1)))
  (is (not (results/warnings? test-result-2)))
  (is (results/warnings? test-result-3))
  (is (not (results/warnings? test-result-4)))
  (is (results/warnings? test-result-5)))

(deftest errors
  (is (= nil
         (results/errors test-result-1)))
  (is (= ["Oops"]
         (results/errors test-result-2)))
  (is (= nil
         (results/errors test-result-3)))
  (is (= ["Oops" "Uh-oh ..." "*BOOM*"]
         (results/errors test-result-4)))
  (is (= nil
         (results/errors test-result-5))))

(deftest warnings
  (is (= nil
         (results/warnings test-result-1)))
  (is (= nil
         (results/warnings test-result-2)))
  (is (= ["Be careful ..."]
         (results/warnings test-result-3)))
  (is (= nil
         (results/warnings test-result-4)))
  (is (= ["Be careful ..."
          "Watch out!"
          "I have a bad feeling about this ..."]
         (results/warnings test-result-5))))

(deftest collect-errors
  (is (= []
         (results/collect-errors [test-result-1])))
  (is (= ["Oops"]
         (results/collect-errors [test-result-1
                                  test-result-2])))
  (is (= ["Oops"]
         (results/collect-errors [test-result-1
                                  test-result-2
                                  test-result-3])))
  (is (= ["Oops" "Oops" "Uh-oh ..." "*BOOM*"]
         (results/collect-errors [test-result-1
                                  test-result-2
                                  test-result-3
                                  test-result-4
                                  test-result-5]))))

(deftest collect-warnings
  (is (= []
         (results/collect-warnings [test-result-1])))
  (is (= []
         (results/collect-warnings [test-result-1
                                    test-result-2])))
  (is (= ["Be careful ..."]
         (results/collect-warnings [test-result-1
                                    test-result-2
                                    test-result-3])))
  (is (= ["Be careful ..."
          "Be careful ..."
          "Watch out!"
          "I have a bad feeling about this ..."]
         (results/collect-warnings [test-result-1
                                    test-result-2
                                    test-result-3
                                    test-result-4
                                    test-result-5]))))

(deftest collect
  (is (= {:errors []
          :warnings []}
         (results/collect [test-result-1])))
  (is (= {:errors ["Oops"]
          :warnings []}
         (results/collect [test-result-1
                           test-result-2])))
  (is (= {:errors ["Oops"]
          :warnings ["Be careful ..."]}
         (results/collect [test-result-1
                           test-result-2
                           test-result-3])))
  (is (= {:errors ["Oops" "Oops" "Uh-oh ..." "*BOOM*"]
          :warnings ["Be careful ..."
                     "Be careful ..."
                     "Watch out!"
                     "I have a bad feeling about this ..."]}
         (results/collect [test-result-1
                           test-result-2
                           test-result-3
                           test-result-4
                           test-result-5]))))
