import type { Meta, StoryObj } from '@storybook/react-vite'
import { BaseHeader } from './BaseHeader'

const meta: Meta<typeof BaseHeader> = {
  title: 'Components/BaseHeader',
  component: BaseHeader,
}

export default meta
type Story = StoryObj<typeof BaseHeader>

export const Default: Story = {
  args: {
    navigation: (
      <nav className="flex items-center gap-4 pb-3">
        <span>Home</span>
        <span>Rates</span>
        <span>Wires</span>
      </nav>
    ),
    children: (
      <div className="flex items-center gap-3 bg-white py-3 pr-6 pl-32">
        <span>Hello, Ada</span>
      </div>
    ),
  },
}
