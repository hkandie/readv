import { afterEach, describe, expect, it, vi } from 'vitest'
import { waitFor } from '@testing-library/react'
import { http, HttpResponse } from 'msw'
import { server } from '../../mocks/server'
import { currentUser } from '../../mocks/handlers'
import { renderComponent } from '../../utils/renderComponent'
import { HomePage } from '../../../src/pages/HomePage/HomePage'

const getUserMe = vi.fn()

afterEach(() => {
  server.resetHandlers()
  getUserMe.mockClear()
  document.title = ''
})

describe('HomePage', () => {
  it('should set the page title and fire GET /api/user/me without rendering anything', async () => {
    server.use(
      http.get('*/api/user/me', ({ request }) => {
        getUserMe(request.method, new URL(request.url).pathname)
        return HttpResponse.json(currentUser)
      }),
    )

    renderComponent(<HomePage />)

    expect(document.title).toBe('ReAdvantage - Home')

    await waitFor(() => {
      expect(getUserMe).toHaveBeenCalledWith('GET', '/api/user/me')
    })
  })
})