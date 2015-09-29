# Dev tools
 - git
 - npm
 - grunt-cli
```sh
$ npm install -g grunt-cli
```
 - bower
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
# Usefull commands
 - To initialize npm project and create **package.json** file 
```sh
$ npm init
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
# Grunt tasks
 - load-grunt-tasks
```
    // Load grunt tasks automatically
    require('load-grunt-tasks')(grunt);
```
 - time-grunt
```
    // Time how long tasks take. Can help when optimizing build times
    require('time-grunt')(grunt);
```
 - grunt-contrib-jshint

 - grunt-contrib-concat
