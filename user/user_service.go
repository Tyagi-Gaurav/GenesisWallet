package user

import (
	"github.com/google/uuid"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

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

type server struct {
	userMap map[string]*UserInternal
}

func (s *server) AddUser(req *UserGrpcRequestDTO) (UserID, error) {
	userId, err := uuid.NewUUID()

	if err != nil {
		return UserID{}, status.Errorf(codes.Internal, "Error while generating Product ID: %v", err)
	}

	if s.userMap == nil {
		s.userMap = make(map[string]*UserInternal)
	}

	ui := &UserInternal{
		Id:          userId,
		Username:    req.GetUsername(),
		FirstName:   req.GetFirstname(),
		LastName:    req.GetLastname(),
		Password:    req.GetPassword(),
		DateOfBirth: req.GetDateOfBirth(),
	}

	s.userMap[ui.Username] = ui

	return UserID{Value: userId.String()}, nil
}

func (s *server) GetUser(userID *FetchUserDetailsByIdGrpcRequestDTO) (*UserDetailsGrpcResponseDTO, error) {
	data, exists := s.userMap[userID.GetId()]

	if exists {
		return &UserDetailsGrpcResponseDTO{
			UserName:    data.Username,
			FirstName:   data.FirstName,
			LastName:    data.LastName,
			DateOfBirth: data.DateOfBirth,
			HomeCountry: data.HomeCountry,
		}, nil
	}

	return nil, status.Errorf(codes.NotFound, "User does not exist: %v", userID)
}
