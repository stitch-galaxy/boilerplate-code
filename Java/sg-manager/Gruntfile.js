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
      app: [
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
      },
      cache: {
        files: [{
          dot: true,
          src: ['.tmp', '.sass-cache']
        }]
      }
    },

    copy: {
      sourcesForMinifiedVersion: {
        files: [{
          expand: true,
          dot: true,
          cwd: '<%= app.src %>',
          dest: '<%= app.target %>',
          src: [
            '*.{ico,png,txt}',
            '.htaccess',
            '*.html',
            'views/**',
          ]
        }]
      },
      sourcesForNonMinifiedVersion: {
        files: [{
          expand: true,
          dot: true,
          cwd: '<%= app.src %>',
          dest: '<%= app.target %>',
          src: [
            '*.{ico,png,txt}',
            '.htaccess',
            '*.html',
            'views/**',
            'scripts/**',
            'bower_components/**',
          ]
        }]
      },
      devConfig: {
        src: '<%= app.src %>/etc/dev.js',
        dest: '<%= app.src %>/scripts/services/config.js'
      },
      prodConfig: {
        src: '<%= app.src %>/etc/prod.js',
        dest: '<%= app.src %>/scripts/services/config.js'
      },
      uatConfig: {
        src: '<%= app.src %>/etc/uat.js',
        dest: '<%= app.src %>/scripts/services/config.js'
      },
    },

    // Reads HTML for usemin blocks to enable smart builds that automatically
    // concat, minify and revision files. Creates configurations in memory so
    // additional tasks can operate on them
    useminPrepare: {
      html: '<%= app.src %>/index.html',
      options: {
        dest: '<%= app.target %>',
        flow: {
          html: {
            steps: {
              js: ['concat','uglifyjs'],
              css: ['cssmin']
            },
            post: {}
          }
        }
      }
    },

    // Performs rewrites based on rev and the useminPrepare configuration
    usemin: {
      html: ['<%= app.target %>/{,*/}*.html'],
      css: ['<%= app.target %>/styles/{,*/}*.css'],
      options: {
        assetsDirs: ['<%= app.target %>']
      }
    },

    cssmin: {
      options: {
        root: '<%= app.src %>'
      }
    },

    // ngmin tries to make the code safe for minification automatically by
    // using the Angular long form for dependency injection. It doesn't work on
    // things like resolve or inject so those have to be done manually.
    ngmin: {
      target: {
        files: [{
          expand: true,
          cwd: '.tmp/concat/scripts',
          src: '*.js',
          dest: '.tmp/concat/scripts'
        }]
      }
    },

    // Add vendor prefixed styles
    autoprefixer: {
      options: {
        browsers: ['last 1 version']
      },
      target: {
        files: [{
          expand: true,
          cwd: 'styles/',
          src: '{,*/}*.css',
          dest: 'styles/'
        }]
      }
    },

    // Renames files for browser caching purposes
    rev: {
      target: {
        files: {
          src: [
            '<%= app.target %>/scripts/{,*/}*.js',
            '<%= app.target %>/styles/{,*/}*.css',
            '<%= app.target %>/images/{,*/}*.{png,jpg,jpeg,gif,webp,svg}',
            '<%= app.target %>/styles/fonts/*'
          ]
        }
      }
    },

    imagemin: {
      target: {
        files: [{
          expand: true,
          cwd: '<%= app.src %>/images',
          src: '{,*/}*.{png,jpg,jpeg,gif}',
          dest: '<%= app.target %>/images'
        }]
      }
    },

    svgmin: {
      target: {
        files: [{
          expand: true,
          cwd: '<%= app.src %>/images',
          src: '{,*/}*.svg',
          dest: '<%= app.target %>/images'
        }]
      }
    },

    // Replace Google CDN references
    cdnify: {
      options: {
        cdn: require('google-cdn-data')
      },
      dist: {
        html: ['<%= app.target %>/*.html']
      }
    },

    htmlmin: {
      target: {
        options: {
          collapseWhitespace: true,
          collapseBooleanAttributes: true,
          removeCommentsFromCDATA: true,
          removeOptionalTags: true
        },
        files: [{
          expand: true,
          cwd: '<%= app.target %>',
          src: ['*.html', 'views/{,*/}*.html'],
          dest: '<%= app.target %>'
        }]
      }
    },

    devUpdate: {
        main: {
            options: {
                updateType: 'force',
                reportUpdated: false,
                semver: false,
                packages: {
                    devDependencies: true,
                    dependencies: true
                },
                packageJson: null
            }
        }
    },


  });

  grunt.registerTask('prepareBuild', [
    'clean',
    'bowerInstall',
    'jshint',
    'compass',
    'imagemin',
    'svgmin',
  ]);

  grunt.registerTask('copyWithoutMinification', [
    'copy:sourcesForNonMinifiedVersion',
  ]);

  grunt.registerTask('copyAndMinify', [
    'copy:sourcesForMinifiedVersion',
    'cdnify',
    'useminPrepare',
    'autoprefixer',
    'concat',
    'ngmin',
    'cssmin',
    'uglify',
    'rev',
    'usemin',
    'htmlmin'
  ]);

  grunt.registerTask('dev', [
    'copy:devConfig',
    'prepareBuild',
    'copyWithoutMinification',
  ]);

  grunt.registerTask('devMinified', [
    'copy:devConfig',
    'prepareBuild',
    'copyAndMinify',
  ]);

  grunt.registerTask('default', [
    'dev'
  ]);
};
