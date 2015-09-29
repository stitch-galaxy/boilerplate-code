'use strict';

module.exports = function(grunt) {

    //Used to load grunt tasks automatically
    require('load-grunt-tasks')(grunt);

    //Used to measure how long grunt tasks run
    require('time-grunt')(grunt);

    grunt.initConfig({
        //Used to install bower dependencies
        bower: {
            install: {
                options: {
                    targetDir: 'app/bower_components'
                }
            }
        },
        // Used to check javascript syntax
        jshint: {
            options: {
                jshintrc: '.jshintrc'
            },
            src: {
                src: [
                    'Gruntfile.js',
                    'app/components/**/*.js'
                ]
            }
        },
        //Used to wire bower dependencies
        //Place following code to your html files you want to inject bower dependencies
        //<!-- bower:css -->
        //<!-- endbower -->
        //<!-- bower:js -->
        //<!-- endbower -->
        wiredep: {
            src: {
                src: 'app/*.html'
            }
        },
        //Used to compile sass code
        compass: {
            src: {
                options: {
                    sassDir: 'app/sass',
                    cssDir: 'app/css'
                }
            }
        },
        //Used to add vendor specific prefixes to css files
        autoprefixer: {
            options: {
                browsers: ['last 1 version']
            },
            src: {
                src: 'app/css/**/*.css'
            }
        },
        //Used to spawn connect web server
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
                    base: 'src'
                }
            },
            dist: {
                options: {
                    open: true,
                    base: 'dist',
                    keepalive: true
                }
            }
        },
        //Used to clean temporary files
        clean: {
            dist: {
                src: 'dist'
            },
            useminCache: {
                src: '.tmp'
            },
            sassCache: {
                src: '.sass-cache'
            },
            generatedCss: {
                src: 'app/css'
            }
        },
        //Used to spawn other grunt tasks when files changed
        watch: {
            //spawn bower install and wire dependencies
            bower: {
                files: ['bower.json'],
                tasks: ['bower', 'wiredep']
            },
            //spawn js checks when sources changed
            js: {
                files: ['app/components/**/*.js'],
                tasks: ['newer:jshint']
            },
            //compile sass code whe sources changed
            sass: {
                files: ['app/sass/**/*.{scss,sass}'],
                tasks: ['clean:sassCache', 'clean:generatedCss', 'compass', 'autoprefixer']
            },
            //reload web page when files changed
            files: {
                options: {
                    livereload: '<%= connect.options.livereload %>'
                },
                files: [
                    'app/*.html', //html
                    'app/partials/**/*.html', //partials
                    'app/css/**/*.css', //css styles
                    'app/images/**/*', //images
                    'app/components/**/*.js'//scripts
                ]
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
        uglify: {
            options: {
                sourceMap: true
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
        htmlmin: {
            dist: {
                options: {
                    collapseWhitespace: true,
                    conservativeCollapse: true,
                    collapseBooleanAttributes: true,
                    removeCommentsFromCDATA: true,
                    removeOptionalTags: true
                },
                files: [{
                        expand: true,
                        cwd: '<%= sg.dist %>',
                        src: ['*.html', '<%= sg.partials %>/**/*.html'],
                        dest: '<%= sg.dist %>'
                    }]
            }
        }
    });

    //add watch commands to compass and autoprefixer

    //build process
    grunt.registerTask('build', [
        'jshint',
        'clean:sassCache',
        'clean:generatedCss',
        'compass',
        'autoprefixer',
        'bower',
        'wiredep'
    ]);

    //dist process: take html, concat js'es and css'es, cssmin css'es, uglify js'es, 
    grunt.registerTask('dist', [
        'build',
        'clean:dist',
        'clean:useminCache',
        'copy', //related to usemin as soon as usemin replace <build></build> blocks
        'imagemin', //related to filerev=>usemin as soon as filerev rename minified and copied files
        'useminPrepare',
        'concat',
        'ngAnnotate',
        'uglify',
        'cssmin',
        'filerev',
        'usemin',
        'htmlmin'
    ]);

    grunt.registerTask('serve', 'Compile then start a connect web server', function(target) {
        if (target === 'dist') {
            return grunt.task.run(['dist', 'connect:dist']);
        }
        if (target === 'src') {
            return grunt.task.run(['build', 'connect:src', 'watch']);
        }
        grunt.log.warn('Please specify dist or src target!');
    });

};
