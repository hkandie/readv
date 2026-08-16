import { createContext } from 'react'
import type { ToastMessage, ToastVariant } from './types'

export interface ToastContextValue {
  toasts: ToastMessage[]
  addToast: (message: string, variant?: ToastVariant) => void
  removeToast: (id: string) => void
}

export const ToastContext = createContext<ToastContextValue | undefined>(undefined)
