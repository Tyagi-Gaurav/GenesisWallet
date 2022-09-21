package main

import (
	"context"
	"log"
	"time"

	"github.com/google/uuid"
	user "github.com/wallet/user"
	"google.golang.org/grpc"
)

const (
	address = "localhost:50051"
)

func main() {
	conn, err := grpc.Dial(address, grpc.WithInsecure())

	if err != nil {
		log.Fatalf("did not connect: %v", err)
	}
	defer conn.Close()
	user_client := user.NewUserClient(conn)

	ctx, cancel := context.WithTimeout(context.Background(), time.Second)
	defer cancel()

	r, err := user_client.AddUser(ctx, &user.UserGrpcRequestDTO{
		Id:          uuid.New().String(),
		Firstname:   "TestFirstName",
		Lastname:    "TestLastName",
		Username:    "myUser",
		Password:    "myPassword",
		DateOfBirth: "dateOfBirth",
		Gender:      user.Gender_GENDER_MALE,
		HomeCountry: "home",
		Authorities: []string{"a", "b"},
	})

	if err != nil {
		log.Fatalf("Could not add user: %v", err)
	}
	log.Printf("User added succeessfully with Id: %v", r.Value)

	user, err := user_client.FetchUsersById(ctx, &user.FetchUserDetailsByIdGrpcRequestDTO{
		Id: r.Value,
	})

	if err != nil {
		log.Fatalf("Could not get user: %v", err)
	}
	log.Printf("User: ", user)
}
