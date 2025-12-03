
HOW TO DUMP AND CREATE CONTAINERS
=================================

```
HIGHLY TECHNICAL AND IN NOTES FORM!!!

INFORMATION ON HOW TO CREATE NEW POSTGRESQL 
AND ELASTIC DUMPS.

YOU ONLY NEED TO READ THIS IF YOU WANT TO 
SAVE-AND-UPDATE THE SAMPLE DATA!
```



We need two docker containers;

1. postgresql (with loaded data)
2. elastic (with indexed data)
 



By-Hand Building (i.e. for upgrades)
------------------------------------

TODO: this should be automated

Get Running
%%%%%%%%%%%

1. run a docker postgresql (with the sample data injected)
   RUN FROM `docker/` (where `dump.gn.sql` is)
```
docker rm postgresql-gn5
docker run -d -e POSTGRES_DB=geonetwork -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -p 5555:5432  --name postgresql-gn5 -v `pwd`/dump.gn.sql:/docker-entrypoint-initdb.d/dump.gn.sql   postgres:16-alpine
```

2. run elastic
```
docker run --name gn5_demo-elasticsearch-1  -p 9200:9200 -p 9300:9300 -e "path.repo=/tmp" -e ES_JAVA_OPTS="-Xms750m -Xmx2g" -e "discovery.type=single-node" -e "xpack.security.enabled=false" -e "xpack.security.enrollment.enabled=false" docker.elastic.co/elasticsearch/elasticsearch:8.14.0
```


3. gn4 (from source)

Likely need to sign on as admin, then admin tools -> delete and reindex.

```
cd src/web
mvn jetty:run jetty:run -Dgeonetwork.db.type=postgres-postgis -Djdbc.database=geonetwork -Djdbc.username=postgres -Djdbc.password=postgres -Djdbc.host=localhost -Ddb.port=5555 -Djdbc.port=5555 -Djetty.port=8080
```  

4. validate working in gui

Dump Data
--------

Postgresql:

```
pg_dump -h localhost -p 5555 -U postgres --column-inserts geonetwork > dump.gn.sql
```

`pg_dump` version miss-match?  Use:
``` 
brew install postgresql@16
brew link --force --overwrite postgresql@16
```

Elastic - create dump
=====================

startup:
```
docker run --name gn5_demo-elasticsearch-1  -p 9200:9200 -p 9300:9300 -e "path.repo=/tmp" -e ES_JAVA_OPTS="-Xms750m -Xmx2g" -e "discovery.type=single-node" -e "xpack.security.enabled=false" -e "xpack.security.enrollment.enabled=false" docker.elastic.co/elasticsearch/elasticsearch:8.14.0
```

 

Run GN4 and admin->tools->delete index/re-index
* this will populate the Elastic index
* Verify by going to:
  http://localhost:9200/gn-records
  http://localhost:9200/gn-records/_search?pretty=true&q=*:*  (show records)

Create backup repo:
```
curl -XPUT -d '
{
"type": "fs",
"settings": {
"location": "/tmp/es_backups",
"compress": true
}
}' -H 'Content-Type: application/json' "http://localhost:9200/_snapshot/my_backup"
```


delete any existing repo:
```
curl -XDELETE "http://localhost:9200/_snapshot/my_backup"
```


delete any existing snapshot:
```
curl -XDELETE "http://localhost:9200/_snapshot/my_backup/snapshot_1"
```

Create actual backup:
```
curl -XPUT -d '
{
"indices": "gn-records",
"ignore_unavailable": true,
"include_global_state": false
}' -H 'Content-Type: application/json' "http://localhost:9200/_snapshot/my_backup/snapshot_1?wait_for_completion=true"
```

TAR up the backup:

```
docker exec -it gn5_demo-elasticsearch-1 bash

cd /tmp
tar cvfz es_backups.tar.gz es_backups/
```

download the backup

```
docker cp gn5_demo-elasticsearch-1:/tmp/es_backups.tar.gz .
```

Restore
=======

NOTE: order is imporant - repo must be created AFTER snapshot is put (decompressed) into the container

restart elastic (it will be empty)
```
docker kill gn5_demo-elasticsearch-1
docker rm gn5_demo-elasticsearch-1
docker run --name gn5_demo-elasticsearch-1  -p 9200:9200 -p 9300:9300 -e "path.repo=/tmp" -e ES_JAVA_OPTS="-Xms750m -Xmx2g" -e "discovery.type=single-node" -e "xpack.security.enabled=false" -e "xpack.security.enrollment.enabled=false" docker.elastic.co/elasticsearch/elasticsearch:8.14.0
```

copy old snapshot/backup to elastic container and un-tar it:
```
docker cp  es_backups.tar.gz gn5_demo-elasticsearch-1:/tmp/es_backups.tar.gz
docker exec   -w /tmp gn5_demo-elasticsearch-1 tar -xvzf es_backups.tar.gz
```

Create backup repo:
```
curl -XPUT -d '
{
"type": "fs",
"settings": {
"location": "/tmp/es_backups",
"compress": true
}
}' -H 'Content-Type: application/json' "http://localhost:9200/_snapshot/my_backup"
```



load snapshot into elastic
```
curl -XPOST -d '
{
  "indices": "*",
  "ignore_unavailable": true,
  "include_global_state": false
}' -H 'Content-Type: application/json' "http://localhost:9200/_snapshot/my_backup/snapshot_1/_restore"
```



Helpful Info
============

http://localhost:7979/ogcapi-records<br>
http://localhost<br>
http://localhost:9200/gn-records <br>
[http://localhost:9200/gn-records/_search?pretty=true&q=\*:\*&size=500](http://localhost:9200/gn-records/_search?pretty=true&q=*:*&size=500)<br>
`docker exec -it gn5_demo-elasticsearch-1 bash`<br>
