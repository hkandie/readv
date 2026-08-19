import { http, HttpResponse } from 'msw'
import type { User } from '../../src/types'

export const currentUser: User = { id: '1', name: 'Ada', email: 'ada@example.com' }

export const handlers = [http.get('*/api/user/me', () => HttpResponse.json(currentUser))]
