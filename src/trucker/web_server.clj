(ns trucker.web-server
  (:require [com.stuartsierra.component :as component]
            [immutant.web :as web]
            [immutant.web.undertow :as undertow]
            [taoensso.timbre :as timbre]
            [environ.core :refer [env]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :refer [charset]]
            [ring.middleware.content-type :as content-type]
            [ring.middleware.json :refer [wrap-json-response]]
            [trucker.handler :refer [app]]
            [compojure.handler :refer [api]]))

(defn wrap-charset [handler]
  (fn [request]
    (let [response (handler request)]
      (charset response "UTF-8"))))

(defn allow-cross-origin [handler]
  (fn [request]
    (let [response (handler request)]
      (-> response
          (assoc-in [:headers "Access-Control-Allow-Origin"] "*")
          (assoc-in [:headers "Access-Control-Allow-Methods"] "GET,PUT,POST,DELETE,OPTIONS")
          (assoc-in [:headers "Access-Control-Allow-Headers"] "X-Requested-With,Content-Type,Cache-Control,token")))))

(defn wrap-exception [handler]
  (fn [request]
    (try
      (handler request)
      (catch Exception e
        (timbre/error e)
        {:status 500
         :body   "Server error"}))))

(defrecord WebServer [web-server]
  component/Lifecycle
  (start [component]
    (if web-server
      component
      (let [host         (:http-host env)
            port         (-> (:http-port env)
                             (Integer/parseInt))
            _            (timbre/info "Starting web server on " host ":" port)
            ring-handler (-> app
                             (wrap-charset)
                             (wrap-json-response)
                             (api)
                             (allow-cross-origin)
                             (wrap-exception))
            web-server   (->> (undertow/options {:host host
                                                 :port port})
                              (web/run ring-handler))]
        (assoc component :web-server web-server))))
  (stop [component]
    (timbre/info "Stopping web server")
    (when web-server
      (web/stop web-server)
      (dissoc component :web-server))))

(defn new-web-server
  []
  (map->WebServer {}))

