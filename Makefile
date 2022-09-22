#$@ is the name of the target being generated, 
#$(@F) - Returns only the file portion of the value
#- at the beginning of a line ignores errors.

.PHONY: protogen

USERS := $(shell find user -type f -name '*.go')
GOCMD := go
BINARY_NAME := users_app

protogen:
	protoc --go-grpc_out=./user --go_out=./user ./user/proto/user_service.proto

#Download dependencies and put them into vendor
vendor:
	$(GOCMD) mod vendor

clean:
	-rm -f out/*
	-rmdir out

all: clean protogen 

all_images: all user_image api_gateway

all_compose: all compose 

user_image:
	docker build -f user/Dockerfile ./user

api_gateway:
	docker build -f api-gateway/Dockerfile ./api-gateway

compose:
	docker-compose up -d --build

compose-down:
	docker-compose down --rmi all

help:
	@echo ''
	@echo 'Available targets:'
	@echo ''
	@echo '  microservices		Prints a List of available services (JSON)'
	@echo '  omd-service		Prints the default service name for the repository'
	@echo ''
	@echo "Available Microservices:"
	@echo ''
	@for microservice in $(MICROSERVICES); do \
		echo "  - $${microservice}"; \
	done;
	@echo ''