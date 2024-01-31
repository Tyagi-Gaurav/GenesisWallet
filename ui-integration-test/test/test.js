"use strict";
process.env.NODE_ENV = "test";

var serverHost = "http://localhost:3000";

import chai from "chai";
import chaiHttp from "chai-http";

chai.should();
chai.use(chaiHttp);

describe("Main Page", () => {
  describe("/GET ", () => {
    it("it should show home page", (done) => {
      chai
        .request(serverHost)
        .get("/")
        .end((err, res) => {
          res.should.have.status(200);
          done();
        });
    });
  });

  describe("/login ", () => {
    it("it should show login page", (done) => {
      chai
        .request(serverHost)
        .get("/login")
        .end((err, res) => {
          res.should.have.status(200);
          done();
        });
    });
  });

  describe("/register ", () => {
    it("it should show register page", (done) => {
      chai
        .request(serverHost)
        .get("/register")
        .end((err, res) => {
          res.should.have.status(200);
          done();
        });
    });
  });

  describe("/status ", () => {
    it("it should show status page", (done) => {
      chai
        .request(serverHost)
        .get("/status")
        .end((err, res) => {
          res.should.have.status(200);
          done();
        });
    });
  });
});
