import { usePageTitle } from '../../hooks/usePageTitle'
import { StatCard } from './StatCard'
import { WiresCard } from './WiresCard'
import { QuickActionsCard } from './QuickActionsCard'
import { Page } from '@fhlbdm/core-ui'

export function HomePage() {
  usePageTitle('Home')

  return (
    <Page>
      <p className="text-xs text-slate-400">{'Data updated: 06/17/2026 5:45 PM'}</p>
      <div className="grid grid-cols-1 gap-4 lg:grid-cols-4 py-4">
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:col-span-2">
          <StatCard title="SOFR Advance" value="$1,000,000.00" subtitle="Outstanding balance" />
          <StatCard
            title="Daily Reset Advance"
            value="$250,000.00"
            subtitle="Outstanding balance"
          />
          <StatCard title="Fixed Advance" />
          <StatCard title="Capital Stock" />
          <StatCard title="Collateral" />
        </div>
        <WiresCard />
        <QuickActionsCard />
      </div>
    </Page>
  )
}
