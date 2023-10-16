
import "dotenv/config";
import express from "express";
import bodyParser from "body-parser";
import protoLoader from "@grpc/proto-loader";
import grpcLibrary from "@grpc/grpc-js";

var PROTO_PATH = process.env.PROTO_PATH;

const app = express();
const port = (process.env.PORT === undefined) ? 3000 : process.env.PORT;

const packageDefinition = protoLoader.loadSync(PROTO_PATH, {
        keepCase: true,
        longs: String,
        enums: String,
        defaults: true,
        oneofs: true});

const userServiceObject = grpcLibrary.loadPackageDefinition(packageDefinition).com.gw.user.grpc;
var userServiceClient = new userServiceObject.UserService('localhost:19090', grpcLibrary.credentials.createInsecure());

app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.static("public"));

app.listen(port, () => {
  console.log(`Listening on port ${port}`);
});

app.get("/", (req, res) => {
  res.render("home.ejs");
});

app.get("/register", (req, res) => {
  res.render("register.ejs");
});

app.get("/login", (req, res) => {
  res.render("login.ejs");
});

app.get("/status", (req, res) => {
  res.status(200).send({"status" : "UP"});
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
      //TODO Set error flag
      res.render("register.ejs");
    } else {
      console.log("External User created result : " + JSON.stringify(feature));
      res.render("login.ejs");
    }
  });
});

app.post("/login", (req, res) => {
  userServiceClient.authenticate({
    userName: req.body.username,
    password: req.body.password
  }, function(err, result) {
    if (err) {
      console.log(err);
      //TODO Some error of Internal error
    } else {
      console.log("Result: " + JSON.stringify(result));
      if (result.either === "error") {
        //TODO Set error flag
        res.render("login.ejs");
      } else {
        var initials = result.authDetails.firstName.charAt(0).toUpperCase() + result.authDetails.lastName.charAt(0).toUpperCase();;
        res.render("welcome.ejs", {
          initials: initials
        });
        //TODO Some error of Invalid Credentials?
      }
    }
  });
});