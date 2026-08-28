import type { HTMLAttributes } from 'react'

export type FooterProps = HTMLAttributes<HTMLElement>

export function Footer({ className = '', ...props }: FooterProps) {
  return (
    <footer
      className={`w-full border-t border-slate-200 px-4 py-4 text-sm text-slate-500 ${className}`}
      {...props}
    >
      © {new Date().getFullYear()} ReAdvantage
    </footer>
  )
}
