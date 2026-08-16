import type { ToastMessage } from './types'

const VARIANT_STYLES: Record<ToastMessage['variant'], string> = {
  info: 'bg-slate-800 text-white',
  success: 'bg-emerald-600 text-white',
  error: 'bg-red-600 text-white',
}

export function Toast({
  toast,
  onDismiss,
}: {
  toast: ToastMessage
  onDismiss: (id: string) => void
}) {
  return (
    <div
      role="alert"
      className={`flex items-center justify-between gap-4 rounded-md px-4 py-2 shadow-lg ${VARIANT_STYLES[toast.variant]}`}
    >
      <span className="text-sm">{toast.message}</span>
      <button
        type="button"
        onClick={() => onDismiss(toast.id)}
        aria-label="Dismiss notification"
        className="text-sm font-semibold opacity-80 hover:opacity-100"
      >
        ×
      </button>
    </div>
  )
}
