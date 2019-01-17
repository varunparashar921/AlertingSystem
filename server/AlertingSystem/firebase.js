// Your Firebase Cloud Messaging Server API key
var request = require('request');
var API_KEY = "AAAAikmcLbA:APA91bHmo73NWro7JgpWbsNN0mo8aocDrzl4ThmqA9MUItY2PcIDfv_AMKENXPqGWKifX8C7jb1v1pAWCdLxo8WNzlRo-9xHjgm8zq0YmO5ic_dfo5KzqxJI3uvZZIY-gXXrPOCafVw2";
var SENDER_KEY = "593940458928";
exports.sendNotificationToTopic = function (to, title, message, notificationType, data, onSuccess) {
    console.log("Topic :" + to);
    var notificationData = {
        to: '/topics/' + to,
        notification: {
            title: title,
            body: message
        },
        data: {
            type: notificationType,
            title: title,
            message: message,
            body: data
        }
    };
    request({
        url: 'https://fcm.googleapis.com/fcm/send',
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'key=' + API_KEY
        },
        body: JSON.stringify(notificationData)
    }, function (error, response, body) {
        console.log("error:" + JSON.stringify(error));
        console.log("Response:" + JSON.stringify(response));
        console.log("Body:" + JSON.stringify(body));
        console.log("Data:" + JSON.stringify(data));
        if (error) { console.error(error); }
        else if (response.statusCode >= 400) {
            console.error('HTTP Error: ' + response.statusCode + ' - ' + response.statusMessage);
        } else {
        }
        onSuccess(error, response);
    });
};

exports.sendNotificationToUser = function (to, title, message, notificationType, data, onSuccess) {
    var notificationData = {
        to: to,
        notification: {
            title: title,
            body: message
        },
        data: {
            type: notificationType,
            title: title,
            message: message,
            body: data
        }
    };
    request({
        url: 'https://fcm.googleapis.com/fcm/send',
        method: 'POST',
        headers: {
            'Content-Type': ' application/json',
            'Authorization': 'key=' + API_KEY
        },
        body: JSON.stringify(notificationData)
    }, function (error, response, body) {
        console.log("error:" + JSON.stringify(error));
        console.log("Response:" + JSON.stringify(response));
        console.log("Body:" + JSON.stringify(body));
        console.log("Data:" + JSON.stringify(data));
        if (error) { console.error(error); }
        else if (response.statusCode >= 400) {
            console.error('HTTP Error: ' + response.statusCode + ' - ' + response.statusMessage);
        } else {
            onSuccess();
        }
    });
};

exports.sendNotificationToMultipleUsers = function (users, title, message, notificationType, data, onSuccess) {
    console.log("Users;;;" + users);
    var notificationData = {
        operation: "create",
        notification_key_name: "testing",
        registration_ids: users,
        notification: {
            title: title,
            body: message
        },
        data: {
            type: notificationType,
            title: title,
            body: data
        }
    };
    request({
        url: 'https://android.googleapis.com/gcm/notification',
        method: 'POST',
        headers: {
            'Content-Type': ' application/json',
            'Authorization': 'key=' + API_KEY,
            'project_id': SENDER_KEY
        },
        body: JSON.stringify(notificationData)
    }, function (error, response, body) {
        console.log("error:" + JSON.stringify(error));
        console.log("Response:" + JSON.stringify(response));
        console.log("Body:" + JSON.stringify(body));
        console.log("Data:" + JSON.stringify(data));
        if (error) { console.error(error); }
        else if (response.statusCode >= 400) {
            console.error('HTTP Error: ' + response.statusCode + ' - ' + response.statusMessage);
        } else {
            onSuccess();
        }
    });
};