'use strict';

module.exports = function(grunt) {

    // Load grunt tasks automatically
    require('load-grunt-tasks')(grunt);

    // Time how long tasks take. Can help when optimizing build times
    require('time-grunt')(grunt);

    // Configurable paths for the application
    var appConfig = {
        src: 'app',
        dist: 'dist',
        test: 'test',
        useminCache: './.tmp',
        sassCache: './.sass-cache',
        bowerComponents: 'bower_components',
        components: 'components',
        partials: 'partials',
        sass: 'sass',
        css: 'css',
        images: 'images',
        javascript: 'scripts'
    };

    grunt.initConfig({
        sg: appConfig,
        bower: {
            install: {
                options: {
                    targetDir: '<%= sg.src %>/<%= sg.bowerComponents %>'
                }
            }
        },
        jshint: {
            options: {
                jshintrc: '.jshintrc'
            },
            src: {
                src: [
                    'Gruntfile.js',
                    '<%= sg.src %>/<%= sg.components %>/**/*.js',
                    '<%= sg.test %>/**/*.js',
                ]
            }
        },
        wiredep: {
            src: {
                src: '<%= sg.src %>/*.html'
            }
        },
        compass: {
            src: {
                options: {
                    sassDir: '<%= sg.src %>/<%= sg.sass %>',
                    cssDir: '<%= sg.src %>/<%= sg.css %>'
                }
            }
        },
        autoprefixer: {
            options: {
                browsers: ['last 1 version']
            },
            src: {
                src: '<%= sg.src %>/<%= sg.css %>/*.css'
            }
        },
        clean: {
            dist: {
                src: '<%= sg.dist %>'
            },
            useminCache: {
                src: '<%= sg.useminCache %>'
            },
            sassCache: {
                src: '<%= sg.sassCache %>'
            },
            generatedCss: {
                src: '<%= sg.src %>/<%= sg.css %>'
            }
        },
        useminPrepare: {
            src: '<%= sg.src %>/*.html',
            options: {
                dest: '<%= sg.dist %>'
            }
        },
        concat: {
            options: {
                separator: ';'
            }
        },
        copy: {
            dist: {
                files: [
                    {
                        expand: true,
                        cwd: '<%= sg.src %>',
                        src: [
                            '<%= sg.images %>/**/*',
                            '*.html',
                            '<%= sg.partials %>/**/*.html',
                        ],
                        dest: '<%= sg.dist %>'
                    }
                ]
            }
        },
        filerev: {
            options: {
                encoding: 'utf8',
                algorithm: 'md5',
                length: 20
            },
            dist: {
                files: [{
                        cwd: '<%= sg.dist %>',
                        src: [
                            '<%= sg.images %>/**/*',
                            '<%= sg.javascript %>/*.js',
                            '<%= sg.css %>/*.css',
                        ]
                    }]
            }
        },
        usemin: {
            html: '<%= sg.dist %>/*.html',
            css: '<%= sg.dist %>/<%= sg.css %>/**/*.css',
            options: {
                assetsDirs: ['<%= sg.dist %>']
            }
        },
        ngAnnotate: {
            src: {
                files: [{
                        src: ['<%= sg.src %>/components/**/*.js'],
                    }]
            }
        },
    });

    //add watch commands to compass and autoprefixer

    //build process
    grunt.registerTask('build', ['bower:install', 'jshint', 'wiredep', 'compass', 'autoprefixer']);

    //dist process: take html, concat js'es and css'es, cssmin css'es, uglify js'es, 
    grunt.registerTask('dist', [/*'build', */
        'clean:dist',
        'clean:useminCache',
        'useminPrepare',
        'concat',
        'uglify',
        'cssmin',
        'copy',
        'filerev',
        'usemin'
    ]);

};
