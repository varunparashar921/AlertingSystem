module.exports = function (mongoose) {
    var versionStatus = {
        versionKey: false // You should be aware of the outcome after set to false
    };
    var Schema = mongoose.Schema;

    var userSchema = new Schema({
        firstName: { type: String, required: true },
        lastName: { type: String, required: true },
        email: { type: String, unique: true, required: true },
        password: { type: String, required: true },
        gender: String,
        phNo: String,
        lat: String,
        lon: String,
        notifyNotification: { type: Boolean, default: true },
        deviceToken: String,
        deviceName: { type: String, required: true },
        deviceOS: { type: String, required: true }
    }, versionStatus);

    var tsunamiSchema = new Schema({
        originTime: { type: String, required: true },
        magnitude: { type: String, required: true },
        depth: { type: String },
        lat: { type: String },
        lon:{type:String}
    }, versionStatus);

    var floodSchema = new Schema({
        originTime: { type: String, required: true },
        lat: { type: String },
        lon:{type:String}
    }, versionStatus);

    var fireSchema = new Schema({
        originTime: { type: String, required: true },
        brightNess: { type: String, required: true },
        satellite: { type: String },
        lat: { type: String },
        lon:{type:String}
    }, versionStatus);

    var earthQuakeSchema = new Schema({
        originTime: { type: String, required: true },
        magnitude: { type: String, required: true },
        lat: { type: String },
        lon:{type:String}
    }, versionStatus);

    function ApiResponse(statusCode, message, data, error) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.error = error;

        this.setError = function (message, err) {
            this.statusCode = 503;
            this.message = message;
            this.error = new Error(err);
        }
    }
    // declare seat covers here too
    var models = {
        User: mongoose.model('User', userSchema),
        TSunami: mongoose.model('TSunami', tsunamiSchema),
        Flood: mongoose.model('Flood', floodSchema),
        Fire: mongoose.model('Fire', fireSchema),
        EarthQuake: mongoose.model('EarthQuake', earthQuakeSchema),
        ApiResponse
    };
    return models;
}