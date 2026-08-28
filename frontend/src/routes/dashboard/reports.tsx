import { createFileRoute } from '@tanstack/react-router'
import { DashboardReportsPage } from '../../pages/DashboardReportsPage/DashboardReportsPage'
import { reportsSearchSchema } from '../../pages/DashboardReportsPage/searchParams'

export const Route = createFileRoute('/dashboard/reports')({
  validateSearch: reportsSearchSchema,
  component: DashboardReportsPage,
})
