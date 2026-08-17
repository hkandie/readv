import { useQuery } from '@tanstack/react-query'
import type { User } from '../../types'
import { useApi } from '../../hooks/useApi'

export function useCurrentUser() {
  const client = useApi()
  return useQuery({
    queryKey: ['user', 'me'],
    queryFn: () => client.get<User>('/api/user/me').then((response) => response.data),
  })
}
