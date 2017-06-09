module.exports = {
    module: {
        loaders: [
          {
            test: /.jsx?$/,
            loader: 'babel-loader',
            query: {
              presets: ['react']
            }
          }
        ]
    },
    resolve: {
        extensions: ['.js','.jsx']
    },
    //Use external version instead of webpack-controlled one, to speed up bundle creation
    externals: {
        'react': 'React',
        'react-dom': 'ReactDOM',
        'axios': 'axios'
    }
}
