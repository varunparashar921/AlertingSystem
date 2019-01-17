var express = require('express');
var mongoose = require('mongoose');
var bodyParser = require('body-parser');
mongoose.connect('mongodb://localhost:27017/alertingsystem', { useMongoClient: true });
var firebase = require("./firebase");
var mail = require("./mail");
var app = express();
var twilio = require('twilio');
var port = process.env.PORT || 8080;
var accountSid = 'AC4e8b95fdcc6110a8d8ea8a73759c2d19'; // Your Account SID from www.twilio.com/console
var authToken = 'd4b1e10bc5c40d0bbef9a10bcdc8ef1f';
app.use(express.json());
app.use(bodyParser.urlencoded({ extended: true })); // support encoded bodies
var db = mongoose.connection;
var models = require("./models")(mongoose);

db.on('connected', function () {
    console.log('Mongoose alertingsystem connection opend');
})
  
// If the connection throws an error
db.on('error', function (err) {
    console.log('Mongoose alertingsystem connection error: ' + err);
})
  
// When the connection is disconnected
db.on('disconnected', function () {
    console.log('Mongoose alertingsystem connection disconnected');
})

// ----START----   API call 

// Create User
app.post('/alertingsystem/createUser', function (req, res) {
    var userData = req.body;
  
    var firstName = userData.firstName;
    var lastName = userData.lastName;
    var email = userData.email;
    var password = userData.password;
    var gender = userData.gender;
    var phNo = userData.phNo;
    var lat = userData.lat;
    var lon = userData.lon;
    var deviceName = userData.deviceName;
    var deviceOS = userData.deviceOS;
    var deviceToken = userData.deviceToken;

    var user = new models.User({
      email: email,
      firstName: firstName,
      lastName: lastName,
      password: password,
      gender: gender,
      phNo: phNo,
      lat: lat,
      lon: lon,
      deviceToken: deviceToken,
      deviceName: deviceName,
      deviceOS: deviceOS
    });
 
    user.save(function (err, result) {
      var apiResponse = new models.ApiResponse(200);
      if (err) {
        console.error(err);
        apiResponse.message = "Invalid User, User Already registered";
        apiResponse.error = new Error(err);
      } else {
        apiResponse.data = result;
      }
      console.log("ApiResponse:" + JSON.stringify(apiResponse));
      res.send(apiResponse);
    });
})

//change password
app.post('/alertingsystem/changeLocation', function (req, res) {
  var userData = req.body;
  var userId = userData.userId;
  var lat = userData.lat;
  var lon = userData.lon;
  var message = 'Invalid Location';
  var options = {
    $set: {
      lat: lat,
      lon: lon
    }
  };
  var apiResponse = new models.ApiResponse(200);
  models.User.findByIdAndUpdate(userId, options, function (err, user) {
    console.log(JSON.stringify(user));
    if (err) {
      apiResponse.setError(message, new Error(err));
    } else {
      apiResponse.message = "Location Succesfully Updated";
    }
    res.send(apiResponse);
  });
});

function distance(lat1, lng1,lat2,lng2) {
  console.log("Diff:"+(lng1 - lng2))
  var a = (lat1 - lat2) * distPerLat(lat1)
  var b = (lng1 - lng2) * distPerLng(lat1)
  return Math.sqrt(a * a + b * b)/1000
}

function distPerLng(lat) {
  return (0.0003121092 * Math.pow(lat, 4.0) + 0.0101182384 * Math.pow(lat, 3.0) - 17.2385140059 * lat * lat
  + 5.5485277537 * lat + 111301.967182595)
}

function distPerLat(lat) {
  return -0.000000487305676 * Math.pow(lat, 4.0) - 0.0033668574 * Math.pow(lat, 3.0) + 0.4601181791 * lat * lat - 1.4558127346 * lat + 110579.25662316
}

// Earthquake Data
app.post('/alertingsystem/getEarthquakeData', function (req, res) {
    var data = req.body;
    var lat = data.lat;
    var lon = data.lon;
    var errorMsg = "Invalid Data";
    models.EarthQuake.find(function (err, alerts) {
      var apiResponse = new models.ApiResponse(200);
      if (err) {
      console.error(err);
      apiResponse.setError(errorMsg, new Error(err));
      } else {
        var modifiedAlerts=[]
        if(Array.isArray(alerts)){
          alerts.map(function (alert) {
            if (distance(parseFloat(alert.lat),parseFloat(alert.lon),parseFloat(lat),parseFloat(lon))<50) {
              modifiedAlerts.push(alert)
            }
          });
        }
        apiResponse.data = modifiedAlerts;
      }
      res.send(apiResponse);
    });
})

