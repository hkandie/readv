import { createContext, useContext } from 'react'
import { useCurrentUser } from './useCurrentUser'
import type { User } from './types'

interface UserContextType {
  user: User | undefined
  isLoading: boolean
  error: Error | null
}

const UserContext = createContext<UserContextType | undefined>(undefined)

export function UserProvider({ children }: { children: React.ReactNode }) {
  const { data: user, isPending: isLoading, error } = useCurrentUser()

  return (
    <UserContext.Provider value={{ user, isLoading, error: error as Error | null }}>
      {children}
    </UserContext.Provider>
  )
}

export function useUser() {
  const context = useContext(UserContext)
  if (context === undefined) {
    throw new Error('useUser must be used within a UserProvider')
  }
  return context
}
