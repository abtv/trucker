(ns trucker.systems
  (:require [com.stuartsierra.component :as component]
            [trucker.web-server :refer [new-web-server]]))

(defn new-system []
  (-> (component/system-map
       :web-server (new-web-server))
      (component/system-using
       {:web-server []})))
