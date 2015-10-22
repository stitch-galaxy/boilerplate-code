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
var runSequence = require('run-sequence');
var del = require('del');
var concat = require('gulp-concat');
var rename = require('gulp-rename');
var rev = require('gulp-rev');
var revCollector = require('gulp-rev-collector');
var angularTemplateCache = require('gulp-angular-templatecache');
var addStream = require('add-stream');

var paths = {
  dirs: {
    build: {
      dev : './build',
      prod : './dist'     
    },
    manifests: './manifests'
  },
  js : './components/**/*.js',
  sass : './sass/main.scss',
  images : './images/**/*.{png,jpg,jpeg,gif}',
  html : './*.html',
  favicons : ['./favicon.ico', './apple-touch-icon.png'],
  robots : './robots.txt',
  fonts : ['./fonts/**/*'],
  vendorFonts : ['./bower_components/bootstrap-sass/assets/fonts/**/*', './bower_components/components-font-awesome/fonts/**/*'],
  partials : './partials/**/*'
};

//clean
gulp.task('clean:prod', function () {
  return del([paths.dirs.build.prod, paths.dirs.manifests]);
});

gulp.task('clean:dev', function () {
  return del(paths.dirs.build.dev);
});

//linting
gulp.task('lintGulpfile', function() {
  return gulp.src('./gulpfile.js')
    .pipe(jshint())
    .pipe(jshint.reporter('jshint-stylish'))
    .pipe(jshint.reporter('fail'));
});

gulp.task('templates:cache', function () {
  var angularTemplateCacheOptions = {
    root : 'partials'
  };
  return gulp.src(paths.partials)
    .pipe(angularTemplateCache(angularTemplateCacheOptions))
    //.pipe(gulp.dest('./components'));
    .pipe(gulp.dest(paths.dirs.build.dev + '/assets/js'));
});

function prepareTemplates() {
  var angularTemplateCacheOptions = {
    root : 'partials'
  };

  return gulp.src(paths.partials)
    //.pipe(minify and preprocess the template html here)
    .pipe(angularTemplateCache(angularTemplateCacheOptions));
}

//js
gulp.task('js:prod', function () {
  return gulp.src(paths.js)
    .pipe(jshint())
    .pipe(jshint.reporter('jshint-stylish'))
    .pipe(jshint.reporter('fail'))
    .pipe(addStream.obj(prepareTemplates()))
    .pipe(sourcemaps.init({loadMaps: true}))
      .pipe(concat('site.js'))
      .pipe(ngAnnotate())
      .pipe(rename({suffix: '.min'}))
      .pipe(uglify())	
      .pipe(rev())
    .pipe(sourcemaps.write('./'))
    .pipe(gulp.dest(paths.dirs.build.prod + '/assets/js'))
    .pipe(rev.manifest('./rev-manifest-js.json'))
    .pipe(gulp.dest(paths.dirs.manifests));
});

gulp.task('js:dev', function () {
  return gulp.src(paths.js)
    .pipe(jshint())
    .pipe(jshint.reporter('jshint-stylish'))
    .pipe(jshint.reporter('fail'))
    .pipe(addStream.obj(prepareTemplates()))
    .pipe(sourcemaps.init({loadMaps: true}))
      .pipe(concat('site.js'))
      .pipe(rename({suffix: '.min'}))
    .pipe(sourcemaps.write('./'))
    .pipe(gulp.dest(paths.dirs.build.dev + '/assets/js'))
    .pipe(connect.reload());
});

//css
gulp.task('css:prod', function () {
  var sassOptions = {
  //  outputStyle: 'compressed'
  };
  var autoPrefixerOptions = {
    browsers: ['last 2 versions']
  };

  return gulp.src(paths.sass)
    .pipe(sourcemaps.init({loadMaps: true}))
      .pipe(sass(sassOptions).on('error', sass.logError))
      .pipe(concat('site.css'))
      .pipe(autoprefixer(autoPrefixerOptions))
      .pipe(rename({suffix: '.min'}))
      .pipe(minifyCss())
      .pipe(rev())
    .pipe(sourcemaps.write('./'))
    .pipe(gulp.dest(paths.dirs.build.prod + '/assets/css'))
    .pipe(rev.manifest('./rev-manifest-css.json'))
    .pipe(gulp.dest(paths.dirs.manifests));
});

