(ns clojusc.results.tests.unit.core
  "Note: this namespace is exclusively for unit tests."
  (:require
   [clojure.test :refer :all]
   [clojusc.results.core :as result]))

(def test-result-1 (result/create 1))
(def test-result-2 (result/errors test-result-1 ["Oops"]))
(def test-result-3 (result/warnings test-result-1 ["Be careful ..."]))
(def test-result-4 (result/errors
                    test-result-2
                    ["Uh-oh ..." "*BOOM*"]))
(def test-result-5 (result/warnings
                    test-result-3
                    ["Watch out!" "I have a bad feeling about this ..."]))

(deftest create
  (is (= {:data nil} (result/create nil)))
  (is (= {:data 0} (result/create 0)))
  (is (= {:data 1} test-result-1))
  (let [r (result/create (/ 1 0))]
    (is (= {:data nil} r))
    (is (= [{:msg "Divide by zero"}] (result/errors r))))
  (is (= {:value nil} (result/create nil {:key-name :value})))
  (is (= {:value 0} (result/create 0 {:key-name :value})))
  (let [r (result/create (/ 1 0) {:key-name :value})]
    (is (= {:value nil} r))
    (is (= [{:msg "Divide by zero"}] (result/errors r))))
  (let [r (result/create {:error :data} {:erred? true})]
    (is (= {:data nil} r))
    (is (= [{:msg {:error :data}}] (result/errors r)))))


(deftest errors?
  (is (not (result/errors? test-result-1)))
  (is (result/errors? test-result-2))
  (is (not (result/errors? test-result-3)))
  (is (result/errors? test-result-4))
  (is (not (result/errors? test-result-5))))

(deftest warnings?
  (is (not (result/warnings? test-result-1)))
  (is (not (result/warnings? test-result-2)))
  (is (result/warnings? test-result-3))
  (is (not (result/warnings? test-result-4)))
  (is (result/warnings? test-result-5)))

(deftest errors
  (is (= nil
         (result/errors test-result-1)))
  (is (= ["Oops"]
         (result/errors test-result-2)))
  (is (= nil
         (result/errors test-result-3)))
  (is (= ["Oops" "Uh-oh ..." "*BOOM*"]
         (result/errors test-result-4)))
  (is (= nil
         (result/errors test-result-5))))

(deftest warnings
  (is (= nil
         (result/warnings test-result-1)))
  (is (= nil
         (result/warnings test-result-2)))
  (is (= ["Be careful ..."]
         (result/warnings test-result-3)))
  (is (= nil
         (result/warnings test-result-4)))
  (is (= ["Be careful ..."
          "Watch out!"
          "I have a bad feeling about this ..."]
         (result/warnings test-result-5))))

(deftest collect-values
  (is (= [1]
         (result/collect-values [test-result-1])))
  (is (= [1 1]
         (result/collect-values [test-result-1
                                  test-result-2])))
  (is (= [1 1 1]
         (result/collect-values [test-result-1
                                  test-result-2
                                  test-result-3])))
  (is (= [1 1 1 1 1]
         (result/collect-values [test-result-1
                                  test-result-2
                                  test-result-3
                                  test-result-4
                                  test-result-5]))))
(deftest collect-errors
  (is (= []
         (result/collect-errors [test-result-1])))
  (is (= ["Oops"]
         (result/collect-errors [test-result-1
                                  test-result-2])))
  (is (= ["Oops"]
         (result/collect-errors [test-result-1
                                  test-result-2
                                  test-result-3])))
  (is (= ["Oops" "Oops" "Uh-oh ..." "*BOOM*"]
         (result/collect-errors [test-result-1
                                  test-result-2
                                  test-result-3
                                  test-result-4
                                  test-result-5]))))

(deftest collect-warnings
  (is (= []
         (result/collect-warnings [test-result-1])))
  (is (= []
         (result/collect-warnings [test-result-1
                                    test-result-2])))
  (is (= ["Be careful ..."]
         (result/collect-warnings [test-result-1
                                    test-result-2
                                    test-result-3])))
  (is (= ["Be careful ..."
          "Be careful ..."
          "Watch out!"
          "I have a bad feeling about this ..."]
         (result/collect-warnings [test-result-1
                                    test-result-2
                                    test-result-3
                                    test-result-4
                                    test-result-5]))))

(deftest collect
  (is (= {:values [1]
          :errors []
          :warnings []}
         (result/collect [test-result-1])))
  (is (= {:values [1 1]
          :errors ["Oops"]
          :warnings []}
         (result/collect [test-result-1
                           test-result-2])))
  (is (= {:values [1 1 1]
          :errors ["Oops"]
          :warnings ["Be careful ..."]}
         (result/collect [test-result-1
                           test-result-2
                           test-result-3])))
  (is (= {:values [1 1 1 1 1]
          :errors ["Oops" "Oops" "Uh-oh ..." "*BOOM*"]
          :warnings ["Be careful ..."
                     "Be careful ..."
                     "Watch out!"
                     "I have a bad feeling about this ..."]}
         (result/collect [test-result-1
                           test-result-2
                           test-result-3
                           test-result-4
                           test-result-5]))))
