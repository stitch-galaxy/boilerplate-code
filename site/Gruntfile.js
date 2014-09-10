'use strict';

module.exports = function(grunt) {

    // Load grunt tasks automatically
    require('load-grunt-tasks')(grunt);

    // Time how long tasks take. Can help when optimizing build times
    require('time-grunt')(grunt);

    // Configurable paths for the application
    var appConfig = {
        src: require('./bower.json').appPath,
        dist: 'dist',
        test: 'test',
        tmp: './tmp',
        sass_cache: './.sass-cache'
    };

    grunt.initConfig({
        sg: appConfig,
        bower: {
            install: {
                options: {
                    targetDir: '<%= sg.src %>/bower_components'
                }
            }
        },
        jshint: {
            options: {
                jshintrc: '.jshintrc',
                reporter: require('jshint-stylish')
            },
            src: {
                src: [
                    'Gruntfile.js',
                    '<%= sg.src %>/components/**/*.js',
                    '<%= sg.test %>/**/*.js',
                ]
            }
        },
        wiredep: {
            src: {
                src: [
                    '<%= sg.src %>/*.html',
                    '<%= sg.src %>/partials/**/*.html'
                ]
            }
        },
        compass: {
            src: {
                options: {
                    sassDir: '<%= sg.src %>/sass',
                    cssDir: '<%= sg.src %>/css'
                }
            }
        },
        autoprefixer: {
            options: {
                browsers: ['last 1 version']
            },
            src: {
                files: [{
                        src: ['<%= sg.src %>/css/*.css']
                    }]
            }
        },
        ngAnnotate: {
            src: {
                files: [{
                        src: ['<%= sg.src %>/components/**/*.js'],
                    }]
            }
        },
        clean: {
            dist: {
                files: [{
                        dot: true,
                        src: [
                            '<%= sg.dist %>/**/*',
                            '<%= sg.tmp %>/**/*',
                            '<%= sg.sass_cache %>/**/*'
                        ]
                    }]
            }
        },
    });

    //add watch commands to compass and autoprefixer
    grunt.registerTask('build', ['newer:jshint', 'bower:install', 'wiredep', 'compass', 'autoprefixer', 'ngAnnotate']);
    //dist process: take html, concat js'es and css'es, cssmin css'es, uglify js'es, 

    grunt.registerTask('dist', ['build', 'clean']);

};
