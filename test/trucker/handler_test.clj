(ns trucker.handler-test
  (:require [clojure.test :refer :all]
            [reloaded.repl :refer [go stop]]
            [trucker.systems :refer [new-system]]
            [org.httpkit.client :as http-client]))

(deftest test-app
  (reloaded.repl/set-init! new-system)
  (go)
  (try
    (testing "get Kursk geocode info"
      (let [response (http-client/get "http://localhost:3000/geocode?address=Kursk")]
        (is (= (:status @response) 200))
        (is (.contains (:body @response) "Kursk, Kursk Oblast, Russia"))))
    (testing "get Denver geocode info"
      (let [response (http-client/get "http://localhost:3000/geocode?address=Denver")]
        (is (= (:status @response) 200))
        (is (.contains (:body @response) "Denver, IN, USA"))))
    (testing "get New York city geocode info"
      (let [response (http-client/get "http://localhost:3000/geocode?address=New%20York")]
        (is (= (:status @response) 200))
        (is (.contains (:body @response) "New York, NY, USA"))))
    (testing "empty address info"
      (let [response (http-client/get "http://localhost:3000/geocode")]
        (is (= (:status @response) 400))))
    (testing "not-found route"
      (let [response (http-client/get "http://localhost:3000/invalid")]
        (is (= (:status @response) 404))))
    (finally
      (stop))))


