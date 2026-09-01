import type { Meta, StoryObj } from '@storybook/react-vite'
import { Dropdown } from './Dropdown'

const meta: Meta<typeof Dropdown> = {
  title: 'Components/Dropdown',
  component: Dropdown,
}

export default meta
type Story = StoryObj<typeof Dropdown>

export const Default: Story = {
  args: {
    label: 'Open menu',
    children: (
      <>
        <Dropdown.Item>Profile</Dropdown.Item>
        <Dropdown.Item>Log out</Dropdown.Item>
      </>
    ),
  },
}
