import { Card } from '@fhlbdm/core-ui'

type WireStatus = 'Complete' | 'Pending' | 'Cancelled'

interface WireEntry {
  id: string
  amount: string
  status: WireStatus
}

const WIRES: WireEntry[] = [
  { id: 'w1', amount: '$1,000.00', status: 'Complete' },
  { id: 'w2', amount: '$2,500.00', status: 'Pending' },
  { id: 'w3', amount: '$500.00', status: 'Cancelled' },
]

const STATUS_STYLES: Record<WireStatus, string> = {
  Complete: 'bg-emerald-100 text-emerald-700',
  Pending: 'bg-amber-100 text-amber-700',
  Cancelled: 'bg-red-100 text-red-700',
}

export function WiresCard() {
  return (
    <Card>
      <p className="text-sm font-medium text-slate-500">Wires</p>
      <ul className="mt-3 flex flex-col gap-3">
        {WIRES.map((wire) => (
          <li key={wire.id} className="flex items-center justify-between">
            <span className="text-sm font-medium text-slate-900">{wire.amount}</span>
            <span
              className={`rounded-full px-2 py-0.5 text-xs font-medium ${STATUS_STYLES[wire.status]}`}
            >
              {wire.status}
            </span>
          </li>
        ))}
      </ul>
    </Card>
  )
}
