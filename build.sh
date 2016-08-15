lein do clean, test
lein uberjar
rm -rf release
mkdir release/
cp .lein-env release/
cp target/service.jar release/

