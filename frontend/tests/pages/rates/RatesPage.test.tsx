import { afterEach, describe, expect, it } from 'vitest'
import { screen } from '@testing-library/react'
import { renderComponent } from '../../utils/renderComponent'
import { RatesPage } from '../../../src/pages/rates/RatesPage'

afterEach(() => {
  document.title = ''
})

describe('RatesPage', () => {
  it('should set the page title and render a Rates heading', async () => {
    renderComponent(<RatesPage />)

    expect(await screen.findByRole('heading', { name: 'Rates' })).toBeInTheDocument()
    expect(document.title).toBe('ReAdvantage - Rates')
  })
})
