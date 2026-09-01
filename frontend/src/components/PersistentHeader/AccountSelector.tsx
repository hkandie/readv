import { Dropdown } from '@fhlbdm/core-ui'
import { Check, ChevronDown } from 'lucide-react'
import { useMemberAccount } from '../../memberAccount/hooks/useMemberAccount'

export const AccountSelector = () => {
  const { selectedAccount, accounts, onSelectAccount } = useMemberAccount()

  if (!selectedAccount) {
    return null
  }

  return (
    <div>
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
  )
}
