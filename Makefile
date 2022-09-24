#$@ is the name of the target being generated, 
#$(@F) - Returns only the file portion of the value
#- at the beginning of a line ignores errors.

.PHONY: protogen api-gateway user

USERS := $(shell find user -type f -name '*.go')
GOCMD := go
BINARY_NAME := users_app
MICROSERVICES:= user api-gateway

protogen:
	protoc --go-grpc_out=./user --go_out=./user ./user/proto/user_service.proto

#Download dependencies and put them into vendor
vendor:
	$(GOCMD) mod vendor

clean:
	-rm -f out/*
	-rmdir out

pre-process: clean protogen 

user: pre-process
	docker build -f user/Dockerfile .

api-gateway:
	docker build -f api-gateway/Dockerfile ./api-gateway

compose:
	docker-compose up -d --build

compose-down:
	docker-compose down --rmi all

help:
	@echo ''
	@echo 'Available targets:'
	@echo ''
	@echo ' clean			Cleans all generated outputs'
	@echo ' protogen		Generates protobuf using all grpc proto files'
	@echo ' pre-process		clean and protogen'
	@echo ' user			Compiles user service with its image'
	@echo ' api-gateway		Compiles user service with its image'
	@echo ''
	@echo "Available Microservices:"
	@echo ''
	@for microservice in $(MICROSERVICES); do \
		echo "  - $${microservice}"; \
	done;
	@echo ''