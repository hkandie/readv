import { createFileRoute } from '@tanstack/react-router'
import { DashboardSettingsPage } from '../../pages/DashboardSettingsPage/DashboardSettingsPage'

export const Route = createFileRoute('/dashboard/settings')({
  component: DashboardSettingsPage,
})
