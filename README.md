# trucker

A very simple app cache: the app minimizes number of calls to an external service to the bare minimum.

The app uses 2 caches:
- primary cache: Redis cache to store key-value pairs for 24 hours and then expired key-value pairs
- secondary cache: clojure cache (atom) to store pending requests to an external service, which minimizes number of calls to the external service

The app exposes GET /geocode?address=some%20address endpoint, makes calls to an external service and returns results using cached data when possible.

## Prerequisites

You have to install and run Redis server before running the app or tests

wget http://download.redis.io/redis-stable.tar.gz
tar xzf redis-stable.tar.gz
cd redis-stable
make
sudo make install
redis-server

## Running

To start a web server for the application, run:

    lein repl
    (go)

## Testing

To test the app, run

    lein test

## Building

To build and run the app in release mode, run

   ./build.sh
   cd target
   java -jar service.jar

## License

Copyright © 2016 abtv
