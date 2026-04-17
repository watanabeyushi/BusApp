/** Base URL for API (empty = same origin; dev proxy uses /api → backend). */
export function apiBase(): string {
  const base = import.meta.env.VITE_API_BASE_URL ?? ''
  return base.replace(/\/$/, '')
}

export type TripDirection = 'OUTBOUND' | 'INBOUND'

export type StopInfo = {
  stopId: string
  stopName: string
}

export async function fetchStops(
  routeId: string,
  direction: TripDirection,
): Promise<StopInfo[]> {
  const q = new URLSearchParams({ routeId, direction })
  const res = await fetch(`${apiBase()}/api/stops?${q.toString()}`)
  if (!res.ok) {
    throw new Error(`HTTP ${res.status}`)
  }
  return (await res.json()) as StopInfo[]
}

export type NextBusResponse = {
  routeId: string
  routeName: string
  boardingStopId: string
  destinationStopId: string
  departureTime: string
  arrivalAtDestination: string
  followingDepartureTime: string | null
  followingArrivalAtDestination: string | null
}

export async function fetchNextBus(
  routeId: string,
  boardingStopId: string,
  destinationStopId: string,
  direction: TripDirection,
): Promise<NextBusResponse | null> {
  const q = new URLSearchParams({
    routeId,
    boardingStopId,
    destinationStopId,
    direction,
  })
  const res = await fetch(`${apiBase()}/api/bus/next?${q.toString()}`)
  if (res.status === 404) {
    return null
  }
  if (!res.ok) {
    throw new Error(`HTTP ${res.status}`)
  }
  return (await res.json()) as NextBusResponse
}

export type CongestionLevel = 'EMPTY' | 'NORMAL' | 'CROWDED'

export type CongestionLatestResponse = {
  id: number
  routeId: string
  stopId: string
  level: CongestionLevel
  recordedAt: string
}

/** 該当停車地に混雑登録がなければ null（404） */
export async function fetchCongestionLatest(
  routeId: string,
  stopId: string,
): Promise<CongestionLatestResponse | null> {
  const q = new URLSearchParams({ routeId, stopId })
  const res = await fetch(`${apiBase()}/api/congestion/latest?${q.toString()}`)
  if (res.status === 404) {
    return null
  }
  if (!res.ok) {
    throw new Error(`HTTP ${res.status}`)
  }
  return (await res.json()) as CongestionLatestResponse
}

export async function postCongestion(
  routeId: string,
  stopId: string,
  level: CongestionLevel,
): Promise<void> {
  const res = await fetch(`${apiBase()}/api/congestion`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ routeId, stopId, level }),
  })
  if (!res.ok) {
    throw new Error(`HTTP ${res.status}`)
  }
}
