import { useCurrentUser } from '../hooks/useCurrentUser'
import { UserContext } from './UserContext'

export function UserProvider({ children }: { children: React.ReactNode }) {
  const { data: user, isPending: isLoading, error } = useCurrentUser()

  return (
    <UserContext.Provider value={{ user, isLoading, error: error as Error | null }}>
      {children}
    </UserContext.Provider>
  )
}
