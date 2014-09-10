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
        useminCache: '.tmp',
        sassCache: '.sass-cache',
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
                    '<%= sg.test %>/**/*.js'
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
        copy: {
            dist: {
                files: [
                    {
                        expand: true,
                        cwd: '<%= sg.src %>',
                        src: [
                            '<%= sg.images %>/**/*',
                            '*.html',
                            '<%= sg.partials %>/**/*.html'
                        ],
                        dest: '<%= sg.dist %>'
                    }
                ]
            }
        },
        imagemin: {
            dist: {
                files: [{
                        expand: true,
                        cwd: '<%= sg.src %>/<%= sg.images %>',
                        src: '**/*.{png,jpg,jpeg,gif}',
                        dest: '<%= sg.dist %>/<%= sg.images %>'
                    }]
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
        ngAnnotate: {
            options: {
                singleQuotes: true
            },
            dist: {
                src: '<%= sg.useminCache %>/concat/<%= sg.javascript %>/*.js'
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
                        exapand: true,
                        //cwd: '<%= sg.dist %>', //not working with filerev
                        src: [
                            '<%= sg.dist %>/<%= sg.images %>/**/*',
                            '<%= sg.dist %>/<%= sg.javascript %>/*.js',
                            '<%= sg.dist %>/<%= sg.css %>/*.css'
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
        connect: {
            options: {
                port: 9000,
                // Change this to '0.0.0.0' to access the server from outside.
                hostname: 'localhost',
                livereload: 35729
            },
            src: {
                options: {
                    open: true,
                    base: '<%= sg.src %>'
                }
            },
            test: {
                options: {
                    port: 9001,
                    base: '<%= sg.src %>'
                }
            },
            dist: {
                options: {
                    open: true,
                    base: '<%= sg.dist %>'
                }
            }
        },
        watch: {
            bower: {
                files: ['bower.json'],
                tasks: ['bower', 'wiredep']
            },
            js: {
                files: ['<%= sg.src %>/<%= sg.components %>/**/*.js',
                    '<%= sg.test %>/**/*.js'],
                tasks: ['newer:jshint']
            },
            sass: {
                files: ['<%= sg.src %>/<%= sg.sass %>/**/*.{scss,sass}'],
                tasks: ['compass', 'autoprefixer']
            },
            gruntfile: {
                files: ['Gruntfile.js']
            },
            files: {
                options: {
                    livereload: '<%= connect.options.livereload %>'
                },
                files: [
                    '<%= sg.src %>/*.html', //html
                    '<%= sg.partials %>/**/*.html', //partials
                    '<%= sg.src %>/<%= sg.css %>/*.css', //css styles
                    '<%= sg.dist %>/<%= sg.images %>/**/*', //images
                    '<%= sg.src %>/<%= sg.components %>/**/*.js'//scripts
                ]
            }
        }
    });

    //add watch commands to compass and autoprefixer

    //build process
    grunt.registerTask('build', [
        'bower',
        'wiredep',
        'jshint',
        'compass',
        'autoprefixer'
    ]);

    //dist process: take html, concat js'es and css'es, cssmin css'es, uglify js'es, 
    grunt.registerTask('dist', [
        'build',
        'clean:dist',
        'clean:useminCache',
        'copy',
        'imagemin',
        'useminPrepare',
        'concat',
        'ngAnnotate',
        'uglify',
        'cssmin',
        'filerev',
        'usemin'
    ]);

    grunt.registerTask('serve', 'Compile then start a connect web server', function(target) {
        if (target === 'dist') {
            return grunt.task.run(['dist', 'connect:dist:keepalive']);
        }
        if (target === 'src') {
            return grunt.task.run(['build', 'connect:src', 'watch']);
        }
        grunt.log.warn('Please specify dist or src target!');
    });

};
