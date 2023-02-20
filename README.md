# websocket-service
module websocket in microservice


port service
server:
  port: ${WEBSOCKET_SVC_PORT:9092}
  
redis config

spring:
  application:
    name: websocket
  redis:
    host: localhost
    port: 6379
    timeout: 60000
    password:

topic message redis

redis-websocket:
  websocket-topic: test
