import { describe, expect, it } from 'vitest'
import { render, screen } from '@testing-library/react'
import { Footer } from './Footer'

describe('Footer', () => {
  it('should render a footer landmark with the current year', () => {
    render(<Footer />)

    expect(screen.getByRole('contentinfo')).toHaveTextContent(String(new Date().getFullYear()))
  })
})
