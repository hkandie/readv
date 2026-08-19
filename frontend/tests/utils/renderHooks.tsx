import { renderHook } from '@testing-library/react'
import type { RenderHookOptions, RenderHookResult } from '@testing-library/react'
import { QueryClientProvider } from '@tanstack/react-query'
import { createTestQueryClient } from './testQueryClient'
import { ApiProvider } from '../../src/auth/context/ApiProvider'

export function renderHooks<Result, Props>(
  hook: (props: Props) => Result,
  options?: Omit<RenderHookOptions<Props>, 'wrapper'>,
): RenderHookResult<Result, Props> {
  const queryClient = createTestQueryClient()

  return renderHook(hook, {
    ...options,
    wrapper: ({ children }) => (
      <QueryClientProvider client={queryClient}>
        <ApiProvider>{children}</ApiProvider>
      </QueryClientProvider>
    ),
  })
}
