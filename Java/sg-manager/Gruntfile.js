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
      js: {
        files: ['<%= app.src %>/scripts/{,*/}*.js'],
        tasks: ['newer:jshint:all']
      },
      gruntfile: {
        files: ['Gruntfile.js']
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


