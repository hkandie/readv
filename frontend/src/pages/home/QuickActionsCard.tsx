import type { ComponentType } from 'react'
import { Link } from '@tanstack/react-router'
import { Percent, Send, ListChecks, FileText, ShieldCheck } from 'lucide-react'
import { Card } from '@fhlbdm/core-ui'

interface QuickAction {
  label: string
  icon: ComponentType<{ className?: string }>
  to?: '/rates' | '/wires'
}

const QUICK_ACTIONS: QuickAction[] = [
  { label: 'View Rates', icon: Percent, to: '/rates' },
  { label: 'Create a Wire', icon: Send, to: '/wires' },
  { label: 'View Previous Advances', icon: ListChecks },
  { label: 'Statements', icon: FileText },
  { label: 'Pledge or Release Collateral', icon: ShieldCheck },
]

export function QuickActionsCard() {
  return (
    <Card>
      <p className="text-sm font-medium text-slate-500">Quick actions</p>
      <ul className="mt-3 flex flex-col gap-1">
        {QUICK_ACTIONS.map(({ label, icon: Icon, to }) => (
          <li key={label}>
            {to ? (
              <Link
                to={to}
                className="flex items-center gap-2 rounded-md px-2 py-1.5 text-sm text-slate-700 hover:bg-slate-50"
              >
                <Icon className="size-4 text-emerald-700" />
                <span>{label}</span>
              </Link>
            ) : (
              <span className="flex items-center gap-2 rounded-md px-2 py-1.5 text-sm text-slate-700">
                <Icon className="size-4 text-emerald-700" />
                <span>{label}</span>
              </span>
            )}
          </li>
        ))}
      </ul>
    </Card>
  )
}
