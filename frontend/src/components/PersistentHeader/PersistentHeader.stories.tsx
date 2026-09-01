import type { Meta, StoryObj } from '@storybook/react-vite'
import { PersistentHeader } from './PersistentHeader'

const meta: Meta<typeof PersistentHeader> = {
  title: 'Components/PersistentHeader',
  component: PersistentHeader,
}

export default meta
type Story = StoryObj<typeof PersistentHeader>

export const Skeleton: Story = {}
