import { useQuery } from '@tanstack/react-query'
import type { Report } from '../../types'
import { useApi } from '../../hooks/useApi'
import type { ReportsSearch } from '../../pages/DashboardReportsPage/searchParams'

export function useReports(filters: ReportsSearch) {
  const client = useApi()
  return useQuery({
    queryKey: ['reports', filters],
    queryFn: () =>
      client.get<Report[]>('/api/reports', { params: filters }).then((response) => response.data),
  })
}
