import { describe, expect, it, vi } from 'vitest'
import { fireEvent, render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { Dropdown } from './Dropdown'

describe('Dropdown', () => {
  it('should open the menu when the trigger is clicked', async () => {
    render(
      <Dropdown label="Open">
        <Dropdown.Item>Item one</Dropdown.Item>
      </Dropdown>,
    )

    expect(screen.queryByRole('menu')).not.toBeInTheDocument()

    await userEvent.click(screen.getByRole('button', { name: 'Open' }))

    expect(screen.getByRole('menu')).toBeInTheDocument()
    expect(screen.getByRole('menuitem', { name: 'Item one' })).toBeInTheDocument()
  })

  it('should close the menu when clicking outside', async () => {
    render(
      <div>
        <Dropdown label="Open">
          <Dropdown.Item>Item one</Dropdown.Item>
        </Dropdown>
        <button>Outside</button>
      </div>,
    )

    await userEvent.click(screen.getByRole('button', { name: 'Open' }))
    expect(screen.getByRole('menu')).toBeInTheDocument()

    await userEvent.click(screen.getByRole('button', { name: 'Outside' }))
    expect(screen.queryByRole('menu')).not.toBeInTheDocument()
  })

  it('should close the menu when Escape is pressed', async () => {
    render(
      <Dropdown label="Open">
        <Dropdown.Item>Item one</Dropdown.Item>
      </Dropdown>,
    )

    await userEvent.click(screen.getByRole('button', { name: 'Open' }))
    expect(screen.getByRole('menu')).toBeInTheDocument()

    await userEvent.keyboard('{Escape}')
    expect(screen.queryByRole('menu')).not.toBeInTheDocument()
  })

  it('should call onClick and close the menu when an item is selected', async () => {
    const onClick = vi.fn()
    render(
      <Dropdown label="Open">
        <Dropdown.Item onClick={onClick}>Item one</Dropdown.Item>
      </Dropdown>,
    )

    await userEvent.click(screen.getByRole('button', { name: 'Open' }))
    await userEvent.click(screen.getByRole('menuitem', { name: 'Item one' }))

    expect(onClick).toHaveBeenCalledOnce()
    expect(screen.queryByRole('menu')).not.toBeInTheDocument()
  })

  it('should stay open when a non-Escape key is pressed', async () => {
    render(
      <Dropdown label="Open">
        <Dropdown.Item>Item one</Dropdown.Item>
      </Dropdown>,
    )

    await userEvent.click(screen.getByRole('button', { name: 'Open' }))
    expect(screen.getByRole('menu')).toBeInTheDocument()

    fireEvent.keyDown(document, { key: 'Enter' })
    expect(screen.getByRole('menu')).toBeInTheDocument()
  })

  it('should align the menu to the left when align is "left"', async () => {
    render(
      <Dropdown label="Open" align="left">
        <Dropdown.Item>Item one</Dropdown.Item>
      </Dropdown>,
    )

    await userEvent.click(screen.getByRole('button', { name: 'Open' }))

    expect(screen.getByRole('menu')).toHaveClass('left-0')
  })
})
