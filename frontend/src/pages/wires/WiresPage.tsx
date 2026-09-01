import { Page } from '../../components'
import { usePageTitle } from '../../hooks/usePageTitle'

export function WiresPage() {
  usePageTitle('Wires')

  return (
    <Page>
      <h1 className="text-2xl font-semibold text-slate-900">Wires</h1>
      <p className="mt-2 text-sm text-slate-600">Wire details coming soon.</p>
    </Page>
  )
}
