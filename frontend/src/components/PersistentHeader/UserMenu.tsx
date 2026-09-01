import { Dropdown } from '@fhlbdm/core-ui'
import { User } from 'lucide-react'

export const UserMenu = () => {
  const onViewProfile = () => {
    console.log('View profile clicked')
  }

  const onLogout = () => {
    console.log('Logout clicked')
  }
  return (
    <Dropdown
      label={<User className="size-5" />}
      ariaLabel="User menu"
      buttonClassName="flex size-9 items-center justify-center rounded-full bg-brand-green text-white hover:bg-brand-green-dark"
    >
      <Dropdown.Item onClick={onViewProfile}>Profile</Dropdown.Item>
      <Dropdown.Item onClick={onLogout}>Log out</Dropdown.Item>
    </Dropdown>
  )
}
