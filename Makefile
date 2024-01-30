#$@ is the name of the target being generated, 
#$(@F) - Returns only the file portion of the value
#- at the beginning of a line ignores errors.

.PHONY: protogen api-gateway user full 

MICROSERVICES:= user api-gateway ui

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
	./scripts/full_local_build.sh

fq:
	./scripts/full_local_build.sh -t

down:
	docker-compose down --rmi all

qr: 
	./mvnw clean package -DskipTests=true
	docker-compose --env-file ./ui/.env  up -d --build

ft:
	./mvnw test -DskipTests=false -pl functional-test -Dtest=CucumberTest
	( cd ui-functional-test && npm test )

smoke:
	./mvnw test -DskipTests=false -pl functional-test -Dtest=SmokeTest

open_ui:
	open http://localhost:3000

node_base_image:
	./baseImages/createImage.sh nodejs

java_base_image:
	./baseImages/createImage.sh nodejs

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
	@echo ' qr					Restart docker-compose stack after a small change to any app'
	@echo ' open_ui				Open UI'
	@echo ' node_base_image		Create new Nodejs base images'
	@echo ' java_base_image		Create new Java base images'
	@echo ''
	@echo "Available Microservices:"
	@echo ''
	@for microservice in $(MICROSERVICES); do \
		echo "  - $${microservice}"; \
	done;
	@echo ''