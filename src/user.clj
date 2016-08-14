(ns user
  (:require [reloaded.repl :refer [system init start stop go reset]]
            [trucker.systems :refer [new-system]]))

(reloaded.repl/set-init! new-system)
