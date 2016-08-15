# trucker

A very simple geo server cache

The app minimizes number of calls to the bare minimum.

## Prerequisites

You have to install and run Redis server before running the app or tests

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
