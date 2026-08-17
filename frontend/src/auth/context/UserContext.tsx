import { createContext } from 'react'
import type { User } from '../../types'
import { useCurrentUser } from '../hooks/useCurrentUser'
interface UserContextType {
  user: User | undefined
  isLoading: boolean
  error: Error | null
}

export const UserContext = createContext<UserContextType | undefined>(undefined)

export function UserProvider({ children }: { children: React.ReactNode }) {
  const { data: user, isPending: isLoading, error } = useCurrentUser()

  return (
    <UserContext.Provider value={{ user, isLoading, error: error as Error | null }}>
      {children}
    </UserContext.Provider>
  )
}
