import { useQuery } from '@tanstack/react-query'
import { client } from '../client'
import type { User } from './types'

export function useCurrentUser() {
  return useQuery({
    queryKey: ['user', 'me'],
    queryFn: () => client.get<User>('/api/user/me'),
  })
}
