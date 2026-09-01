import { describe, expect, it } from 'vitest'
import { render, screen } from '@testing-library/react'
import { WiresCard } from '../../../src/pages/home/WiresCard'

describe('WiresCard', () => {
  it('should render the Wires heading and every wire with its status', () => {
    render(<WiresCard />)

    expect(screen.getByText('Wires')).toBeInTheDocument()
    expect(screen.getByText('$1,000.00')).toBeInTheDocument()
    expect(screen.getByText('$2,500.00')).toBeInTheDocument()
    expect(screen.getByText('$500.00')).toBeInTheDocument()
    expect(screen.getByText('Complete')).toBeInTheDocument()
    expect(screen.getByText('Pending')).toBeInTheDocument()
    expect(screen.getByText('Cancelled')).toBeInTheDocument()
  })
})