// Fire Data
app.post('/alertingsystem/getFireData', function (req, res) {
  var data = req.body;
  var lat = data.lat;
  var lon = data.lon;
	var errorMsg = "Invalid Data";
	models.Fire.find(function (err, alerts) {
	  var apiResponse = new models.ApiResponse(200);
	  if (err) {
		console.error(err);
		apiResponse.setError(errorMsg, new Error(err));
	  } else {
      var modifiedAlerts=[]
        alerts.map(function (alert) {
          if (distance(parseFloat(alert.lat),parseFloat(alert.lon),parseFloat(lat),parseFloat(lon))<50) {
            modifiedAlerts.push(alert)
          }
        });
      apiResponse.data = modifiedAlerts;
	  }
	  res.send(apiResponse);
	});
})

// Flood Data
app.post('/alertingsystem/getFloodData', function (req, res) {
  var data = req.body;
  var lat = data.lat;
  var lon = data.lon;
	var errorMsg = "Invalid Data";
	models.Flood.find(function (err, alerts) {
	  var apiResponse = new models.ApiResponse(200);
	  if (err) {
		console.error(err);
		apiResponse.setError(errorMsg, new Error(err));
	  } else {
      var modifiedAlerts=[]
      alerts.map(function (alert) {
          if (distance(parseFloat(alert.lat),parseFloat(alert.lon),parseFloat(lat),parseFloat(lon))<50) {
            modifiedAlerts.push(alert)
          }
      });
      apiResponse.data = modifiedAlerts;
	  }
	  res.send(apiResponse);
	});
})

// TSunami Data
app.post('/alertingsystem/getTSunamiData', function (req, res) {
  var data = req.body;
  var lat = data.lat;
  var lon = data.lon;
	var errorMsg = "Invalid Data";
	models.TSunami.find(function (err, alerts) {
	  console.log("getTSunamiData Response"+alerts);
	  var apiResponse = new models.ApiResponse(200);
	  if (err) {
      console.error(err);
      apiResponse.setError(errorMsg, new Error(err));
	  } else {
      var modifiedAlerts=[]
      console.log("alerts Data:"+ alerts+" "+Array.isArray(alerts))
      alerts.map(function (alert) {
          console.log("distance:"+ distance(parseFloat(alert.lat),parseFloat(alert.lon),parseFloat(lat),parseFloat(lon)))
          if (distance(parseFloat(alert.lat),parseFloat(alert.lon),parseFloat(lat),parseFloat(lon))<50) {
            modifiedAlerts.push(alert)
          }
      });
      apiResponse.data = modifiedAlerts;
	  }
	  res.send(apiResponse);
	});
})

app.post('/alertingsystem/userSettings', function (req, res) {
    var userData = req.body;
  
    var userId = userData.userId;
    var notifyNotification = userData.notifyNotification;
    var message = 'Invalid User';
    var options = {
      $set: {
        notifyNotification: notifyNotification
      }
    };

    var apiResponse = new models.ApiResponse(200);
    models.User.findByIdAndUpdate(userId, options, function (err, user) {
      console.log(JSON.stringify(user));
      if (err) {
        apiResponse.setError(message, new Error(err));
      } else {
        apiResponse.message = "User Settings Succesfully Updated";
        apiResponse.data = user;
      }
      res.send(apiResponse);
    });
});

// Login
app.post('/alertingsystem/login', function (req, res) {
    var userName = req.body.email;
    var pwd = req.body.password;
    var errorMsg = "Invalid UserName,password";
    models.User.find({
      email: userName,
      password: pwd
    }, function (err, result) {
      console.log(result);
      var apiResponse = new models.ApiResponse(200);
      if (err) {
        console.error(err);
        apiResponse.setError(errorMsg, new Error(err));
      } else {
        if (Array.isArray(result) && result.length > 0) {
          var user = result[0];
          apiResponse.data = user;
        } else {
          apiResponse.setError(errorMsg, new Error(err));
        }
      }
      res.send(apiResponse);
    });
})

app.post('/alertingsystem/registerToken', function (req, res) {
    var userData = req.body;
    var userId = userData.userId;
    var token = userData.token;
    var errorMsg = "Invalid User";
    var apiResponse = new models.ApiResponse(200);
    var options = {
      $set: {
        deviceToken: token
      }
    };
    console.log(JSON.stringify(token));
    models.User.update({ _id: userId }, { $set: { deviceToken: token } }, { new: false }, function (err, user) {
      console.log(JSON.stringify(user));
      if (err) {
        apiResponse.setError(message, new Error(err));
      } else {
        apiResponse.message = "Token Succesfully Updated";
      }
      return res.send(apiResponse);
    });
})

