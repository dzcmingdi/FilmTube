const path = require('path');
const VueLoaderPlugin = require('vue-loader/lib/plugin')
const idea_path = 'D:\\IdeaProjects\\FilmTube\\src\\main\\resources\\static\\js';
const this_path = 'dist'
module.exports = {
    mode: 'production',
    entry: {
        index: "./src/index/index.js",
        upload: "./src/upload/upload.js",
        // film:"./src/film.js",
        // test:"./src/test/video-player.js"
    },
    output: {
        filename: "[name].js",
        path: path.resolve(__dirname, idea_path),
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env']
                    }
                },
                exclude: /node_modules/
            },
            {
                test: /\.vue$/,
                loader: 'vue-loader',
                exclude: /node_modules/
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader'],
            },
            {
                test: /\.s(c|a)ss$/,
                use: [
                    'vue-style-loader',
                    'css-loader',
                    {
                        loader: 'sass-loader',
                        // Requires sass-loader@^8.0.0
                        options: {
                            implementation: require('sass'),
                            sassOptions: {
                                indentedSyntax: true // optional
                            },
                        },
                    },
                ],
            },
            {
                test: /\.(png|jpg|ico|gif)$/,
                loader: 'url-loader',
                exclude: /node_modules/
            }

        ]
    },
    plugins: [
        new VueLoaderPlugin()
    ],
    resolve: {
        alias: {
            'vue$': 'vue/dist/vue.esm.js'
        }
    },
    watch: true
}