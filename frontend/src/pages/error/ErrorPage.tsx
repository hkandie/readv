import { Page } from '@fhlbdm/core-ui'
import type { ErrorComponentProps } from '@tanstack/react-router'
import { usePageTitle } from '../../hooks/usePageTitle'

export function ErrorPage({ error, reset }: ErrorComponentProps) {
  usePageTitle('Something went wrong')

  return (
    <Page>
      <h1 className="text-2xl font-semibold text-slate-900">Something went wrong</h1>
      <p className="mt-2 text-sm text-slate-600">{error.message}</p>
      <button
        type="button"
        onClick={reset}
        className="mt-4 rounded-md bg-brand-green px-4 py-2 text-sm font-medium text-white hover:bg-brand-green-dark"
      >
        Try again
      </button>
    </Page>
  )
}
