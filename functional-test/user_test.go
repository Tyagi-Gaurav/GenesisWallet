package main

import (
	"bytes"
	"crypto/tls"
	"crypto/x509"
	"encoding/json"
	"io/ioutil"
	"log"
	"net/http"
	"testing"

	"github.com/google/uuid"
	"golang.org/x/net/http2"
)

type TestUserGrpcRequestDTO struct {
	id          string
	firstname   string
	lastname    string
	username    string
	password    string
	dateOfBirth string
	//Gender      string
	homeCountry string
	authorities []string
}

func (userReq *TestUserGrpcRequestDTO) MarshalJSON() ([]byte, error) {
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
		Id:          userReq.id,
		Firstname:   userReq.firstname,
		Lastname:    userReq.lastname,
		Username:    userReq.username,
		Password:    userReq.password,
		DateOfBirth: userReq.dateOfBirth,
		HomeCountry: userReq.homeCountry,
		Authorities: userReq.authorities,
	})
}

func TestAddUser(t *testing.T) {
	caCert, err := ioutil.ReadFile("wallet.crt")
	if err != nil {
		log.Fatalf("Reading server certificate: %s", err)
	}
	caCertPool := x509.NewCertPool()
	caCertPool.AppendCertsFromPEM(caCert)

	// Create TLS configuration with the certificate of the server
	tlsConfig := &tls.Config{
		RootCAs: caCertPool,
	}
	tr := &http2.Transport{
		TLSClientConfig: tlsConfig,
	}
	request := &TestUserGrpcRequestDTO{
		id:          uuid.New().String(),
		firstname:   "TestFirstName",
		lastname:    "TestLastName",
		username:    "myUser",
		password:    "myPassword",
		dateOfBirth: "dateOfBirth",
		homeCountry: "home",
		authorities: []string{"a", "b"},
	}

	m, _ := json.Marshal(request)
	buf := bytes.NewBuffer(m)
	client := &http.Client{Transport: tr}
	endpoint := "https://localhost/user.User/AddUser"
	req, _ := http.NewRequest("POST", endpoint, buf)
	req.Header.Set("Content-Type", "application/grpc")
	req.Header.Set("TE", "trailers") //???
	req.Close = true
	_, err = client.Do(req)

	if err != nil {
		t.Error("Got error from POST: ", err)
	}
}
