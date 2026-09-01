import { useContext } from 'react'
import { MemberAccountContext } from '../context/MemberAccountContext'

export function useMemberAccount() {
  const context = useContext(MemberAccountContext)
  if (context === undefined) {
    throw new Error('useMemberAccount must be used within a MemberAccountProvider')
  }
  return context
}
