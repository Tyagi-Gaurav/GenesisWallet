package user

import (
	context "context"

	"github.com/google/uuid"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

var userMapByUserName map[string]*UserInternal = make(map[string]*UserInternal)
var userMapByUserId map[string]*UserInternal = make(map[string]*UserInternal)

type UserInternal struct {
	Id          uuid.UUID
	Username    string
	FirstName   string
	LastName    string
	Password    string
	DateOfBirth string
	Gender      string
	HomeCountry string
	Authorities []string
}

type Server struct {
	UnimplementedUserServer
}

func (s *Server) AddUser(ctx context.Context, req *UserGrpcRequestDTO) (*UserID, error) {
	userId, err := uuid.NewUUID()

	if err != nil {
		return &UserID{}, status.Errorf(codes.Internal, "Error while generating Product ID: %v", err)
	}

	ui := &UserInternal{
		Id:          userId,
		Username:    req.GetUsername(),
		FirstName:   req.GetFirstname(),
		LastName:    req.GetLastname(),
		Password:    req.GetPassword(),
		DateOfBirth: req.GetDateOfBirth(),
	}

	userMapByUserName[ui.Username] = ui
	userMapByUserId[ui.Id.String()] = ui

	return &UserID{Value: userId.String()}, nil
}

func (s *Server) FetchUsersById(ctx context.Context, userID *FetchUserDetailsByIdGrpcRequestDTO) (*UserDetailsGrpcResponseDTO, error) {
	data, exists := userMapByUserId[userID.GetId()]

	if exists {
		return &UserDetailsGrpcResponseDTO{
			UserName:    data.Username,
			FirstName:   data.FirstName,
			LastName:    data.LastName,
			DateOfBirth: data.DateOfBirth,
			HomeCountry: data.HomeCountry,
		}, nil
	}

	return nil, status.Errorf(codes.NotFound, "User does not exist: %v", data)
}

func (s *Server) FetchUsersByName(ctx context.Context, userID *FetchUserDetailsByNameGrpcRequestDTO) (*UserDetailsGrpcResponseDTO, error) {
	data, exists := userMapByUserName[userID.GetName()]

	if exists {
		return &UserDetailsGrpcResponseDTO{
			UserName:    data.Username,
			FirstName:   data.FirstName,
			LastName:    data.LastName,
			DateOfBirth: data.DateOfBirth,
			HomeCountry: data.HomeCountry,
		}, nil
	}

	return nil, status.Errorf(codes.NotFound, "User does not exist: %v", data)
}

func (s *Server) LoginWithPassword(ctx context.Context, in *LoginGrpcRequestDTO) (*LoginGrpcResponseDTO, error) {
	data, exists := userMapByUserName[in.GetUsername()]

	if exists {
		return &LoginGrpcResponseDTO{
			Userid: data.Id.String(),
			Token:  uuid.New().String(),
		}, nil
	}

	return nil, status.Errorf(codes.NotFound, "User does not exist: %v", data)
}
