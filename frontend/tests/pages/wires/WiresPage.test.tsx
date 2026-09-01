import { afterEach, describe, expect, it } from 'vitest'
import { screen } from '@testing-library/react'
import { renderComponent } from '../../utils/renderComponent'
import { WiresPage } from '../../../src/pages/wires/WiresPage'

afterEach(() => {
  document.title = ''
})

describe('WiresPage', () => {
  it('should set the page title and render a Wires heading', async () => {
    renderComponent(<WiresPage />)

    expect(await screen.findByRole('heading', { name: 'Wires' })).toBeInTheDocument()
    expect(document.title).toBe('ReAdvantage - Wires')
  })
})
