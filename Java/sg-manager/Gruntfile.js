// Author Tarasov Evgeny 2014
'use strict';

module.exports = function (grunt) {

  // Load grunt tasks automatically
  require('load-grunt-tasks')(grunt);

  // Time how long tasks take. Can help when optimizing build times
  require('time-grunt')(grunt);

  // Define the configuration for all the tasks
  grunt.initConfig({

    // Project settings
    app: {
      // configurable paths
      src: 'src',
      target: 'target'
    },

    watch: {
      bower: {
        files: ['bower.json'],
        tasks: ['bowerInstall']
      },
      js: {
        files: ['<%= app.src %>/scripts/{,*/}*.js'],
        tasks: ['newer:jshint:all']
      },
      gruntfile: {
        files: ['Gruntfile.js']
      },
      compass: {
        files: ['<%= app.src %>/sass/{,*/}*.{scss,sass}'],
        tasks: ['compass:app']
      }

    },

    // Automatically inject Bower components into the app
    bowerInstall: {
      app: {
        src: ['<%= app.src %>/*.html']
      }
    },

    // Make sure code styles are up to par and there are no obvious mistakes
    jshint: {
      options: {
        jshintrc: '.jshintrc',
        reporter: require('jshint-stylish')
      },
      all: [
        'Gruntfile.js',
        '<%= app.src %>/scripts/{,*/}*.js'
      ]
    },

    // Compiles Sass to CSS and generates necessary files if requested
    compass: {
      options: {
        sassDir: '<%= app.src %>/sass',
        cssDir: '<%= app.src %>/styles',
        generatedImagesDir: '<%= app.src %>/images/generated',
        imagesDir: '<%= app.src %>/images',
        javascriptsDir: '<%= app.src %>/scripts',
        fontsDir: '<%= app.src %>/sass/fonts',
        importPath: '<%= app.src %>/bower_components',
        httpImagesPath: '/images',
        httpGeneratedImagesPath: '/images/generated',
        httpFontsPath: '/styles/fonts',
        relativeAssets: false,
        assetCacheBuster: false,
        raw: 'Sass::Script::Number.precision = 10\n'
      },
      app: {
      }
    },


    // Empties folders to start fresh
    clean: {
      target: {
        files: [{
          dot: true,
          src: ['<%= app.target %>/*']
        }]
      }
    }
  });

  grunt.registerTask('build', [
    'clean'
  ]);

  grunt.registerTask('default', [
    'newer:jshint',
    'build',
    'watch'
  ]);
};
