import { describe, expect, it, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { createRootRoute, createRouter, RouterProvider } from '@tanstack/react-router'
import { PersistentHeader, type PersistentHeaderProps } from './PersistentHeader'

function renderHeader(props: PersistentHeaderProps = {}) {
  const rootRoute = createRootRoute({ component: () => <PersistentHeader {...props} /> })
  const router = createRouter({ routeTree: rootRoute })
  return render(<RouterProvider router={router} />)
}

describe('PersistentHeader', () => {
  it('should render Home, Rates, and Wires links', async () => {
    renderHeader()

    expect(await screen.findByRole('link', { name: 'Home' })).toHaveAttribute('href', '/')
    expect(screen.getByRole('link', { name: 'Rates' })).toHaveAttribute('href', '/rates')
    expect(screen.getByRole('link', { name: 'Wires' })).toHaveAttribute('href', '/wires')
  })

  it('should render Help and User menu buttons', async () => {
    renderHeader()

    expect(await screen.findByRole('button', { name: 'Help' })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: 'User menu' })).toBeInTheDocument()
  })

  it('should call onViewProfile and onLogout from the user menu', async () => {
    const onViewProfile = vi.fn()
    const onLogout = vi.fn()
    renderHeader({ onViewProfile, onLogout })

    await userEvent.click(await screen.findByRole('button', { name: 'User menu' }))
    await userEvent.click(screen.getByRole('menuitem', { name: 'Log out' }))

    expect(onLogout).toHaveBeenCalledOnce()
    expect(onViewProfile).not.toHaveBeenCalled()
  })

  it('should not render the greeting row when no userName is given', async () => {
    renderHeader()

    expect(await screen.findByRole('link', { name: 'Home' })).toBeInTheDocument()
    expect(screen.queryByText(/^Hello,/)).not.toBeInTheDocument()
  })

  it('should render the greeting, organization selector, and member id', async () => {
    const onSelectOrganization = vi.fn()
    renderHeader({
      userName: 'Ada',
      organizations: ['Bank A', 'Bank B'],
      onSelectOrganization,
      memberId: '#1234',
    })

    expect(await screen.findByText('Hello, Ada')).toBeInTheDocument()
    expect(screen.getByText('#1234')).toBeInTheDocument()

    await userEvent.click(screen.getByRole('button', { name: 'Select organization' }))
    expect(screen.getByRole('menuitem', { name: 'Bank B' })).toBeInTheDocument()

    await userEvent.click(screen.getByRole('menuitem', { name: 'Bank B' }))
    expect(onSelectOrganization).toHaveBeenCalledWith('Bank B')
  })
})
