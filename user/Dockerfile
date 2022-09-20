FROM golang:1.18-alpine AS builder

ADD . /src
WORKDIR /src
#go mod edit --replace github.com/wallet/user=./user/ && \
RUN go get github.com/google/uuid && \
	go get google.golang.org/protobuf/reflect/protoreflect && \
	go get google.golang.org/protobuf/runtime/protoimpl && \
	go get google.golang.org/grpc/codes && \
	go get google.golang.org/grpc/status && \
	go get golang.org/x/net/http2 && \
	go get golang.org/x/net/http2/hpack && \
	go get github.com/wallet/user

RUN go build user_service.go user_service_grpc.pb.go user_service.pb.go
RUN go build -o out/user_main main/user_server_main.go

FROM golang:1.18-alpine

WORKDIR /build
COPY --from=builder /src/out/user_main .

CMD ["/build/user_main"]