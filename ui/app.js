import express from "express";
import bodyParser from "body-parser";
import protoLoader from "@grpc/proto-loader";
import grpcLibrary from "@grpc/grpc-js";

var PROTO_PATH = "../proto/UserService.proto";

const app = express();
const port = 3000; //TODO Read this from environment config

const packageDefinition = protoLoader.loadSync(PROTO_PATH, {
        keepCase: true,
        longs: String,
        enums: String,
        defaults: true,
        oneofs: true});

console.log("Package Definition: " + packageDefinition);

const userServiceObject = grpcLibrary.loadPackageDefinition(packageDefinition).com.gw.user.grpc;
var userServiceClient = new userServiceObject.UserService('localhost:19090', grpcLibrary.credentials.createInsecure());

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
  userServiceClient.createUser({
    userName : req.body.username,
    password : req.body.password,
    firstName : req.body.firstName,
    lastName : req.body.lastName
  }, function(err, feature) {
    if (err) {
      console.log("Error occurred: " + err);
    } else {
      console.log("External User created result : " + JSON.stringify(feature));
    }
  });
  
  res.render("welcome.ejs");
});
