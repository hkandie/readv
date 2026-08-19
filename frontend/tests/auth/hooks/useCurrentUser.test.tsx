import { describe, expect, it } from 'vitest'
import { waitFor } from '@testing-library/react'
import { currentUser } from '../../mocks/handlers'
import { renderHooks } from '../../utils/renderHooks'
import { useCurrentUser } from '../../../src/auth/hooks/useCurrentUser'

describe('useCurrentUser', () => {
  it('should resolve to the response body from GET /api/user/me', async () => {
    const { result } = renderHooks(() => useCurrentUser())

    await waitFor(() => expect(result.current.isSuccess).toBe(true))

    expect(result.current.data).toEqual(currentUser)
  })
})
