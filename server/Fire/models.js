module.exports = function (mongoose) {
    var versionStatus = {
        versionKey: false // You should be aware of the outcome after set to false
    };
    var Schema = mongoose.Schema;

    var alertSchema = new Schema({
        originTime: { type: String, required: true },
        brightNess: { type: String, required: true },
        satellite: { type: String },
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
        Alert: mongoose.model('Alert', alertSchema),
        ApiResponse
    };
    return models;
}