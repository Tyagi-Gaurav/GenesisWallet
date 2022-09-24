package service

import (
	"log"

	"github.com/google/uuid"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

var userMapByUserName map[string]*userInternal = make(map[string]*userInternal)
var userMapByUserId map[string]*userInternal = make(map[string]*userInternal)

type userInternal struct {
	id          uuid.UUID
	username    string
	firstName   string
	lastName    string
	password    string
	dateOfBirth string
	gender      string
	homeCountry string
	authorities []string
}

type AddUserRequest struct {
	Username    string
	FirstName   string
	LastName    string
	Password    string
	DateOfBirth string
	Gender      string
	HomeCountry string
	Authorities []string
}

type AddUserResponse struct {
	UserId string
}

type IUserService interface {
	AddUser(req *AddUserRequest) (*AddUserResponse, error)
}

type UserService struct{}

func NewUserService() *UserService {
	return &UserService{}
}

func (us *UserService) AddUser(req *AddUserRequest) (*AddUserResponse, error) {
	log.Println("Inside AddUser Service")
	userId, err := uuid.NewUUID()

	ui := &userInternal{
		id:          userId,
		username:    req.Username,
		firstName:   req.FirstName,
		lastName:    req.LastName,
		password:    req.Password,
		dateOfBirth: req.DateOfBirth,
	}

	if err != nil {
		return &AddUserResponse{}, status.Errorf(codes.Internal, "Error while Adding user: %v", err)
	}

	userMapByUserName[req.Username] = ui
	userMapByUserId[userId.String()] = ui

	return &AddUserResponse{UserId: userId.String()}, nil
}
