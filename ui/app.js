import express from "express";
import bodyParser from "body-parser";
import protoLoader from "@grpc/proto-loader";
import grpcLibrary from "@grpc/grpc-js";

const app = express();
const port = 3000; //TODO Read this from environment config

const packageDefinition = protoLoader.loadSync(protoFileName, options);
const packageObject = grpcLibrary.loadPackageDefinition(packageDefinition);

app.use(bodyParser.urlencoded({ extended: true }));

app.listen(port, () => {
  console.log(`Listening on port ${port}`);
});

app.get("/", (req, res) => {
  res.render("home.ejs");
});

app.get("/register", (req, res) => {
  res.render("register.ejs");
});

app.post("/register", (req, res) => {
  //const response = axios.get("https://bored-api.appbrewery.com/random");
  res.render("secrets.ejs");
});