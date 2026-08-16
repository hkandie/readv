import type { Meta, StoryObj } from '@storybook/react-vite'
import { Page } from './Page'

const meta: Meta<typeof Page> = {
  title: 'Components/Page',
  component: Page,
}

export default meta
type Story = StoryObj<typeof Page>

export const Default: Story = {
  args: {
    children: <p>Page content goes here.</p>,
  },
}
