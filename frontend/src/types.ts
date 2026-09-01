export interface User {
  id: string
  name: string
  email: string
}

export interface Report {
  id: string
  name: string
  status: 'active' | 'archived'
}

export interface Account {
  id: string
  name: string
}
