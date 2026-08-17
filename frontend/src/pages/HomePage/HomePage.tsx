import { usePageTitle } from '../../hooks/usePageTitle'
import { useCurrentUser } from '../../api/user'

export function HomePage() {
  usePageTitle('Home')
  useCurrentUser()

  return <main />
}
