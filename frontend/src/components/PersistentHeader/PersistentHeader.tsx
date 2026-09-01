import { CircleHelp } from 'lucide-react'
import { Link } from '@tanstack/react-router'
import logo from '../../assets/logo.jpg'
import { Navigation } from './Navigation'
import { AccountSelector } from './AccountSelector'
import { UserMenu } from './UserMenu'


export function PersistentHeader() {
  return (
    <header className="relative w-full">
      <Link to="/">
        <img
          src={logo}
          alt="FHLB Des Moines"
          className="absolute top-3 pl-8 z-10 w-28 shrink-0 object-contain"
        />
      </Link>

      <div className="flex items-center gap-8 bg-brand-sage pt-3 pr-6 pl-32">
        <Navigation />

        <div className="flex items-center gap-3">
          <button
            type="button"
            aria-label="Help"
            className="flex size-9 shrink-0 items-center justify-center rounded-full bg-brand-green text-white hover:bg-brand-green-dark"
          >
            <CircleHelp className="size-5" />
          </button>
          <UserMenu />
        </div>
      </div>

      <div className="flex flex-col gap-1 bg-white py-3 pr-6 pl-32 sm:flex-row sm:items-center sm:justify-between">
        <div className="flex items-baseline gap-3">
          <span className="text-2xl font-bold text-slate-900">Hello, Brandon</span>

          <AccountSelector />
        </div>
      </div>
    </header>
  )
}
