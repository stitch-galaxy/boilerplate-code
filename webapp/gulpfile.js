var gulp = require('gulp');
var gutil = require("gulp-util");
var webpack = require("webpack");

var config = require('./webpack.config.js');


gulp.task("webpack", function(callback) {
    // run webpack
    webpack(config, function(err, stats) {
        if(err) throw new gutil.PluginError("webpack", err);
        gutil.log("[webpack]", stats.toString({
            // output options
        }));
        callback();
    });
});