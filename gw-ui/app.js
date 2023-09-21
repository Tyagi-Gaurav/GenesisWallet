import express from "express";
import bodyParser from "body-parser";

const app = express();
const port = 3000; //TODO Read this from environment config

app.listen(port, () => {
  console.log(`Listening on port ${port}`);
});