import React, { useEffect, useRef, useState } from 'react'
import { twMerge } from 'tailwind-merge'

export interface DropdownProps {
  label: React.ReactNode
  children: React.ReactNode
  align?: 'left' | 'right'
  buttonClassName?: string
  ariaLabel?: string
}

const DropdownRoot: React.FC<DropdownProps> = ({
  label,
  children,
  align = 'right',
  buttonClassName = '',
  ariaLabel,
}) => {
  const [open, setOpen] = useState(false)
  const containerRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    if (!open) return

    function handlePointerDown(event: MouseEvent) {
      if (!containerRef.current?.contains(event.target as Node)) {
        setOpen(false)
      }
    }

    function handleKeyDown(event: KeyboardEvent) {
      if (event.key === 'Escape') {
        setOpen(false)
      }
    }

    document.addEventListener('mousedown', handlePointerDown)
    document.addEventListener('keydown', handleKeyDown)
    return () => {
      document.removeEventListener('mousedown', handlePointerDown)
      document.removeEventListener('keydown', handleKeyDown)
    }
  }, [open])

  return (
    <div ref={containerRef} className="relative inline-block">
      <button
        type="button"
        aria-haspopup="menu"
        aria-expanded={open}
        aria-label={ariaLabel}
        onClick={() => setOpen((value) => !value)}
        className={twMerge(
          'flex items-center gap-1 rounded-md text-slate-600 hover:text-slate-900',
          buttonClassName,
        )}
      >
        {label}
      </button>
      {open ? (
        <div
          role="menu"
          onClick={() => setOpen(false)}
          className={twMerge(
            'absolute z-10 mt-2 min-w-40 rounded-md border border-slate-200 bg-white py-1 shadow-lg',
            align === 'right' ? 'right-0' : 'left-0',
          )}
        >
          {children}
        </div>
      ) : null}
    </div>
  )
}

export interface DropdownItemProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: React.ReactNode
}

const DropdownItem: React.FC<DropdownItemProps> = ({ children, className = '', ...props }) => {
  return (
    <button
      type="button"
      role="menuitem"
      className={twMerge(
        'flex w-full items-center gap-2 px-3 py-2 text-left text-sm text-slate-700 hover:bg-slate-50',
        className,
      )}
      {...props}
    >
      {children}
    </button>
  )
}

export const Dropdown = Object.assign(DropdownRoot, {
  Item: DropdownItem,
})
