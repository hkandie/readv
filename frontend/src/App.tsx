import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { RouterProvider, createRouter } from '@tanstack/react-router'
import { ToastProvider } from './toast'
import { routeTree } from './routeTree.gen'
import { ApiProvider } from './auth/context/ApiProvider'
import { UserProvider } from './auth/context/UserProvider'
import { MemberProvider } from './memberAccount/context/MemberProvider'

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
            <MemberProvider>
              <RouterProvider router={router} />
            </MemberProvider>
          </UserProvider>
        </ApiProvider>
      </ToastProvider>
    </QueryClientProvider>
  )
}

export default App
