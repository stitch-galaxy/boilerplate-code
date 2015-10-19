'use strict';

var gulp = require('gulp');
var bower = require('gulp-bower');
var jshint = require('gulp-jshint');
var stylish = require('jshint-stylish');
var wiredep = require('wiredep').stream;
var sourcemaps = require('gulp-sourcemaps');
var sass = require('gulp-sass');
var connect = require('gulp-connect');
var autoprefixer = require('gulp-autoprefixer');
var copy = require('gulp-copy');
var imagemin = require('gulp-imagemin');
var del = require('del');

var usemin = require('gulp-usemin');
var useref = require('gulp-useref');
var gulpif = require('gulp-if');
var filter = require('gulp-filter');
var uglify = require('gulp-uglify');
var minifyCss = require('gulp-minify-css');
var rev = require('gulp-rev');
var revReplace = require('gulp-rev-collector');
var minifyHtml = require('gulp-minify-html');
var ngAnnotate = require('gulp-ng-annotate');


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

gulp.task('index', function() {
  var assets = useref.assets();

  return gulp.src('./app/index.html')
    .pipe(assets)
    .pipe(gulpif('*.js', uglify()))
    .pipe(gulpif('*.css', minifyCss()))
    .pipe(assets.restore())
    .pipe(useref())
    .pipe(gulp.dest('./dist'));
});


//gulp.task('indexNew', function() {
//    return gulp.src('./app/index.html')
//        .pipe(usemin({
            //js: [
                //sourcemaps.init({
                //    loadMaps: true
                //}),
                //'concat',
                //uglify()//,
                //sourcemaps.write('./')
            //]//,
            //css: [
            //    sourcemaps.init({
            //        loadMaps: true
            //    }),
            //   'concat',
            //    minifyCss(),
            //    sourcemaps.write('./')
            //]
//        }))
//        .pipe(gulp.dest('./dist/'));
//});

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

gulp.task('renameFiles', function () {
    return gulp.src('./dist/images/**/*.{png,jpg,jpeg,gif}')
        .pipe(rev())
        .pipe(gulp.dest('./dist/images'))
        .pipe(rev.manifest())
        .pipe(gulp.dest('./manifests/'));
});

gulp.task('replaceFileNames', function () {
  var revReplaceOptions = {
    //replaceReved: true
  };
  return gulp.src(['./manifests/*.json', './dist/**/*.html'])
    .pipe(revReplace(revReplaceOptions))
    .pipe(gulp.dest('./dist'));
});


gulp.task('build', ['lintGulpfile', 'bower', 'lint', 'html', 'js', 'css']);

gulp.task('debug', ['build', 'connect', 'watch']);

//gulp.task('dist', ['build', 'clean', 'copy', 'imagemin', 'renameFiles', 'replaceFileNames']);
gulp.task('dist', ['build', 'clean', 'copy', 'imagemin', 'usemin', 'connectDist']);
//gulp.task('dist', ['build', 'clean', 'copy', 'imagemin', 'index', 'connectDist']);

gulp.task('default', ['debug']);


