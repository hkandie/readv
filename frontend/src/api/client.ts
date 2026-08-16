const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? ''

let currentRequestId = ''

export function setApiHeaders(requestId: string) {
  currentRequestId = requestId
}

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const headers = new Headers(init?.headers)
  headers.set('Accept', 'application/json')
  headers.set('X-Request-ID', currentRequestId)

  const response = await fetch(`${BASE_URL}${path}`, {
    ...init,
    headers,
  })

  if (!response.ok) {
    throw new Error(`Request to ${path} failed with status ${response.status}`)
  }

  return (await response.json()) as T
}

export const client = {
  get: <T>(path: string) => request<T>(path, { method: 'GET' }),
}
