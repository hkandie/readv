import type { ReactNode } from 'react'

export interface PageProps {
  children: ReactNode
}

export function Page({ children }: PageProps) {
  return <main className="mx-auto w-full max-w-4xl px-4 py-8 sm:px-6 lg:px-8">{children}</main>
}
