import { Link } from '@tanstack/react-router'

const NAV_LINKS = [
  { to: '/', label: 'Home' },
  { to: '/dashboard/overview', label: 'Overview' },
  { to: '/dashboard/reports', label: 'Reports' },
  { to: '/dashboard/settings', label: 'Settings' },
] as const

export function PersistentHeader() {
  return (
    <header className="flex h-14 w-full items-center gap-6 border-b border-slate-200 px-4">
      <nav className="flex items-center gap-6">
        {NAV_LINKS.map((link) => (
          <Link
            key={link.to}
            to={link.to}
            className="text-sm text-slate-600 hover:text-slate-900"
            activeProps={{ className: 'text-sm font-semibold text-slate-900' }}
          >
            {link.label}
          </Link>
        ))}
      </nav>
    </header>
  )
}
