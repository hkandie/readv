import { describe, expect, it, vi } from 'vitest'
import { renderHook } from '@testing-library/react'
import type { AxiosInstance } from 'axios'
import { useApi } from './useApi'
import { ApiContext } from '../auth/context/ApiContext'

describe('useApi', () => {
  it('should throw when used outside of an ApiProvider', () => {
    // React logs the render error via console.error, and jsdom's window "error" event
    // would otherwise be promoted to an unhandled process exception by Vitest — both expected here
    const consoleError = vi.spyOn(console, 'error').mockImplementation(() => {})
    const onWindowError = (event: Event) => event.preventDefault()
    window.addEventListener('error', onWindowError)

    expect(() => renderHook(() => useApi())).toThrow('useApi must be used within an ApiProvider')

    window.removeEventListener('error', onWindowError)
    consoleError.mockRestore()
  })

  it('should return the api instance when used within an ApiProvider', () => {
    const api = {} as AxiosInstance
    const { result } = renderHook(() => useApi(), {
      wrapper: ({ children }) => <ApiContext.Provider value={api}>{children}</ApiContext.Provider>,
    })

    expect(result.current).toBe(api)
  })
})
