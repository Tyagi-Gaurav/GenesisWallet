package main

import (
	"bytes"
	"crypto/tls"
	"encoding/json"
	"io"
	"log"
	"net/http"
	"testing"
)

type TestUserAddRequestDTO struct {
	Firstname   string   `json:"firstName"`
	Lastname    string   `json:"lastName"`
	Username    string   `json:"userName"`
	Password    string   `json:"password"`
	DateOfBirth string   `json:"dateOfBirth"`
	Gender      string   `json:"gender"`
	HomeCountry string   `json:"homeCountry"`
	Authorities []string `json:"authorities"`
}

type TestUserAddResponse struct {
	UserId string `json:"userId"`
}

func (userReq *TestUserAddRequestDTO) MarshalJSON() ([]byte, error) {
	return json.Marshal(struct {
		Id          string   `json:"id"`
		Firstname   string   `json:"firstname"`
		Lastname    string   `json:"lastname"`
		Username    string   `json:"username"`
		Password    string   `json:"password"`
		DateOfBirth string   `json:"dateOfBirth"`
		HomeCountry string   `json:"homeCountry"`
		Authorities []string `json:"authorities"`
	}{
		Firstname:   userReq.Firstname,
		Lastname:    userReq.Lastname,
		Username:    userReq.Username,
		Password:    userReq.Password,
		DateOfBirth: userReq.DateOfBirth,
		HomeCountry: userReq.HomeCountry,
		Authorities: userReq.Authorities,
	})
}

func TestAddUser(t *testing.T) {
	// Create TLS configuration with the certificate of the server
	tlsConfig := &tls.Config{
		//RootCAs: caCertPool,
		InsecureSkipVerify: true,
	}
	tr := &http.Transport{
		TLSClientConfig: tlsConfig,
	}
	request := &TestUserAddRequestDTO{
		Firstname:   "TestFirstName",
		Lastname:    "TestLastName",
		Username:    "myUser",
		Password:    "myPassword",
		DateOfBirth: "dateOfBirth",
		HomeCountry: "home",
		Authorities: []string{"a", "b"},
	}

	m, _ := request.MarshalJSON()
	buf := bytes.NewBuffer(m)
	client := &http.Client{Transport: tr}
	endpoint := "https://localhost/user/AddUser"
	req, _ := http.NewRequest("POST", endpoint, buf)
	req.Header.Set("Content-Type", "application/json")
	req.Close = true
	resp, err := client.Do(req)

	if err != nil {
		t.Error("Got error from POST: ", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		t.Error("Got non-ok response code from POST: ", resp.StatusCode)
	}

	bodyBytes, err := io.ReadAll(resp.Body)
	if err != nil {
		log.Fatal(err)
	}
	bodyString := string(bodyBytes)

	log.Println(bodyString)
}
