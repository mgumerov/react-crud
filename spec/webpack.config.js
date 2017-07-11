mainConfig = require("../webpack.config.js");
var path = require('path');

var m = Object.assign({}, mainConfig);

delete m.externals;
m.resolve.modules = [path.resolve(".."), path.resolve("../node_modules")];

module.exports = m;