gulp.task('css:dev', function () {
  var sassOptions = {
  //  outputStyle: 'compressed'
  };
  var autoPrefixerOptions = {
    browsers: ['last 2 versions']
  };

  return gulp.src(paths.sass)
    .pipe(sourcemaps.init({loadMaps: true}))
      .pipe(concat('site.css'))
      .pipe(sass(sassOptions).on('error', sass.logError))
      .pipe(autoprefixer(autoPrefixerOptions))
      .pipe(rename({suffix: '.min'}))
    .pipe(sourcemaps.write('./'))
    .pipe(gulp.dest(paths.dirs.build.dev + '/assets/css'))
    .pipe(connect.reload());
});

//bower dependencies
gulp.task('dependencies', function() {
  return bower();
});

//vendorjs
gulp.task('vendorjs:prod', function () {
  return gulp.src(wiredep().js)
    .pipe(sourcemaps.init({loadMaps: true}))
      .pipe(concat('vendor.js'))
      .pipe(rename({suffix: '.min'}))
      .pipe(uglify())	
      .pipe(rev())
    .pipe(sourcemaps.write('./'))
    .pipe(gulp.dest(paths.dirs.build.prod + '/assets/js'))
    .pipe(rev.manifest('./rev-manifest-vendorjs.json'))
    .pipe(gulp.dest(paths.dirs.manifests));

});

gulp.task('vendorjs:dev', function () {
  return gulp.src(wiredep().js)
    .pipe(sourcemaps.init({loadMaps: true}))
      .pipe(concat('vendor.js'))
      .pipe(rename({suffix: '.min'}))
    .pipe(sourcemaps.write('./'))
    .pipe(gulp.dest(paths.dirs.build.dev + '/assets/js'));
});

//vendorcss
gulp.task('vendorcss:prod', function () {
  return gulp.src(wiredep().css)
    .pipe(sourcemaps.init({loadMaps: true}))
        .pipe(concat('vendor.css'))
        .pipe(rename({suffix: '.min'}))
        .pipe(minifyCss())
        .pipe(rev())
    .pipe(sourcemaps.write('./'))
    .pipe(gulp.dest(paths.dirs.build.prod + '/assets/css'))
    .pipe(rev.manifest('./rev-manifest-vendorcss.json'))
    .pipe(gulp.dest(paths.dirs.manifests));
});

gulp.task('vendorcss:dev', function () {
  return gulp.src(wiredep().css)
    .pipe(sourcemaps.init({loadMaps: true}))
        .pipe(concat('vendor.css'))
        .pipe(rename({suffix: '.min'}))
    .pipe(sourcemaps.write('./'))
    .pipe(gulp.dest(paths.dirs.build.dev + '/assets/css'));
});

//html
gulp.task('html:prod', function () {
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
  return gulp.src([paths.dirs.manifests + '/**/*.json', paths.html])
    .pipe(revCollector(revCollectorOptions))
    .pipe(minifyHtml(minifyHtmlOpts))
    .pipe(gulp.dest(paths.dirs.build.prod));
});

gulp.task('html:dev', function () {
  return gulp.src(paths.html)
    .pipe(gulp.dest(paths.dirs.build.dev))
    .pipe(connect.reload());
});

gulp.task('htmlAndJsAndCss:prod', function(callback) {
  runSequence('dependencies',
              ['js:prod', 'vendorjs:prod', 'css:prod', 'vendorcss:prod'],
              'html:prod',
              callback);
});

gulp.task('htmlAndJsAndCss:dev', function(callback) {
  runSequence('dependencies',
              ['js:dev', 'vendorjs:dev', 'css:dev', 'vendorcss:dev', 'html:dev'],
              callback);
});

