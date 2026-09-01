import { describe, expect, it } from 'vitest'
import { render, screen } from '@testing-library/react'
import { StatCard } from '../../../src/pages/home/StatCard'

describe('StatCard', () => {
  it('should render the title, value, and subtitle when a value is provided', () => {
    render(
      <StatCard title="SOFR Advance" value="$3,950,000,000.00" subtitle="Outstanding balance" />,
    )

    expect(screen.getByText('SOFR Advance')).toBeInTheDocument()
    expect(screen.getByText('$3,950,000,000.00')).toBeInTheDocument()
    expect(screen.getByText('Outstanding balance')).toBeInTheDocument()
  })

  it('should render the value without a subtitle when none is provided', () => {
    render(<StatCard title="Daily Reset Advance" value="$450,000,000.00" />)

    expect(screen.getByText('Daily Reset Advance')).toBeInTheDocument()
    expect(screen.getByText('$450,000,000.00')).toBeInTheDocument()
  })

  it('should render an empty-state placeholder when no value is provided', () => {
    render(<StatCard title="Fixed Advance" />)

    expect(screen.getByText('Fixed Advance')).toBeInTheDocument()
    expect(screen.getByText('No data yet')).toBeInTheDocument()
  })
})
