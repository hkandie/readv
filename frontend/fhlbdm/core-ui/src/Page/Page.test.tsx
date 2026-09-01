import { describe, expect, it } from 'vitest'
import { render, screen } from '@testing-library/react'
import { Page } from './Page'

describe('Page', () => {
  it('should render its children inside a main landmark', () => {
    render(
      <Page>
        <p>Hello</p>
      </Page>,
    )

    expect(screen.getByRole('main')).toHaveTextContent('Hello')
  })
})
