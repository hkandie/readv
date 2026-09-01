import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { RouterProvider, createRouter } from '@tanstack/react-router'
import { ToastProvider } from './toast'
import { routeTree } from './routeTree.gen'
import { ApiProvider } from './auth/context/ApiProvider'
import { UserProvider } from './auth/context/UserProvider'
import { MemberAccountProvider } from './memberAccount/context/MemberAccountProvider'

const router = createRouter({ routeTree, basepath: import.meta.env.BASE_URL })

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
        <ApiProvider>
           <UserProvider>
            <MemberAccountProvider>
              <RouterProvider router={router} />
            </MemberAccountProvider>
          </UserProvider>
        </ApiProvider>
      </ToastProvider>
    </QueryClientProvider>
  )
}

export default App
