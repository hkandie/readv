import { createFileRoute, Outlet } from '@tanstack/react-router'
import { Page } from '../../components'

export const Route = createFileRoute('/dashboard')({
  component: () => (
    <Page>
      <Outlet />
    </Page>
  ),
})
