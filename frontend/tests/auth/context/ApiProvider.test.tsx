import { afterEach, describe, expect, it, vi } from 'vitest'
import { renderHook } from '@testing-library/react'

describe('ApiProvider', () => {
  afterEach(() => {
    vi.unstubAllEnvs()
  })

  it('should default the base URL to an empty string when VITE_API_BASE_URL is unset', async () => {
    vi.stubEnv('VITE_API_BASE_URL', undefined)
    vi.resetModules()

    const { ApiProvider } = await import('../../../src/auth/context/ApiProvider')
    const { useApi } = await import('../../../src/hooks/useApi')

    const { result } = renderHook(() => useApi(), { wrapper: ApiProvider })

    expect(result.current.defaults.baseURL).toBe('')
  })
})
