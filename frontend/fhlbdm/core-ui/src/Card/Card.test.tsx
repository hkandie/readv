import { describe, expect, it } from 'vitest'
import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { Card } from './Card'

describe('Card', () => {
  it('should render its children', () => {
    render(<Card>Hello</Card>)
    expect(screen.getByText('Hello')).toBeInTheDocument()
  })

  it('should call onClick when clicked', async () => {
    let clicked = false
    render(<Card onClick={() => (clicked = true)}>Click me</Card>)

    await userEvent.click(screen.getByText('Click me'))

    expect(clicked).toBe(true)
  })

  it('should apply the elevated shadow style when elevated', () => {
    render(<Card elevated>Hello</Card>)
    expect(screen.getByText('Hello')).toHaveClass('shadow-lg')
  })

  it('should apply the requested padding scale', () => {
    render(<Card padding="lg">Hello</Card>)
    expect(screen.getByText('Hello')).toHaveClass('p-5')
  })

  it('should render Header, Body, Footer, Title, and Text sub-components', () => {
    render(
      <Card>
        <Card.Header>Header content</Card.Header>
        <Card.Body>
          <Card.Title>Card title</Card.Title>
          <Card.Text>Card text</Card.Text>
        </Card.Body>
        <Card.Footer>Footer content</Card.Footer>
      </Card>,
    )

    expect(screen.getByText('Header content')).toBeInTheDocument()
    expect(screen.getByRole('heading', { name: 'Card title' })).toBeInTheDocument()
    expect(screen.getByText('Card text')).toBeInTheDocument()
    expect(screen.getByText('Footer content')).toBeInTheDocument()
  })
})
