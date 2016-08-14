(ns trucker.handler
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [environ.core :refer [env]]
            [taoensso.carmine :as carmine]
            [org.httpkit.client :as http-client]
            [ring.util.codec :refer [url-encode]]
            [clojure.string :as s]))

(def conn {:pool {}
           :spec {:host (:redis-host env)
                  :port (-> (:redis-port env)
                            (Integer/parseInt))}})

(def primary-cache-ttl-s (:primary-cache-ttl-s env))

(def external-service-url (:external-service-url env))

(def secondary-cache (atom {}))

(defn get-primary-cache-value [id]
  (carmine/wcar conn (carmine/get id)))

(defn set-primary-cache-value! [id value]
  (carmine/wcar conn (carmine/set id value :ex primary-cache-ttl-s)))

(defn get-secondary-cache-value [id]
  (get @secondary-cache id))

(defn set-secondary-cache-value! [id value]
  (swap! secondary-cache assoc id value))

(defn evict-secondary-cache-value! [id]
  (swap! secondary-cache dissoc id))

(defn value->response [value]
  (select-keys value [:status :body]))

(defn expensive-geoservice-call [address]
  (let [url            (str external-service-url
                            (when-not (.endsWith external-service-url "/")
                              "/")
                            "maps/api/geocode/json?address="
                            (url-encode address))
        promise-result (http-client/get url)]
    (set-secondary-cache-value! address promise-result)
    (let [{:keys [status body] :as value} @promise-result
          success                         (and status
                                               (>= status 200)
                                               (< status 300))]
      (when success
        (set-primary-cache-value! address body))
      (evict-secondary-cache-value! address)
      (value->response value))))

(defn process-geocode [address]
  (let [primary-value (get-primary-cache-value address)]
    (if primary-value
      {:status 200
       :body   primary-value}
      (let [secondary-value (get-secondary-cache-value address)]
        (if secondary-value
          (value->response @secondary-value)
          (expensive-geoservice-call address))))))

(defroutes app
  (GET "/geocode" {query-params :query-params}
    (let [address (get query-params "address")]
      (if (s/blank? address)
        {:status 400
         :body "Please, provide address param in a query string"}
        (process-geocode address))))
  (route/not-found "Not Found"))

