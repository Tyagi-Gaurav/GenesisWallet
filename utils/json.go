package utils

import (
	"encoding/json"
	"io"
	"net/http"
)

func JsonStatus(message string) []byte {
	m, _ := json.Marshal(struct {
		Message string `json:"message"`
	}{
		Message: message,
	})
	return m
}

func JsonWith(w http.ResponseWriter, statusCode int, respBody string) {
	w.Header().Add("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	io.WriteString(w, respBody)
}
