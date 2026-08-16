export type ToastVariant = 'info' | 'success' | 'error'

export interface ToastMessage {
  id: string
  variant: ToastVariant
  message: string
}
