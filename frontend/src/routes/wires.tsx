import { createFileRoute } from '@tanstack/react-router'
import { WiresPage } from '../pages/wires/WiresPage'

export const Route = createFileRoute('/wires')({
  component: WiresPage,
})
