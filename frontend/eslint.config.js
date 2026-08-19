import js from '@eslint/js'
import globals from 'globals'
import reactHooks from 'eslint-plugin-react-hooks'
import reactRefresh from 'eslint-plugin-react-refresh'
import tseslint from 'typescript-eslint'
import eslintConfigPrettier from 'eslint-config-prettier'
import vitest from '@vitest/eslint-plugin'

export default tseslint.config(
  {
    ignores: ['dist', 'storybook-static', 'coverage', 'src/routeTree.gen.ts'],
  },
  {
    extends: [js.configs.recommended, ...tseslint.configs.recommended],
    files: ['**/*.{ts,tsx}'],
    languageOptions: {
      ecmaVersion: 2023,
      globals: globals.browser,
    },
    plugins: {
      'react-hooks': reactHooks,
      'react-refresh': reactRefresh,
    },
    rules: {
      ...reactHooks.configs.recommended.rules,
      'react-refresh/only-export-components': ['warn', { allowConstantExport: true }],
    },
  },
  {
    files: ['**/*.test.{ts,tsx}'],
    plugins: {
      vitest,
    },
    rules: {
      'vitest/valid-title': [
        'error',
        {
          mustMatch: {
            it: ['^(should|when|result)\\b', "Test titles must start with 'should', 'when', or 'result'."],
          },
        },
      ],
    },
  },
  eslintConfigPrettier,
)
