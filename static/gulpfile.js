'use strict';

var gulp = require('gulp');
//bower
var bower = require('gulp-bower');
var wiredep = require('wiredep').stream;
//linting
var jshint = require('gulp-jshint');
var stylish = require('jshint-stylish');
//sourcemaps
var sourcemaps = require('gulp-sourcemaps');
//css
var sass = require('gulp-sass');
var autoprefixer = require('gulp-autoprefixer');
var minifyCss = require('gulp-minify-css');
//js
var uglify = require('gulp-uglify');
var ngAnnotate = require('gulp-ng-annotate');
//html
var minifyHtml = require('gulp-minify-html');
//images
var imagemin = require('gulp-imagemin');
//web server
var connect = require('gulp-connect');
//dist tasks
var del = require('del');
var rev = require('gulp-rev');
var copy = require('gulp-copy');
//usemin
var usemin = require('gulp-usemin');

gulp.task('lintGulpfile', function() {
  return gulp.src('./gulpfile.js')
    .pipe(jshint())
    .pipe(jshint.reporter('jshint-stylish'))
    .pipe(jshint.reporter('fail'));
});


gulp.task('lint', function() {
  return gulp.src('./app/components/**/*.js')
    .pipe(jshint())
    .pipe(jshint.reporter('jshint-stylish'))
    .pipe(jshint.reporter('fail'));
});

gulp.task('bower', function() {
  return bower();
});

gulp.task('connect', function() {
  connect.server({
    root: './app',
    livereload: true
  });
});

gulp.task('connectDist', function() {
  connect.server({
    root: './dist',
    livereload: true
  });
});


gulp.task('html', function () {
  gulp.src('./app/index.html')
    .pipe(wiredep())
    .pipe(gulp.dest('./app'))
    .pipe(connect.reload());
});

gulp.task('js', function () {
  gulp.src('./app/components/**/*.js')
    .pipe(connect.reload());
});

gulp.task('css', function () {
  var sassOptions = {
  //  outputStyle: 'compressed'
  };
  var autoPrefixerOptions = {
    browsers: ['last 2 versions']
  };

  gulp.src('./app/sass/main.scss')
    .pipe(sourcemaps.init())
    .pipe(sass(sassOptions).on('error', sass.logError))
    .pipe(autoprefixer(autoPrefixerOptions))
    .pipe(sourcemaps.write('./'))
    .pipe(gulp.dest('./app/css'))
    .pipe(connect.reload());
});

gulp.task('watch', function () {
  gulp.watch('./app/index.html', ['html']);
  gulp.watch('./app/sass/**/*.scss', ['css']);
  gulp.watch('./app/components/**/*.js', ['lint', 'js']);
});

gulp.task('clean', function () {
  return del('dist/**/*');
});

gulp.task('copy', function () {
  var toCopy = [
                 //'./app/index.html',
                 './app/partials/**/*.html'
               ];
  return gulp.src(toCopy)
    .pipe(copy('./dist', {prefix: 1}));
});

gulp.task('imagemin', function () {
  var imageminOptions = {
                          //progressive: true,
                          //optimizationLevel: 7,
                          //interlaced: true,
                          //multipass: true
                        };
  return gulp.src('./app/images/**/*.{png,jpg,jpeg,gif}')
    .pipe(imagemin(imageminOptions))
    .pipe(gulp.dest('./dist/images'));
});

gulp.task('usemin', function() {
  return gulp.src('./app/index.html')
    .pipe(usemin({
      css: [ sourcemaps.init({loadMaps: true, debug: true}), 'concat', minifyCss(), rev , sourcemaps.write('./') ],
      vendorcss : [ sourcemaps.init({loadMaps: true, debug: true}), 'concat', minifyCss(), rev , sourcemaps.write('./') ],
      html: [ function () {return minifyHtml({ empty: true });} ],
      vendorjs: [ sourcemaps.init({loadMaps: true, debug: true}), ngAnnotate, uglify, rev, sourcemaps.write('./') ],
      js: [ sourcemaps.init({loadMaps: true, debug: true}), ngAnnotate, uglify, rev, sourcemaps.write('./') ]
    }))
    .pipe(gulp.dest('./dist/'));
});

gulp.task('build', ['lintGulpfile', 'bower', 'lint', 'html', 'js', 'css']);

gulp.task('debug', ['build', 'connect', 'watch']);

gulp.task('dist', ['build', 'clean', 'copy', 'imagemin', 'usemin', 'connectDist']);

gulp.task('default', ['debug']);


