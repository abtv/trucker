(defproject trucker "0.1.0-SNAPSHOT"
  :description "simple geocaching service"
  :url "http://github.com/abtv/trucker"
  :min-lein-version "2.0.0"
  :uberjar-name "service.jar"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"]
                 [ring/ring-json "0.4.0"]
                 [com.stuartsierra/component "0.3.1"]
                 [reloaded.repl "0.2.2"]
                 [com.taoensso/carmine "2.14.0"]
                 [http-kit "2.1.18"]
                 [org.immutant/immutant "2.1.2"]
                 [environ "1.0.2"]
                 [ring/ring-codec "1.0.1"]
                 [com.taoensso/timbre "4.3.1"]]

  :plugins [[lein-environ "1.0.2"]]
  :ring {:handler trucker.handler/app}

  :profiles {:dev     {:env {:http-host            "0.0.0.0"
                             :http-port            "3000"
                             :redis-host           "127.0.0.1"
                             :redis-port           "6379"
                             :external-service-url "http://google.com"
                             :primary-cache-ttl-s  "10"}}
             :uberjar {:main trucker.core
                       :aot  [trucker.core]
                       :env  {:http-host            "0.0.0.0"
                              :http-port            "3000"
                              :redis-host           "127.0.0.1"
                              :redis-port           "6379"
                              :external-service-url "http://google.com"
                              :primary-cache-ttl-s  "86400"}}})

