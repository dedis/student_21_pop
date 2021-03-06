const createExpoWebpackConfigAsync = require('@expo/webpack-config');
const path = require('path');

const aliases = {
  test_data: path.resolve(__dirname, '../tests/data'),
  protocol: path.resolve(__dirname, '../protocol'),
};

async function configAsync(env, argv) {
  const config = await createExpoWebpackConfigAsync(env, argv);

  config.resolve.alias = {
    ...config.resolve.alias,
    ...aliases,
  };

  return config;
}

module.exports = configAsync;
