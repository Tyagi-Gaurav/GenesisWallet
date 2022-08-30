package main

import (
	"log"
	"net/http"
	"path"
	"strconv"
	"text/template"
)

const template_dir = "templates"

type WalletUIServer struct {
	port uint16
}

func (ws *WalletUIServer) Port() uint16 {
	return ws.port
}

func NewWalletUIServer(port uint16) *WalletUIServer {
	return &WalletUIServer{port}
}

func (ws *WalletUIServer) Index(w http.ResponseWriter, req *http.Request) {
	switch req.Method {
	case http.MethodGet:
		t, _ := template.ParseFiles(path.Join(template_dir, "index.html"))
		t.Execute(w, "")
	default:
		log.Printf("Error: Invalid HTTP method")
	}
}

func (ws *WalletUIServer) Run() {
	http.HandleFunc("/", ws.Index)
	log.Fatal(http.ListenAndServe("0.0.0.0:"+strconv.Itoa(int(ws.Port())), nil))
}
