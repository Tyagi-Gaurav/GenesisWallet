#$@ is the name of the target being generated, 
#$(@F) - Returns only the file portion of the value
#- at the beginning of a line ignores errors.

.PHONY: protogen api-gateway user full

MICROSERVICES:= user api-gateway ui

#protogen:
#	protoc --go-grpc_out=./user --go_out=./user ./user/proto/user_service.proto

mcp:
	./mvnw clean package

mci:
	./mvnw clean install

mcpst:
	./mvnw clean package -DskipTests=true

gw-user:
	docker build -f gw-user/Dockerfile .

api-gateway:
	docker build -f api-gateway/Dockerfile ./api-gateway

full:
	./full_local_build.sh

fq:
	./full_local_build.sh -t

down:
	docker-compose down --rmi all

ft:
	./mvnw test -DskipTests=false -pl api-functional-test -Dtest=CucumberTest
	./mvnw test -DskipTests=false -pl ui-functional-test

smoke:
	./mvnw test -DskipTests=false -pl api-functional-test -Dtest=SmokeTest

help:
	@echo ''
	@echo 'Available targets:'
	@echo ''
	@echo ' mcp					mcn clean package'
	@echo ' mci					mvn clean install'
	@echo ' mcpst				mcp and skip test'
	@echo ' gw-user				Build user image'
	@echo ' api-gateway			Build API gateway image'
	@echo ' full				Build full local docker-compose stack'
	@echo ' fq					Build full local docker-compose stack (No tests)'
	@echo ' ft					Run functional tests'
	@echo ' down				Destroy local docker-compose stack'
	@echo ''
	@echo "Available Microservices:"
	@echo ''
	@for microservice in $(MICROSERVICES); do \
		echo "  - $${microservice}"; \
	done;
	@echo ''