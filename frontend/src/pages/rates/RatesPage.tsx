 import { Page } from '@fhlbdm/core-ui'
import { usePageTitle } from '../../hooks/usePageTitle'

export function RatesPage() {
  usePageTitle('Rates')

  return (
    <Page>
      <h1 className="text-2xl font-semibold text-slate-900">Rates</h1>
      <p className="mt-2 text-sm text-slate-600">Rate details coming soon.</p>
    </Page>
  )
}
