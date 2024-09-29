// Seems to fix the issue in
// https://youtrack.jetbrains.com/issue/KT-70904/K-JS-ES2015-causes-large-bundle-size-increase-with-Ktor-client
config.optimization = {
    ...config.optimization,
    concatenateModules: false
};
