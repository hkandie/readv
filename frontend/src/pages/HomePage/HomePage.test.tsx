import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import { render, waitFor } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { HomePage } from './HomePage'

describe('HomePage', () => {
  beforeEach(() => {
    vi.stubGlobal(
      'fetch',
      vi.fn(() =>
        Promise.resolve({
          ok: true,
          json: () => Promise.resolve({ id: '1', name: 'Ada', email: 'ada@example.com' }),
        }),
      ) as unknown as typeof fetch,
    )
  })

  afterEach(() => {
    vi.unstubAllGlobals()
    document.title = ''
  })

  it('sets the page title and fires GET /api/user/me without rendering anything', async () => {
    const queryClient = new QueryClient()
    const { container } = render(
      <QueryClientProvider client={queryClient}>
        <HomePage />
      </QueryClientProvider>,
    )

    expect(document.title).toBe('ReadV')

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith(
        expect.stringContaining('/api/user/me'),
        expect.objectContaining({ method: 'GET' }),
      )
    })

    expect(container.querySelector('main')).toBeEmptyDOMElement()
  })
})
