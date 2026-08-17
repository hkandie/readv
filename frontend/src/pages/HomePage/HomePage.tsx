import { usePageTitle } from '../../hooks/usePageTitle'
import { useCurrentUser } from '../../auth/hooks/useCurrentUser'

export function HomePage() {
  usePageTitle('Home')
  useCurrentUser()

  return <main />
}
