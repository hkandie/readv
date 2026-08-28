import { useNavigate, useSearch } from '@tanstack/react-router'
import { usePageTitle } from '../../hooks/usePageTitle'
import { useReports } from '../../dashboard/hooks/useReports'
import type { ReportsSearch } from './searchParams'

const STATUS_OPTIONS: ReportsSearch['status'][] = ['all', 'active', 'archived']

export function DashboardReportsPage() {
  usePageTitle('Reports')

  const search = useSearch({ from: '/dashboard/reports' })
  const navigate = useNavigate({ from: '/dashboard/reports' })
  const { data, isLoading } = useReports(search)
  const reports = data ?? []

  const updateSearch = (changes: Partial<ReportsSearch>) => {
    navigate({ search: (prev) => ({ ...prev, ...changes }) })
  }

  return (
    <section>
      <h1>Reports</h1>

      <label htmlFor="reports-search">Search</label>
      <input
        id="reports-search"
        value={search.search}
        onChange={(event) => updateSearch({ search: event.target.value, page: 1 })}
      />

      <label htmlFor="reports-status">Status</label>
      <select
        id="reports-status"
        value={search.status}
        onChange={(event) =>
          updateSearch({ status: event.target.value as ReportsSearch['status'], page: 1 })
        }
      >
        {STATUS_OPTIONS.map((status) => (
          <option key={status} value={status}>
            {status}
          </option>
        ))}
      </select>

      {isLoading && <p>Loading…</p>}

      <ul>
        {reports.map((report) => (
          <li key={report.id}>{report.name}</li>
        ))}
      </ul>

      <div>
        <button
          type="button"
          disabled={search.page <= 1}
          onClick={() => updateSearch({ page: search.page - 1 })}
        >
          Previous
        </button>
        <span>Page {search.page}</span>
        <button type="button" onClick={() => updateSearch({ page: search.page + 1 })}>
          Next
        </button>
      </div>
    </section>
  )
}
