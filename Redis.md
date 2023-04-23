## redis-cli
### Starting redis server
```
redis-server
```

### Open CLI on redis
```
redis-cli
```

### Checking if key expires after TTL using SET
* Setting a key that expires in 20 seconds
```
SET mykey1 "myvalue" EX 20 
```
# After 20 seconds, following command should return nil
```
GET mykey1
```
