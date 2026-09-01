import { createRootRoute, Outlet } from '@tanstack/react-router'
import { Footer } from '@fhlbdm/core-ui'
import { PersistentHeader } from '../components/PersistentHeader'


function RootComponent() {

  return (
    <div className="flex min-h-screen flex-col">
      <PersistentHeader
      />
      <main className="flex-1">
        <Outlet />
      </main>
      <Footer />
    </div>
  )
}

export const Route = createRootRoute({
  component: RootComponent,
})
