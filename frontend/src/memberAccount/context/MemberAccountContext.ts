import { createContext } from 'react'
import type { Account } from '../../types'

interface MemberAccountContextType {
  accounts: Account[]
  selectedAccount: Account | undefined
  onSelectAccount: (account: Account) => void
  isLoading: boolean
  error: Error | null
}

export const MemberAccountContext = createContext<MemberAccountContextType | undefined>(undefined)
