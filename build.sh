lein do clean, test
lein uberjar
mkdir release/
cp .lein-env release/
cp target/service.jar release/

