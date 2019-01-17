var nodemailer = require('nodemailer');
var smtpTransport = nodemailer.createTransport({
  service: 'Gmail',
  auth: {
    user: 'alertingsystem.dev@gmail.com',
    pass: 'Timp1234'
  }
});

exports.sendEmail = function (toMail, subject,body,callback) {
  var mailOptions = {
    from: 'alertingsystem.dev@gmail.com',
    to: toMail,
    subject: subject,
    text: body
  };
  smtpTransport.sendMail(mailOptions, callback);
};