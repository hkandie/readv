import { QueryClientProvider } from '@tanstack/react-query'
import { RouterProvider, createRouter } from '@tanstack/react-router'
import { queryClient } from './api/queryClient'
import { ApiProvider } from './api/ApiHandler'
import { ToastProvider } from './toast'
import { UserProvider } from './api/user/UserContext'
import { routeTree } from './routeTree.gen'

const router = createRouter({ routeTree, basepath: '/readvantage' })

declare module '@tanstack/react-router' {
  interface Register {
    router: typeof router
  }
}

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ApiProvider>
        <ToastProvider>
          <UserProvider>
            <RouterProvider router={router} />
          </UserProvider>
        </ToastProvider>
      </ApiProvider>
    </QueryClientProvider>
  )
}

export default App
