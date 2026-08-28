import { createFileRoute } from '@tanstack/react-router'
import { DashboardOverviewPage } from '../../pages/DashboardOverviewPage/DashboardOverviewPage'

export const Route = createFileRoute('/dashboard/overview')({
  component: DashboardOverviewPage,
})
