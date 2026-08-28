import { createRootRoute, Outlet } from '@tanstack/react-router'
import { PersistentHeader, Footer } from '@fhlbdm/core-ui'

export const Route = createRootRoute({
  component: () => (
    <div className="flex min-h-screen flex-col">
      <PersistentHeader />
      <main className="flex-1">
        <Outlet />
      </main>
      <Footer />
    </div>
  ),
})
