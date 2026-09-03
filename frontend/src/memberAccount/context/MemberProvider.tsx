import { useState } from 'react'
import type { Account } from '../../types'
import { useAccounts } from '../hooks/useAccounts'
import { MemberContext } from './MemberContext'

export function MemberProvider({ children }: { children: React.ReactNode }) {
  const { data: accounts = [], isPending: isLoading, error } = useAccounts()
  const [selectedAccount, setSelectedAccount] = useState<Account | undefined>(undefined)

  return (
    <MemberContext.Provider
      value={{
        accounts,
        selectedAccount: selectedAccount ?? accounts[0],
        onSelectAccount: setSelectedAccount,
        isLoading,
        error: error as Error | null,
      }}
    >
      {children}
    </MemberContext.Provider>
  )
}
