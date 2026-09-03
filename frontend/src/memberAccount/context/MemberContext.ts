import { createContext } from 'react'
import type { Account } from '../../types'

interface MemberContextType {
  accounts: Account[]
  selectedAccount: Account | undefined
  onSelectAccount: (account: Account) => void
  isLoading: boolean
  error: Error | null
}

export const MemberContext = createContext<MemberContextType | undefined>(undefined)
