import "dotenv/config";
import express from "express";
import bodyParser from "body-parser";
import session from "express-session";
import passport from "passport";
import googleStrategy from "passport-google-oauth20";
import protoLoader from "@grpc/proto-loader";
import grpcLibrary from "@grpc/grpc-js";

var PROTO_PATH = process.env.PROTO_PATH;

const app = express();
const port = (process.env.PORT === undefined) ? 3000 : process.env.PORT;
const GoogleStrategy = googleStrategy.Strategy;

console.log("PROTO_DIR: " + PROTO_PATH);

app.use(
  session({
    secret: "My little secret.",
    resave: false,
    saveUninitialized: false,
  })
);

app.use(passport.initialize());
app.use(passport.session());

const packageDefinition = protoLoader.loadSync(PROTO_PATH, {
        keepCase: true,
        longs: String,
        enums: String,
        defaults: true,
        oneofs: true});

const userServiceObject = grpcLibrary.loadPackageDefinition(packageDefinition).com.gw.user.grpc;
const userServiceClient = new userServiceObject.UserService('localhost:19090', grpcLibrary.credentials.createInsecure());

console.log();

passport.serializeUser(function (user, cb) {
  process.nextTick(function () {
    return cb(null, {
      id: user.id,
      username: user.username,
      picture: user.picture, //Stores these in session
    });
  });
});

passport.deserializeUser(function (user, cb) {
  process.nextTick(function () {
    return cb(null, user);
  });
});

passport.use(
  new GoogleStrategy(
    {
      clientID: process.env.OAUTH_CLIENT_ID_GOOGLE,
      clientSecret: process.env.OAUTH_CLIENT_SECRET_GOOGLE,
      callbackURL: "http://localhost:3000/auth/google/secrets",
    },
    function (accessToken, refreshToken, profile, cb) {
      userServiceClient.createOrFindUser({
          userName: profile.id, 
          extsource: userServiceObject.ExternalSystem.type.value[1].name
        }, function(err, user) {
          console.log("Returned user: " + JSON.stringify(user));
          return cb(err, user);
        })
    }
  )
);

app.use(express.static("public"));
app.set("view engine", "ejs");
app.use(bodyParser.urlencoded({ extended: true }));

app.get("/", (req, resp) => {
  resp.render("home");
});

app.get("/auth/google",
  passport.authenticate("google", { scope: ["profile", "email"] })
);

app.get(
  "/auth/google/secrets",
  passport.authenticate("google", { failureRedirect: "/login" }),
  function (req, res) {
    // Successful authentication, redirect home.
    res.redirect("/secrets");
  }
);

app.get("/logout", (req, resp) => {
  req.logout(function (err) {
    if (err) {
      return next(err);
    }
    resp.redirect("/");
  });
});

app.get("/register", (req, resp) => {
  resp.render("register");
});

app.get("/secrets", (req, resp) => {
  resp.render("welcome.ejs", {
    initials: "GT"
  });
});

app.post("/register", (req, resp) => {
  User.register(
    { username: req.body.username },
    req.body.password,
    function (err, user) {
      if (err) {
        console.log(err);
        resp.redirect("/register");
      } else {
        passport.authenticate("local")(req, resp, function () {
          resp.redirect("/secrets");
        });
      }
    }
  );
});

app.get("/status", (req, res) => {
  res.status(200).send({"status" : "UP"});
});

app.get("/submit", (req, resp) => {
  if (req.isAuthenticated()) {
    resp.render("submit");
  } else {
    resp.redirect("/login");
  }
});

app.post("/submit", (req, resp) => {
  const submittedSecret = req.body.secret;
  console.log(req.user);
  User.findById(req.user.id).then(function (foundUser) {
    if (foundUser) {
      foundUser.secret = submittedSecret;
      foundUser.save().then(function (result) {
        if (result) {
          resp.redirect("/secrets");
        }
      }).catch(function (err) {
        console.log(err);
      });
    }
  });
});

app.get("/login", (req, resp) => {
  resp.render("login");
});

app.post("/login", (req, resp) => {
  const user = new User({
    username: req.body.username,
    password: req.body.password,
  });
  req.login(user, function (err) {
    if (err) {
      console.log(err);
    } else {
      passport.authenticate("local")(req, resp, function () {
        resp.redirect("/secrets");
      });
    }
  });
});

app.listen(port, () => {
  console.log(`Listening on port ${port}`);
});
