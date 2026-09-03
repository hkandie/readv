import { Dropdown } from '@fhlbdm/core-ui'
import { Check, ChevronDown } from 'lucide-react'
import { useMemberAccount } from '../../memberAccount/hooks/useMemberAccount'

export const AccountSelector = () => {
  const { selectedAccount, accounts, onSelectAccount } = useMemberAccount()

  if (!selectedAccount) {
    return null
  }

  return (
    <div className="flex flex-col gap-1 bg-white py-3 pr-6 pl-32 sm:flex-row sm:items-center sm:justify-between">
      <div className="flex items-baseline gap-3">
        <span className="text-2xl font-bold text-slate-900">Hello, Brandon</span>

        <Dropdown
          align="left"
          ariaLabel="Select organization"
          label={
            <span className="flex items-center gap-1 text-sm text-slate-500">
              {selectedAccount.name}
              <ChevronDown className="size-4" />
            </span>
          }
        >
          {accounts.map((account) => (
            <Dropdown.Item key={account.id} onClick={() => onSelectAccount(account)}>
              <span className="w-4">
                {account.id === selectedAccount.id ? <Check className="size-4" /> : null}
              </span>
              {account.name}
            </Dropdown.Item>
          ))}
        </Dropdown>
        <p># {selectedAccount.id}</p>
      </div>
    </div>
  )
}
