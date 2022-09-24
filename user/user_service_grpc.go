package user

import (
	context "context"
	"log"

	"github.com/wallet/user/service"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

type Server struct {
	UnimplementedUserServer
}

func (s *Server) AddUser(ctx context.Context, req *UserGrpcRequestDTO) (*UserID, error) {
	log.Println("Inside AddUser")

	ui := &service.AddUserRequest{
		Username:    req.GetUsername(),
		FirstName:   req.GetFirstname(),
		LastName:    req.GetLastname(),
		Password:    req.GetPassword(),
		DateOfBirth: req.GetDateOfBirth(),
	}

	resp, err := us.AddUser(ui)

	if err != nil {
		return &UserID{}, status.Errorf(codes.Internal, "Error while Adding user: %v", err)
	}

	return &UserID{Value: resp.UserId}, nil
}

// func (s *Server) FetchUsersById(ctx context.Context, userID *FetchUserDetailsByIdGrpcRequestDTO) (*UserDetailsGrpcResponseDTO, error) {
// 	log.Println("Inside FetchUsersById")
// 	data, exists := userMapByUserId[userID.GetId()]

// 	if exists {
// 		return &UserDetailsGrpcResponseDTO{
// 			UserName:    data.Username,
// 			FirstName:   data.FirstName,
// 			LastName:    data.LastName,
// 			DateOfBirth: data.DateOfBirth,
// 			HomeCountry: data.HomeCountry,
// 		}, nil
// 	}

// 	return nil, status.Errorf(codes.NotFound, "User does not exist: %v", data)
// }

// func (s *Server) FetchUsersByName(ctx context.Context, userID *FetchUserDetailsByNameGrpcRequestDTO) (*UserDetailsGrpcResponseDTO, error) {
// 	log.Println("Inside FetchUsersByName")
// 	data, exists := userMapByUserName[userID.GetName()]

// 	if exists {
// 		return &UserDetailsGrpcResponseDTO{
// 			UserName:    data.Username,
// 			FirstName:   data.FirstName,
// 			LastName:    data.LastName,
// 			DateOfBirth: data.DateOfBirth,
// 			HomeCountry: data.HomeCountry,
// 		}, nil
// 	}

// 	return nil, status.Errorf(codes.NotFound, "User does not exist: %v", data)
// }

// func (s *Server) LoginWithPassword(ctx context.Context, in *LoginGrpcRequestDTO) (*LoginGrpcResponseDTO, error) {
// 	log.Println("Inside LoginWithPassword")
// 	data, exists := userMapByUserName[in.GetUsername()]

// 	if exists {
// 		return &LoginGrpcResponseDTO{
// 			Userid: data.Id.String(),
// 			Token:  uuid.New().String(),
// 		}, nil
// 	}

// 	return nil, status.Errorf(codes.NotFound, "User does not exist: %v", data)
// }
