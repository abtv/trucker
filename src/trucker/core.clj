(ns trucker.core
  (:require [reloaded.repl :refer [go]]
            [trucker.systems :refer [new-system]]
            [taoensso.timbre :as timbre])
  (:gen-class))

(defn -main [& args]
  (timbre/info "Starting service...")
  (reloaded.repl/set-init! new-system)
  (go))
