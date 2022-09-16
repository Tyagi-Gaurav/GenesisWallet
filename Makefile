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

build:
	cd user; go mod vendor; mkdir -p out/bin ; GO111MODULE=on $(GOCMD) build -mod vendor -o out/bin/$(BINARY_NAME) $(USERS)

user_app:
	go mod edit --replace github.com/wallet/user=./user/
	go get github.com/google/uuid
	go get google.golang.org/protobuf/reflect/protoreflect
	go get google.golang.org/protobuf/runtime/protoimpl
	go get google.golang.org/grpc/codes
	go get google.golang.org/grpc/status
	go get golang.org/x/net/http2
	go get golang.org/x/net/http2/hpack
	go get github.com/wallet/user
	$(GOCMD) build user/user_service.go user/user_service_grpc.pb.go user/user_service.pb.go

user_main: 
	$(GOCMD) build -o out/user_main user_server_main.go

clean:
	-rm -f out/*
	-rmdir out

all: clean protogen user_app user_main