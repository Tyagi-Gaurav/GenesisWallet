package main

import (
	"context"
	"log"
	"net"
	"net/http"
	"os"
	"os/signal"
	"strconv"

	user "github.com/wallet/user"
	"google.golang.org/grpc"
)

const (
	port                        = ":50051"
	serverMaxReceiveMessageSize = 1024 * 1024 * 1024 * 2
)

//TODO Read port using flags

// func (s *server) AddUser(ctx context.Context, usr *user.UserGrpcRequestDTO) (*user.UserID, error) {
// 	log.Printf("Received: %v", usr.GetFirstname())
// 	user.AddUser()
// 	return &user.UserID{Value: uuid.New().String()}, nil
// }

func main() {
	c := make(chan os.Signal, 1)
	signal.Notify(c, os.Interrupt)
	ctx, cancel := context.WithCancel(context.Background())

	log.Println("Starting GRPC & HTTP")
	go grpc_listen(ctx)
	go http_listen(ctx)
	log.Println("Started..")

	oscall := <-c //Listen on OS signal and then call cancelFunc provided by context
	log.Printf("system call:%+v", oscall)
	cancel() //This should cancel the go routines too.
}

func http_listen(ctx context.Context) {
	mux := http.NewServeMux()
	mux.Handle("/AddUser", http.HandlerFunc(user.AddUser))

	http.HandleFunc("/AddUser", user.AddUser)
	log.Fatal(http.ListenAndServe("0.0.0.0:"+strconv.Itoa(int(9090)), nil))

	<-ctx.Done()
	log.Printf("server stopped")
}

func grpc_listen(ctx context.Context) {
	lis, err := net.Listen("tcp", port)

	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}

	s := grpc.NewServer(grpc.MaxRecvMsgSize(serverMaxReceiveMessageSize))
	user.RegisterUserServer(s, &user.Server{})

	log.Printf("Server listening at %v", lis.Addr())
	if err := s.Serve(lis); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}

	<-ctx.Done()
	log.Printf("GRPC server stopped")
}
