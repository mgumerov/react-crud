Can use node.js http-server for serving this; or nginx, etc

Prepare env with
npm install --save-dev webpack
npm install --save-dev babel
npm install --save-dev babel-loader
npm install --save-dev babel-core
npm install --save-dev babel-preset-react
and for actually calling ReactDOM.render:
npm install --save-dev react
npm install --save-dev react-dom
npm install --save-dev axios
for bootstrap modal:
npm install --save react-bootstrap

Build with
node_modules\.bin\webpack test-index.jsx bundle.js