function sendSMSAlert(toNo,msg){
  var client = new twilio(accountSid, authToken);
  
  client.messages.create({
      body: msg,
      to: toNo,  // Text this number
      from: '+17084352369' // From a valid Twilio number
  })
  .then((message) => console.log(JSON.stringify(message)));
}

// ----END----   API call 

function alertEarthQuakeNotification(data){
  models.User.find(function (err, users) {
    console.log("Users Response:"+users);
    if (err) {
      console.error(err);
    } else {
      users.map(function (user) {
          console.log("Distance:"+ distance(parseFloat(user.lat),parseFloat(user.lon),parseFloat(data.lat),parseFloat(data.lon)))
          if (distance(parseFloat(user.lat),parseFloat(user.lon),parseFloat(data.lat),parseFloat(data.lon))<50) {
            //TODO send mail
            mail.sendEmail(user.email,
              "EarthQuake Alert...Be Safe",
              "Be Safe with Earthquake in your area\n"+
              "Magnitude:"+ data.magnitude+"\n"+
              "Location:"+ data.lat+","+data.lon+"\n\n\n"+
              "Thanks\nAlertingSystem Team", function (err, result) {
                console.log(result);
              });
              sendSMSAlert(user.phNo,"EarthQuake in your area.Be Alert")
              firebase.sendNotificationToTopic("earthquake","Alert EarthQuake", "Be Safe..!", "earthquake",
                  data, function (err, result) {
                    var apiResponse = new models.ApiResponse(200);
                  if (err) {
                    console.log('EarthQuake Alert Error:' + err);
                  } else {
                    console.log('EarthQuake Alert' + JSON.stringify(result));
                  }
              });
          }
      });
    }
  });
}

function alertFloodNotification(data){
    models.User.find(function (err, users) {
      console.log("Users Response:"+users);
      if (err) {
        console.error(err);
      } else {
        users.map(function (user) {
            console.log("Distance:"+ distance(parseFloat(user.lat),parseFloat(user.lon),parseFloat(data.lat),parseFloat(data.lon)))
            if (distance(parseFloat(user.lat),parseFloat(user.lon),parseFloat(data.lat),parseFloat(data.lon))<50) {
              //TODO send mail
              mail.sendEmail(user.email,
                "Flood Alert...Be Safe",
                "Be Safe with Flood in your area\n"+
                "Location:"+ data.lat+","+data.lon+"\n\n\n"+
                "Thanks\nAlertingSystem Team", function (err, result) {
                  console.log(result);
                });
                sendSMSAlert(user.phNo,"Flood in your area.Be Alert")
                firebase.sendNotificationToTopic("flood","Alert Flood", "Be Safe..!", "flood",
                  data, function (err, result) {
                  if (err) {
                    console.log('Flood Alert Error:' + err);
                  } else {
                    console.log('Flood Alert' + JSON.stringify(result));
                  }
                });  
            }
        });
      }
    });
}

function alertTsunamiNotification(data){
    models.User.find(function (err, users) {
      console.log("Users Response:"+users);
      if (err) {
        console.error(err);
      } else {
        users.map(function (user) {
            console.log("Distance:"+ distance(parseFloat(user.lat),parseFloat(user.lon),parseFloat(data.lat),parseFloat(data.lon)))
            if (distance(parseFloat(user.lat),parseFloat(user.lon),parseFloat(data.lat),parseFloat(data.lon))<50) {
              //TODO send mail
              mail.sendEmail(user.email,
                "TSunami Alert...Be Safe",
                "Be safe with Tsunami in your area\n"+
                "Magnitude:" + data.magnitude +"\nDepth:"+data.depth+"\n"+
                "Location:"+ data.lat+","+data.lon+"\n\n\n"+
                "Thanks\nAlertingSystem Team", function (err, result) {
                  console.log(result);
                });
                sendSMSAlert(user.phNo,"TSunami in your area.Be Alert")
                firebase.sendNotificationToTopic("tsunami","Alert Tsunami", "Be Safe..!", "tsunami",
                    data, function (err, result) {
                    if (err) {
                      console.log('TSunami Alert Error:' + err);
                    } else {
                      console.log('TSunami Alert' + JSON.stringify(result));
                    }
                });
            }
        });
      }
    });

}

