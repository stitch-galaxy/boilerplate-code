var gulp = require('gulp');
var gutil = require("gulp-util");
var webpack = require("webpack");
var del = require('del');
var sourcemaps = require('gulp-sourcemaps');
var concat = require('gulp-concat');
var runSequence = require('run-sequence');

var config = require('./webpack.config.js');

var paths = {
  dirs: {
    build: {
      dev: './build'
    }
  },
  vendorjs: {
    dev: [
      "./node_modules/react/dist/react.js",
      "./node_modules/react-dom/dist/react-dom.js",
      "./node_modules/redux/dist/redux.js",
      "./node_modules/react-redux/dist/react-redux.js",
      "./node_modules/react-router/umd/ReactRouter.js",
      "./node_modules/react-intl/dist/react-intl.js",
      "./node_modules/react-intl/locale-data/ru.js",
    ]
  },
  html: ['./src/index.html'],
};

gulp.task('clean:dev', function () {
  return del(paths.dirs.build.dev, paths.dirs.css);
});

gulp.task('vendorjs:dev', function () {
  return gulp.src(paths.vendorjs.dev)
    .pipe(sourcemaps.init({loadMaps: true}))
    .pipe(concat('vendor.js'))
    .pipe(sourcemaps.write('./'))
    .pipe(gulp.dest(paths.dirs.build.dev));
});

	
gulp.task("webpack", function (callback) {
  // run webpack
  webpack(config, function (err, stats) {
    if (err) throw new gutil.PluginError("webpack", err);
    gutil.log("[webpack]", stats.toString({
      // output options
    }));
    callback();
  });
});

gulp.task('build:dev', function(callback) {
  runSequence('clean:dev',
              ['webpack', 'vendorjs:dev'],
              callback);
});
