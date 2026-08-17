import { createContext } from 'react'
import type { User } from '../../types'

interface UserContextType {
  user: User | undefined
  isLoading: boolean
  error: Error | null
}

export const UserContext = createContext<UserContextType | undefined>(undefined)
