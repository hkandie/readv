import type { ReactElement } from 'react'
import { render } from '@testing-library/react'
import type { RenderOptions, RenderResult } from '@testing-library/react'
import { QueryClientProvider } from '@tanstack/react-query'
import { createRootRoute, createRouter, RouterProvider } from '@tanstack/react-router'
import { createTestQueryClient } from './testQueryClient'
import { ApiProvider } from '../../src/auth/context/ApiProvider'

export function renderComponent(
  ui: ReactElement,
  options?: Omit<RenderOptions, 'wrapper'>,
): RenderResult {
  const queryClient = createTestQueryClient()
  const rootRoute = createRootRoute({ component: () => ui })
  const router = createRouter({ routeTree: rootRoute })

  return render(<RouterProvider router={router} />, {
    ...options,
    wrapper: ({ children }) => (
      <QueryClientProvider client={queryClient}>
        <ApiProvider>{children}</ApiProvider>
      </QueryClientProvider>
    ),
  })
}
