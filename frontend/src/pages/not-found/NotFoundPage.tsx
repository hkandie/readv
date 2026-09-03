import { Page } from '@fhlbdm/core-ui'
import { Link } from '@tanstack/react-router'
import { usePageTitle } from '../../hooks/usePageTitle'

export function NotFoundPage() {
  usePageTitle('Page not found')

  return (
    <Page>
      <h1 className="text-2xl font-semibold text-slate-900">404 — Page not found</h1>
      <p className="mt-2 text-sm text-slate-600">
        The page you're looking for doesn't exist or has been moved.
      </p>
      <Link to="/" className="mt-4 inline-block text-sm font-medium text-brand-green hover:underline">
        Back to home
      </Link>
    </Page>
  )
}
