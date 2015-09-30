# Dev tools

## Nodejs and it's dependencies
 - [npm](https://nodejs.org/en/)
 - [git](https://git-scm.com/downloads)
## Sass processing
 - [ruby](https://www.ruby-lang.org/en/downloads/)
 - [sass](http://sass-lang.com/guide)
 - [compass](http://compass-style.org/install/)
 - Add **bin** folder from Ruby installation folder to **PATH** environment variable
## Grunt task runner
 - [grunt-cli](http://gruntjs.com/getting-started)
```sh
$ npm install -g grunt-cli
```
## Bower dependecy management tool
 - [bower](http://bower.io/)
```sh
$ npm install -g bower
```
#Build process
 - Install node modules
```sh
$ npm install
```
 - Install bower packages
```sh
$ bower install
```
 - Run grunt tasks
```sh
$ # To build dev version
$ grunt build
$ # To build prod version
$ grunt dist
$ # To build dev version and run web page
$ grunt serve:src
$ # To build prod version and run web page
$ grunt serve:dist
```
# Usefull commands
 - To initialize npm project and create **package.json** file 
```sh
$ npm init
```
 - To initialize bower project and create **bower.json** file
```sh
$ bower init
```
 - To install npm dependency and add it to **package.json**
```sh
$ npm install grunt --save-dev
```
> I recomend to use exact versions to avoid problems in production - final binary should not be changed unexpectedly
 - To install bower dependency and add it to **bower.json**
```sh
$ bower install bootstrap -S
```
> I recomend to use exact versions to avoid problems in production - final binary should not be changed unexpectedly
 - To check if new versions of node dependencies exists
```sh
$ npm-check-updates
``` 
 - To check if new versions of node dependencies exists and update **package.json**
```sh
$ npm-check-updates -u
```
 - Windows long file names [solution](https://coderwall.com/p/alhoww/solving-the-source-path-too-long-ntfs-issue) when trying to delete **node_modules** folder
```sh
$ npm i -g rimraf
$ rimraf node_modules
```
