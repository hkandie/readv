import { BaseHeader } from '@fhlbdm/core-ui'
import { Navigation } from './Navigation'
import { AccountSelector } from './AccountSelector'

export function AppHeader() {
  return (
    <BaseHeader navigation={<Navigation />}>      
      <AccountSelector />
    </BaseHeader>
  )
}
