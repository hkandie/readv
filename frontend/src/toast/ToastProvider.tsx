import { useCallback, useMemo, useState, type ReactNode } from 'react'
import { ToastContext, type ToastContextValue } from './ToastContext'
import { ToastContainer } from './ToastContainer'
import type { ToastMessage, ToastVariant } from './types'

export function ToastProvider({ children }: { children: ReactNode }) {
  const [toasts, setToasts] = useState<ToastMessage[]>([])

  const removeToast = useCallback((id: string) => {
    setToasts((current) => current.filter((toast) => toast.id !== id))
  }, [])

  const addToast = useCallback((message: string, variant: ToastVariant = 'info') => {
    const id = crypto.randomUUID()
    setToasts((current) => [...current, { id, message, variant }])
  }, [])

  const value = useMemo<ToastContextValue>(
    () => ({ toasts, addToast, removeToast }),
    [toasts, addToast, removeToast],
  )

  return (
    <ToastContext.Provider value={value}>
      {children}
      <ToastContainer toasts={toasts} onDismiss={removeToast} />
    </ToastContext.Provider>
  )
}
