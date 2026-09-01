import { describe, expect, it } from 'vitest'
import { render, screen } from '@testing-library/react'
import { createRootRoute, createRouter, RouterProvider } from '@tanstack/react-router'
import { QuickActionsCard } from '../../../src/pages/home/QuickActionsCard'

function renderQuickActionsCard() {
  const rootRoute = createRootRoute({ component: QuickActionsCard })
  const router = createRouter({ routeTree: rootRoute })
  return render(<RouterProvider router={router} />)
}

describe('QuickActionsCard', () => {
  it('should render the Quick actions heading and every action', async () => {
    renderQuickActionsCard()

    expect(await screen.findByText('Quick actions')).toBeInTheDocument()
    expect(screen.getByRole('link', { name: /View Rates/ })).toHaveAttribute('href', '/rates')
    expect(screen.getByRole('link', { name: /Create a Wire/ })).toHaveAttribute('href', '/wires')
    expect(screen.getByText('View Previous Advances')).toBeInTheDocument()
    expect(screen.getByText('Statements')).toBeInTheDocument()
    expect(screen.getByText('Pledge or Release Collateral')).toBeInTheDocument()
  })
})
