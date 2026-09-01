import type { Meta, StoryObj } from '@storybook/react-vite'
import { Card } from './Card'

const meta: Meta<typeof Card> = {
  title: 'Components/Card',
  component: Card,
}

export default meta
type Story = StoryObj<typeof Card>

export const Default: Story = {
  args: {
    children: <p>Card content goes here.</p>,
  },
}

export const Elevated: Story = {
  args: {
    elevated: true,
    children: <p>Card content goes here.</p>,
  },
}

export const Padding: Story = {
  args: {
    padding: 'lg',
    children: <p>Card content goes here.</p>,
  },
}

export const WithSections: Story = {
  render: () => (
    <Card>
      <Card.Header>
        <Card.Title>Card title</Card.Title>
      </Card.Header>
      <Card.Body>
        <Card.Text>Card body content goes here.</Card.Text>
      </Card.Body>
      <Card.Footer>
        <Card.Text>Footer content</Card.Text>
      </Card.Footer>
    </Card>
  ),
}
