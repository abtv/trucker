(ns trucker.logger
  (:require [com.stuartsierra.component :as component]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.3rd-party.rotor :refer [rotor-appender]]))

(defrecord Logger []
  component/Lifecycle
  (start [component]
    (timbre/merge-config! {:appenders {:rotor (rotor-appender {:path     "trucker.log"
                                                               :max-size (* 1024 1024)
                                                               :backlog  10})}}))
  (stop [component]))

(defn new-logger []
  (map->Logger {}))

