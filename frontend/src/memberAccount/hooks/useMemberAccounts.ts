import { useQuery } from '@tanstack/react-query'
import type { Account } from '../../types'

// Stubbed until the backend endpoint exists; swap for a client.get('/api/member/accounts') call.
const FAKE_ACCOUNTS: Account[] = [
  { id: '1234', name: 'Federal Home Loan Bank Of Des Moines' },
  { id: '2234', name: 'Prairie State Bank & Trust' },
  { id: '3234', name: 'Acme Federal Credit Union' },
]

export function useMemberAccounts() {
  return useQuery({
    queryKey: ['member', 'accounts'],
    queryFn: () => Promise.resolve(FAKE_ACCOUNTS),
  })
}
