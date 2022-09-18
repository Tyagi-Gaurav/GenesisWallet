package main

import (
	"log"
	"net"

	user "github.com/wallet/user"
	"google.golang.org/grpc"
)

const (
	port = ":50051"
)

//TODO Read port using flags

// type server struct {
// 	user.UnimplementedUserServer
// }

// func (s *server) AddUser(ctx context.Context, usr *user.UserGrpcRequestDTO) (*user.UserID, error) {
// 	log.Printf("Received: %v", usr.GetFirstname())
// 	user.AddUser()
// 	return &user.UserID{Value: uuid.New().String()}, nil
// }

func main() {
	lis, err := net.Listen("tcp", port)

	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}

	s := grpc.NewServer()
	user.RegisterUserServer(s, &user.Server{})

	log.Printf("Server listening at %v", lis.Addr())
	if err := s.Serve(lis); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}
}