//images
gulp.task('images:prod', function () {
  var imageminOptions = {
                          //progressive: true,
                          //optimizationLevel: 7,
                          //interlaced: true,
                          //multipass: true
                        };
  return gulp.src(paths.images)
    .pipe(cache(imagemin(imageminOptions)))
    .pipe(gulp.dest(paths.dirs.build.prod + '/assets/img'));
});

gulp.task('images:dev', function () {
  return gulp.src(paths.images)
    .pipe(gulp.dest(paths.dirs.build.dev + '/assets/img'));
});

//favicon
gulp.task('favicon:prod', function () {
  return gulp.src(paths.favicons)
    .pipe(gulp.dest(paths.dirs.build.prod));
});

gulp.task('favicon:dev', function () {
  return gulp.src(paths.favicons)
    .pipe(gulp.dest(paths.dirs.build.dev));
});

//robots
gulp.task('robots:prod', function () {
  return gulp.src(paths.robots)
    .pipe(gulp.dest(paths.dirs.build.prod));
});

gulp.task('robots:dev', function () {
  return gulp.src(paths.robots)
    .pipe(gulp.dest(paths.dirs.build.dev));
});

//fonts
gulp.task('fonts:prod', function () {
  return gulp.src(paths.fonts)
    .pipe(gulp.dest(paths.dirs.build.prod + '/assets/fonts'));
});

gulp.task('fonts:dev', function () {
  return gulp.src(paths.fonts)
    .pipe(gulp.dest(paths.dirs.build.dev + '/assets/fonts'));
});

//vendorfonts
gulp.task('vendorfonts:prod', function () {
  return gulp.src(paths.vendorFonts)
    .pipe(gulp.dest(paths.dirs.build.prod + '/assets/fonts'));
});

gulp.task('vendorfonts:dev', function () {
  return gulp.src(paths.vendorFonts)
    .pipe(gulp.dest(paths.dirs.build.dev + '/assets/fonts'));
});

//partials
gulp.task('partials:prod', function () {
  return gulp.src(paths.partials)
    .pipe(gulp.dest(paths.dirs.build.prod + '/partials'));
});

gulp.task('partials:dev', function () {
  return gulp.src(paths.partials)
    .pipe(gulp.dest(paths.dirs.build.dev + '/partials'))
    .pipe(connect.reload());
});

gulp.task('build:prod', function(callback) {
  runSequence('lintGulpfile',
              'clean:prod',
              ['images:prod', 'favicon:prod', 'robots:prod', 'fonts:prod', 'vendorfonts:prod', 'partials:prod', 'htmlAndJsAndCss:prod'],
              callback);
});

gulp.task('build:dev', function(callback) {
  runSequence('lintGulpfile',
              'clean:dev',
              ['images:dev', 'favicon:dev', 'robots:dev', 'fonts:dev', 'vendorfonts:dev', 'partials:dev', 'htmlAndJsAndCss:dev'],
              //['images:dev', 'favicon:dev', 'robots:dev', 'fonts:dev', 'vendorfonts:dev', 'partials:dev', 'htmlAndJsAndCss:dev', 'templates:cache'],
              callback);
});

gulp.task('connect:prod', function() {
  connect.server({
    root: paths.dirs.build.prod,
    livereload: true
  });
});

gulp.task('connect:dev', function() {
  connect.server({
    root: paths.dirs.build.dev,
    livereload: true
  });
});

gulp.task('watch:dev', function () {
  gulp.watch(paths.html, ['html:dev']);
  gulp.watch(paths.sass, ['css:dev']);
  gulp.watch(paths.js, ['js:dev']);
  gulp.watch(paths.partials, ['partials:dev']);
});

gulp.task('dev', ['build:dev', 'connect:dev', 'watch:dev']);
gulp.task('prod', ['build:prod', 'connect:prod']);

gulp.task('default', ['dev']);