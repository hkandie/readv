import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { RouterProvider, createRouter } from '@tanstack/react-router'
import { ToastProvider } from './toast'
import { routeTree } from './routeTree.gen'
import { ApiProvider } from './auth/context/ApiProvider'
import { UserProvider } from './auth/context/UserProvider'

const router = createRouter({ routeTree, basepath: '/readvantage' })

declare module '@tanstack/react-router' {
  interface Register {
    router: typeof router
  }
}

const queryClient = new QueryClient()

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ToastProvider>
        <UserProvider>
          <ApiProvider>
            <RouterProvider router={router} />
          </ApiProvider>
        </UserProvider>
      </ToastProvider>
    </QueryClientProvider>
  )
}

export default App
