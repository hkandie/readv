import type { ReactNode } from 'react'
import { CircleHelp, User } from 'lucide-react'
import { Link } from '@tanstack/react-router'
import logo from '../assets/logo.jpg'
import { Dropdown } from '../Dropdown'

export interface BaseHeaderProps {
  navigation: ReactNode
  children: ReactNode
}

export function BaseHeader({ navigation, children }: BaseHeaderProps) {
  const onViewProfile = () => {
    console.log('View profile clicked')
  }

  const onLogout = () => {
    console.log('Logout clicked')
  }
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
        {navigation}

        <div className="flex items-center gap-3">
          <button
            type="button"
            aria-label="Help"
            className="flex size-9 shrink-0 items-center justify-center rounded-full bg-brand-green text-white hover:bg-brand-green-dark"
          >
            <CircleHelp className="size-5" />
          </button>
        </div>
        <Dropdown
          label={<User className="size-5" />}
          ariaLabel="User menu"
          buttonClassName="flex size-9 items-center justify-center rounded-full bg-transparent text-white"
        >
          <Dropdown.Item onClick={onViewProfile}>Profile</Dropdown.Item>
          <Dropdown.Item onClick={onLogout}>Log out</Dropdown.Item>
        </Dropdown>
      </div>

      {children}
    </header>
  )
}
