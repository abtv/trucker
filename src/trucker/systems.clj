(ns trucker.systems
  (:require [com.stuartsierra.component :as component]
            [trucker.logger :refer [new-logger]]
            [trucker.web-server :refer [new-web-server]]))

(defn new-system []
  (-> (component/system-map
       :logger (new-logger)
       :web-server (new-web-server))
      (component/system-using
       {:logger     []
        :web-server [:logger]})))
