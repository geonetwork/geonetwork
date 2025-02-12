# Version 5.0.0

## Build GeoNetwork 5 docker image

```shell
(cd geonetwork-frontend/; npm install; npm run build-all; cd..)
./mvnw clean install -Drelax
(cd src/apps/geonetwork; ../../../mvnw spring-boot:build-image)
```

## Build GeoNetwork 4

Generated GeoNetwork 4 webapp and copy it in the current directory, name it `geonetwork.war`

```shell
cp ../../core-geonetwork/web/target/geonetwork.war .
```

## Run GeoNetwork

Run the docker-composition from the current directory:

```shell
docker compose -f docker-compose.yml -f docker-compose.dev.yml up --build
```

Open http://localhost:7979/geonetwork


## Database dump

```shell
docker compose exec database pg_dump -U geonetwork -d geonetwork -Fc > docker-entrypoint-initdb.d/dump
```

## Manual configuration

* Sign in with `admin`/`admin`
* Configure the port in GeoNetwork 4 in admin console (set it to 7979)
