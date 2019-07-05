module.exports = {
  root: true,

  env: {
    node: true,
  },

  extends: [
    'plugin:vue/strongly-recommended',
    '@vue/airbnb',
  ],

  rules: {
    'no-console': 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'vue/component-name-in-template-casing': 'off',
    'vue/singleline-html-element-content-newline': 'off',
    'vue/max-attributes-per-line': 'off',
    'import/extensions': 'off',
    'no-param-reassign': 'off',
    'import/no-extraneous-dependencies': 'off',
    'consistent-return': 'off',
    'max-len': 'off',
    'no-unused-vars': 'off',
    'no-plusplus': 'off',
    'no-return-assign': 'off',
  },

  parserOptions: {
    parser: 'babel-eslint',
  },

};
