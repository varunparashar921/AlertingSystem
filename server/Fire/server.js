var http= require('http');
var express = require('express');
var mongoose = require('mongoose');
var bodyParser = require('body-parser');
mongoose.connect('mongodb://localhost:27017/fire', { useMongoClient: true });
var app = express();
var port = process.env.PORT || 8082;
app.use(express.json());
app.use(bodyParser.urlencoded({ extended: true })); // support encoded bodies
var db = mongoose.connection;
var models = require("./models")(mongoose);
var sockets=[]

// Data
app.get('/fire/getData', function (req, res) {
	var errorMsg = "Invalid Data";
	models.Alert.find(function (err, alert) {
	  console.log(alert);
	  var apiResponse = new models.ApiResponse(200);
	  if (err) {
		console.error(err);
		apiResponse.setError(errorMsg, new Error(err));
	  } else {
		apiResponse.data = alert;
	  }
	  res.send(apiResponse);
	});
})

// save Alert
app.post('/fire/saveAlert', function (req, res) {
	console.log("Sockets::"+sockets);
	var alertData = req.body;
	var originTime = alertData.originTime;
	var brightNess= alertData.brightNess;
	var satellite= alertData.satellite;
	var lat = alertData.lat;
	var lon = alertData.lon;

	var alert = new models.Alert({
		originTime: originTime,
		brightNess: brightNess,
		satellite: satellite,
		lat:lat,
		lon:lon
	});
	alert.save(function (err, result) {
		console.log(JSON.stringify(result));
		var apiResponse = new models.ApiResponse(200);
		if (err) {
			apiResponse.setError(message, new Error(err));
		} else {
			apiResponse.message = "Alert Succesfully Created";
			apiResponse.data = result;
		}
		res.send(apiResponse);
	});
})

var server = app.listen(port, function () {
    var host = server.address().address
    var port = server.address().port
  
    if (host = '::') {
      host = "localhost";
      console.log('Use IPv6 address (::) when IPv6 is available, or any IPv4 address (0.0.0.0) otherwise instead localhost');
    }
  
    console.log("Fire app listening at http://%s:%s", host, port)
})
