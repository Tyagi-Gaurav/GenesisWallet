package user

import (
	"encoding/json"
	"io"
	"log"
	"net/http"

	"github.com/wallet/user/service"
	"github.com/wallet/utils"
)

type UserAddRequestDTO struct {
	Firstname   string   `json:"firstName"`
	Lastname    string   `json:"lastName"`
	Username    string   `json:"userName"`
	Password    string   `json:"password"`
	DateOfBirth string   `json:"dateOfBirth"`
	Gender      string   `json:"gender"`
	HomeCountry string   `json:"homeCountry"`
	Authorities []string `json:"authorities"`
}

type UserAddResponseDTO struct {
	UserId string `json:"userId"`
}

func (uar *UserAddResponseDTO) MarshalJSON() ([]byte, error) {
	return json.Marshal(struct {
		UserId string `json:"userId"`
	}{
		UserId: uar.UserId,
	})
}

var us = service.NewUserService()

func AddUser(w http.ResponseWriter, req *http.Request) {
	switch req.Method {
	case http.MethodPost:
		log.Println("Got User Add request")
		decoder := json.NewDecoder(req.Body)
		var req UserAddRequestDTO
		err := decoder.Decode(&req)

		if err != nil {
			log.Printf("ERROR: %v", err)
			io.WriteString(w, string(utils.JsonStatus("fail")))
			return
		}

		ur := &service.AddUserRequest{
			Username:    req.Username,
			FirstName:   req.Firstname,
			LastName:    req.Lastname,
			Password:    req.Password,
			DateOfBirth: req.DateOfBirth,
			HomeCountry: req.HomeCountry,
		}

		resp, err := us.AddUser(ur)

		if err != nil {
			log.Printf("ERROR: %v", err)
			io.WriteString(w, string(utils.JsonStatus("fail")))
			return
		}

		uar := &UserAddResponseDTO{resp.UserId}
		m, _ := uar.MarshalJSON()
		utils.JsonWith(w, http.StatusOK, string(m))
	default:
		log.Printf("Error: Invalid HTTP method")
	}
}
