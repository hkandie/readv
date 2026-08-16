import React, { createContext, useContext } from 'react'
import { v4 as uuidv4 } from 'uuid'
import { setApiHeaders } from './client'

interface ApiContextType {
  requestId: string
}

const ApiContext = createContext<ApiContextType | undefined>(undefined)

export function ApiProvider({ children }: { children: React.ReactNode }) {
  const [requestId] = React.useState(() => uuidv4())

  React.useEffect(() => {
    setApiHeaders(requestId)
  }, [requestId])

  return (
    <ApiContext.Provider value={{ requestId }}>
      {children}
    </ApiContext.Provider>
  )
}

export function useApiHandler() {
  const context = useContext(ApiContext)
  if (!context) {
    throw new Error('useApiHandler must be used within ApiProvider')
  }
  return context
}

export function useRequestId() {
  return useApiHandler().requestId
}
