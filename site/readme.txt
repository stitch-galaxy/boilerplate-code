--node as well as bower use semver dependencies versions syntax
https://github.com/npm/node-semver

use exact version 2.0.1 syntax(2.0.x syntax allowed(incorporate new patches only))

To update dependencies use following commands:

npm install npm-check-updates -g

--check if new versions of node dependencies exists
npm-check-updates

--update package.json
npm-check-updates -u

--get specific module latest version
npm view protractor version

Node use MSBuild tool and VS 2010 to build some native dependencies:
https://github.com/TooTallNate/node-gyp/
It generate vcxproj files and build them - use vs 2010 if you build fails
