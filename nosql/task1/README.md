Task 1

Add in `/etc/hosts`
```text
# For mongo cluster purpose
127.0.0.1 mongo1 mongo2 mongo3
```

```sh
docker-compose up -d
```

```sh
docker exec -ti mongo1 sh /scripts/mongo_setup.sh
```
ready
```sh
docker-compose down
```