import type { AxiosInstance } from 'axios'
import axios from 'axios'
import { createContext, useMemo } from 'react'
import React from 'react'
import { v4 as uuidv4 } from 'uuid'

interface ApiClientProviderType {
  client: AxiosInstance
}

export const ApiContext = createContext<ApiClientProviderType | undefined>(undefined)

const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? ''

export function ApiProvider({ children }: { children: React.ReactNode }) {
  const client = useMemo(() => {
    const instance = axios.create({
      baseURL: BASE_URL,
      adapter: 'fetch',
      headers: {
        Accept: 'application/json',
      },
    })
    instance.interceptors.request.use((config) => {
      config.headers.set('X-Request-ID', uuidv4())
      const fakeToken = btoa(JSON.stringify({ sub: '13243', email: 'fake@fh' }))

      config.headers.set('Authorization', `Bearer ${fakeToken}`)

      return config
    })
    return instance
  }, [])

  return <ApiContext.Provider value={client}>{children}</ApiContext.Provider>
}
