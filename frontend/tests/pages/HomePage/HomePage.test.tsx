import { afterEach, describe, expect, it, vi } from 'vitest'
import { render, waitFor } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { http, HttpResponse } from 'msw'
import { server } from '../../mocks/server'
import { ApiProvider } from '../../../src/auth/context/ApiProvider'
import { HomePage } from '../../../src/pages/HomePage/HomePage'

const getUserMe = vi.fn()

afterEach(() => {
  getUserMe.mockClear()
  document.title = ''
})

describe('HomePage', () => {
  it('sets the page title and fires GET /api/user/me without rendering anything', async () => {
    server.use(
      http.get('*/api/user/me', ({ request }) => {
        getUserMe(request.method, new URL(request.url).pathname)
        return HttpResponse.json({ id: '1', name: 'Ada', email: 'ada@example.com' })
      }),
    )

    const queryClient = new QueryClient()
    const { container } = render(
      <QueryClientProvider client={queryClient}>
        <ApiProvider>
          <HomePage />
        </ApiProvider>
      </QueryClientProvider>,
    )

    expect(document.title).toBe('ReAdvantage - Home')

    await waitFor(() => {
      expect(getUserMe).toHaveBeenCalled()
    })

    expect(getUserMe).toHaveBeenCalledWith('GET', '/readvantage/api/user/me')

    expect(container.querySelector('main')).toBeEmptyDOMElement()
  })
})
