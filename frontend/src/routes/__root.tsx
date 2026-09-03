import { createRootRoute, Outlet } from '@tanstack/react-router'
import { Footer } from '@fhlbdm/core-ui'
import { AppHeader } from '../components/AppHeader'
import { NotFoundPage } from '../pages/not-found/NotFoundPage'
import { ErrorPage } from '../pages/error/ErrorPage'

function RootComponent() {
  return (
    <div className="flex min-h-screen flex-col">
      <AppHeader />
      <main className="flex-1">
        <Outlet />
      </main>
      <Footer />
    </div>
  )
}

export const Route = createRootRoute({
  component: RootComponent,
  notFoundComponent: NotFoundPage,
  errorComponent: ErrorPage,
})
