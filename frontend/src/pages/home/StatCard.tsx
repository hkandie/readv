import { Card } from '@fhlbdm/core-ui'

export interface StatCardProps {
  title: string
  value?: string
  subtitle?: string
}

export function StatCard({ title, value, subtitle }: StatCardProps) {
  return (
    <Card>
      <p className="text-sm font-medium text-slate-500">{title}</p>
      {value ? (
        <>
          <p className="mt-1 text-2xl font-semibold text-slate-900">{value}</p>
          {subtitle ? <p className="mt-1 text-xs text-slate-500">{subtitle}</p> : null}
        </>
      ) : (
        <p className="mt-1 text-sm text-slate-400">No data yet</p>
      )}
    </Card>
  )
}
