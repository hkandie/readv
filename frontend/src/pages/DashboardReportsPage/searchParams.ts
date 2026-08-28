import { z } from 'zod'

export const reportsSearchSchema = z.object({
  status: z.enum(['all', 'active', 'archived']).catch('all'),
  search: z.string().catch(''),
  page: z.number().int().min(1).catch(1),
})

export type ReportsSearch = z.infer<typeof reportsSearchSchema>
