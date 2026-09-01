import { useState } from 'react'
import type { Account } from '../../types'
import { useMemberAccounts } from '../hooks/useMemberAccounts'
import { MemberAccountContext } from './MemberAccountContext'

export function MemberAccountProvider({ children }: { children: React.ReactNode }) {
  const { data: accounts = [], isPending: isLoading, error } = useMemberAccounts()
  const [selectedAccount, setSelectedAccount] = useState<Account | undefined>(undefined)

  return (
    <MemberAccountContext.Provider
      value={{
        accounts,
        selectedAccount: selectedAccount ?? accounts[0],
        onSelectAccount: setSelectedAccount,
        isLoading,
        error: error as Error | null,
      }}
    >
      {children}
    </MemberAccountContext.Provider>
  )
}
