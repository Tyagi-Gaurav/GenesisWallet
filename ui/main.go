package main

import (
	"flag"
	"log"
)

func init() {
	log.SetPrefix("UI Server: ")
}

func main() {
	port := flag.Uint("port", 8080, "TCP port Number for UI server")

	flag.Parse()

	app := NewWalletUIServer(uint16(*port))

	app.Run()
}