function alertFireNotification(data){
  models.User.find(function (err, users) {
    console.log("Users Response:"+users);
    if (err) {
      console.error(err);
    } else {
      users.map(function (user) {
          console.log("Distance:"+ distance(parseFloat(user.lat),parseFloat(user.lon),parseFloat(data.lat),parseFloat(data.lon)))
          if (distance(parseFloat(user.lat),parseFloat(user.lon),parseFloat(data.lat),parseFloat(data.lon))<50) {
            //TODO send mail
            mail.sendEmail(user.email,
              "Fire Alert...Be Safe",
              "Be Safe with Fire in your area\n"+
              "BrightNess:"+ data.brightNess+"\n"+
              "Satellite:"+ data.satellite+"\n"+
              "Location:"+ data.lat+","+data.lon+"\n\n\n"+
              "Thanks\nAlertingSystem Team", function (err, result) {
                console.log(result);
              });
              sendSMSAlert(user.phNo,"Fire in your area.Be Alert")
              firebase.sendNotificationToTopic("fire","Alert Fire", "Be Safe..!", "fire",
                data, function (err, result) {
                var apiResponse = new models.ApiResponse(200);
                if (err) {
                  console.log('Fire Alert Error:' + err);
                } else {
                  console.log('Fire Alert' + JSON.stringify(result));
                }
              });
          }
      });
    }
  });
}

// send Alert
app.post('/alertingsystem/earthquakeAlert', function (req, res) {
  var alertData = req.body;
	var originTime = alertData.originTime;
	var magnitude= alertData.magnitude;
	var lat = alertData.lat;
	var lon = alertData.lon;

	var apiResponse = new models.ApiResponse(200);
	var earthQuake = new models.EarthQuake({
		originTime: originTime,
		magnitude: magnitude,
		lat:lat,
		lon:lon
	});
	earthQuake.save(function (err, result) {
    var apiResponse = new models.ApiResponse(200);
    if (err) {
      console.error(err);
      apiResponse.message = "Invalid EarthQuake Data";
      apiResponse.error = new Error(err);
    } else {
      apiResponse.data = result;
      alertEarthQuakeNotification(result);
    }
    res.send(apiResponse);
  });
})

// send Alert
app.post('/alertingsystem/fireAlert', function (req, res) {
    var alertData = req.body;
    var originTime = alertData.originTime;
    var brightNess= alertData.brightNess;
    var satellite= alertData.satellite;
    var lat = alertData.lat;
    var lon = alertData.lon;

    var apiResponse = new models.ApiResponse(200);
    var fire = new models.Fire({
      originTime: originTime,
      brightNess: brightNess,
      satellite: satellite,
      lat:lat,
      lon:lon
    });
    fire.save(function (err, result) {
      if (err) {
        console.error(err);
        apiResponse.message = "Invalid Fire Data";
        apiResponse.error = new Error(err);
      } else {
        apiResponse.data = result;
        alertFireNotification(result);
      }
      res.send(apiResponse);
    });
})

app.post('/alertingsystem/floodAlert', function (req, res) {
	console.log("Sockets::"+sockets);
    var alertData = req.body;
    var originTime = alertData.originTime;
    var lat = alertData.lat;
    var lon = alertData.lon;

    var apiResponse = new models.ApiResponse(200);
    var flood = new models.Flood({
          originTime: originTime,
          lat:lat,
          lon:lon
    });
    flood.save(function (err, result) {
      if (err) {
        console.error(err);
        apiResponse.message = "Invalid Flood Data";
        apiResponse.setError(new Error(err));
      } else {
        apiResponse.data = result;
        alertFloodNotification(result);
      }
      res.send(apiResponse);
    });
})

// register Alert
app.post('/alertingsystem/tsunamiAlert', function (req, res) {
    var alertData = req.body;
    var originTime = alertData.originTime;
    var magnitude = alertData.magnitude;
    var depth = alertData.depth;
    var lat = alertData.lat;
    var lon = alertData.lon;

    var tsunami = new models.TSunami({
        originTime: originTime,
        magnitude: magnitude,
        depth: depth,
        lat:lat,
        lon:lon
    });

    var apiResponse = new models.ApiResponse(200);
    tsunami.save(function (err, result) {
      var apiResponse = new models.ApiResponse(200);
      if (err) {
        console.error(err);
        apiResponse.message = "Invalid Tsunami Data";
        apiResponse.error = new Error(err);
      } else {
        apiResponse.data = result;
        alertTsunamiNotification(result);
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
  
    console.log("FamilyLink app listening at http://%s:%s", host, port)
})