import { describe, expect, it } from 'vitest'
import { screen } from '@testing-library/react'
import { renderComponent } from '../../utils/renderComponent'
import { HomePage } from '../../../src/pages/home/HomePage'

describe('HomePage', () => {
  it('should set the page title and render the dashboard content', async () => {
    renderComponent(<HomePage />)

    expect(await screen.findByText('SOFR Advance')).toBeInTheDocument()

    expect(document.title).toBe('ReAdvantage - Home')

    expect(screen.getByText('$1,000,000.00')).toBeInTheDocument()
    expect(screen.getByText('Wires')).toBeInTheDocument()
    expect(screen.getByText('Quick actions')).toBeInTheDocument()
  })
})
