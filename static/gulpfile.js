'use strict';

var gulp = require('gulp');
//bower
var bower = require('gulp-bower');
var wiredep = require('wiredep');
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
var cache = require('gulp-cache');
//web server
var connect = require('gulp-connect');
//dist tasks
var del = require('del');
var concat = require('gulp-concat');
var concatCss = require('gulp-concat-css');
var rename = require('gulp-rename');
var rev = require('gulp-rev');
var revCollector = require('gulp-rev-collector');
var copy = require('gulp-copy');
//usemin
var usemin = require('gulp-usemin');

//clean task other tasks depends on
gulp.task('clean', function () {
  return del('build/**/*');
});

//linting
gulp.task('lintGulpfile', function() {
  return gulp.src('./gulpfile.js')
    .pipe(jshint())
    .pipe(jshint.reporter('jshint-stylish'))
    .pipe(jshint.reporter('fail'));
});

gulp.task('dependencies', function() {
  return bower();
});

gulp.task('js', function () {
  return gulp.src('./components/**/*.js')
    .pipe(jshint())
    .pipe(jshint.reporter('jshint-stylish'))
    .pipe(jshint.reporter('fail'))
    .pipe(sourcemaps.init({loadMaps: true}))
	.pipe(concat('site.js'))
        .pipe(ngAnnotate())
	.pipe(rename({suffix: '.min'}))
        .pipe(rev())
	.pipe(uglify())	
    .pipe(sourcemaps.write('./'))
    .pipe(gulp.dest('./build/assets/js'))
    .pipe(rev.manifest('./rev-manifest-js.json'))
    .pipe(gulp.dest('./manfiests'));
});

gulp.task('vendorjs', ['dependencies'], function () {
  return gulp.src(wiredep().js)
    .pipe(sourcemaps.init({loadMaps: true}))
	.pipe(concat('vendor.js'))
	.pipe(rename({suffix: '.min'}))
        .pipe(rev())
	.pipe(uglify())	
    .pipe(sourcemaps.write('./'))
    .pipe(gulp.dest('./build/assets/js'))
    .pipe(rev.manifest('./rev-manifest-vendorjs.json'))
    .pipe(gulp.dest('./manfiests'));

});

gulp.task('css', function () {
  var sassOptions = {
  //  outputStyle: 'compressed'
  };
  var autoPrefixerOptions = {
    browsers: ['last 2 versions']
  };

  return gulp.src('./sass/main.scss')
    .pipe(sourcemaps.init({loadMaps: true}))
        .pipe(concat('site.css'))
        .pipe(sass(sassOptions).on('error', sass.logError))
        .pipe(autoprefixer(autoPrefixerOptions))
        .pipe(rename({suffix: '.min'}))
        .pipe(rev())
        .pipe(minifyCss())
    .pipe(sourcemaps.write('./'))
    .pipe(gulp.dest('./build/assets/css'))
    .pipe(rev.manifest('./rev-manifest-css.json'))
    .pipe(gulp.dest('./manfiests'));
});

gulp.task('vendorcss', ['dependencies'], function () {
  return gulp.src(wiredep().css)
    .pipe(sourcemaps.init({loadMaps: true}))
        .pipe(concat('vendor.css'))
        .pipe(rename({suffix: '.min'}))
        .pipe(rev())
        .pipe(minifyCss())
    .pipe(sourcemaps.write('./'))
    .pipe(gulp.dest('./build/assets/css'))
    .pipe(rev.manifest('./rev-manifest-vendorcss.json'))
    .pipe(gulp.dest('./manfiests'));
});

gulp.task('images', function () {
  var imageminOptions = {
                          //progressive: true,
                          //optimizationLevel: 7,
                          //interlaced: true,
                          //multipass: true
                        };
  return gulp.src('./images/**/*.{png,jpg,jpeg,gif}')
    .pipe(cache(imagemin(imageminOptions)))
    .pipe(gulp.dest('./build/assets/img'));
});

gulp.task('html', ['js', 'vendorjs', 'css', 'vendorcss', 'images'], function () {
  var minifyHtmlOpts = { 
                           empty : true,
                           cdata : true,
                           comments : true,
                           conditionals : true,
                           spare : true,
                           quotes : true,
                           loose : true
                       };
  var revCollectorOptions = {
                                replaceReved: true
                            };
  return gulp.src(['./manfiests/**/*.json', './*.html'])
    .pipe(revCollector(revCollectorOptions))
    .pipe(minifyHtml(minifyHtmlOpts))
    .pipe(gulp.dest('./build'));
});

gulp.task('copyFaviconsAndRobotsTxt', function () {
  return gulp.src(['./favicon.ico', './apple-touch-icon.png', './robots.txt'])
    .pipe(copy('./build/'));
});

gulp.task('copyFonts', function () {
  return gulp.src('./fonts/**/*')
    .pipe(copy('./build/assets'));
});

gulp.task('copyPartials', function () {
  return gulp.src('./partials/**/*')
    .pipe(copy('./build'));
});

gulp.task('copy', ['copyFonts', 'copyPartials', 'copyFaviconsAndRobotsTxt']);

gulp.task('build', ['lintGulpfile', 'html', 'images', 'copy']);

gulp.task('connect', ['build'], function() {
  connect.server({
    root: './build',
    livereload: true
  });
});

gulp.task('watch', function () {
  gulp.watch('./app/index.html', ['html']);
  gulp.watch('./app/sass/**/*.scss', ['css']);
  gulp.watch('./app/components/**/*.js', ['lint', 'js']);
});

gulp.task('default', ['connect']);


