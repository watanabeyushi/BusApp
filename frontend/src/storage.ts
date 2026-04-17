import type { TripDirection } from './api'

const PREFIX = 'busapp.v1.'

export type UserRoutePrefs = {
  routeId: string
  direction: TripDirection
  boardingStopId: string
  destinationStopId: string
}

const DEFAULTS: UserRoutePrefs = {
  routeId: 'R1',
  direction: 'OUTBOUND',
  boardingStopId: 'STOP_MAIN',
  destinationStopId: 'STOP_OFFICE',
}

export function loadPrefs(): UserRoutePrefs {
  try {
    const raw = localStorage.getItem(PREFIX + 'prefs')
    if (!raw) {
      return { ...DEFAULTS }
    }
    const p = JSON.parse(raw) as Partial<UserRoutePrefs>
    return {
      routeId: typeof p.routeId === 'string' ? p.routeId : DEFAULTS.routeId,
      direction: p.direction === 'INBOUND' ? 'INBOUND' : 'OUTBOUND',
      boardingStopId:
        typeof p.boardingStopId === 'string'
          ? p.boardingStopId
          : DEFAULTS.boardingStopId,
      destinationStopId:
        typeof p.destinationStopId === 'string'
          ? p.destinationStopId
          : DEFAULTS.destinationStopId,
    }
  } catch {
    return { ...DEFAULTS }
  }
}

export function savePrefs(p: UserRoutePrefs): void {
  localStorage.setItem(PREFIX + 'prefs', JSON.stringify(p))
}
