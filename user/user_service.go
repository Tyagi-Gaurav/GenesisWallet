package user

import (
	"github.com/google/uuid"
)

type UserInternal struct {
	id          uuid.UUID
	firstName   string
	lastName    string
	username    string
	password    string
	dateOfBirth string
	gender      string
	homeCountry string
	authorities []string
}

type server struct {
	userMap map[string]*UserInternal
}

func (s *server) AddUser(user User) UserID {
	return UserID{}
}
