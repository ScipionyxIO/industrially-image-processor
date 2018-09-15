var path = require('path');

module.exports = {
    entry: './src/main/js/App.js',
    devtool: 'sourcemaps',
    cache: true,
    output: {
        path: __dirname,
        filename: './src/main/resources/static/built/bundle.js'
    },
    module: {
        loaders: [
            {
                test: /\.css$/,
                include: /(node_modules)/,
                loaders: ['style-loader', 'css-loader'],
            },
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                loader: 'babel-loader',
                query: {
                    cacheDirectory: true,
                    presets: ['es2015', 'react']
                }
            }]
    },
    resolve: {
        alias: {
            "ag-grid": path.resolve('./node_modules/ag-grid'),
            "bootstrap": path.resolve('./node_modules/bootstrap'),
            "react-bootstrap": path.resolve('./node_modules/react-bootstrap')
        }

    }
};