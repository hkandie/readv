import { Link } from '@tanstack/react-router'

const NAV_LINKS = [
  { label: 'Home', to: '/' },
  { label: 'Rates', to: '/rates' },
  { label: 'Advances' },
  { label: 'Wires', to: '/wires' },
  { label: 'Collateral' },
  { label: 'Statements' },
  { label: 'Capital Stock' },
  { label: 'Deposit Accounts' },
  { label: 'Admin' },
  { label: 'Other Systems' },
] as const

export const Navigation = () => {
  return (
    <nav className="flex flex-1 flex-wrap items-center justify-end gap-x-5 gap-y-1">
      {NAV_LINKS.map((link) =>
        'to' in link ? (
          <Link
            key={link.label}
            to={link.to}
            className="text-sm text-slate-700 hover:text-slate-900"
            activeProps={{ className: 'text-sm font-semibold text-slate-900' }}
          >
            {link.label}
          </Link>
        ) : (
          <span key={link.label} className="text-sm text-slate-700">
            {link.label}
          </span>
        ),
      )}
    </nav>
  )
}

