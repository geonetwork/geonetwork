sleep 5
 curl -XPUT http://localhost:9200/_snapshot/my_backup/snapshot_1/_restore -H 'Content-Type: application/json' -d @/tmp/import-elastic-repo.json
