import { useCallback, useEffect, useMemo, useRef, useState } from 'react'
import {
  fetchCongestionLatest,
  fetchNextBus,
  fetchStops,
  postCongestion,
  type CongestionLatestResponse,
  type CongestionLevel,
  type NextBusResponse,
  type StopInfo,
  type TripDirection,
} from './api'
import { loadPrefs, savePrefs } from './storage'
import './App.css'

function parseTodayTime(time: string): Date {
  const [h, m, s] = time.split(':').map((x) => Number.parseInt(x, 10))
  const d = new Date()
  d.setHours(h, m, s ?? 0, 0)
  return d
}

function minutesUntil(target: Date, now: Date): number {
  return Math.max(0, Math.ceil((target.getTime() - now.getTime()) / 60_000))
}

function levelLabel(level: CongestionLevel): string {
  switch (level) {
    case 'EMPTY':
      return '空き'
    case 'NORMAL':
      return '普通'
    case 'CROWDED':
      return '混雑'
    default:
      return level
  }
}

function ensureOrder(
  stops: StopInfo[],
  boardingId: string,
  destId: string,
): { boardingStopId: string; destinationStopId: string } {
  if (stops.length < 2) {
    return { boardingStopId: boardingId, destinationStopId: destId }
  }
  const bi = stops.findIndex((s) => s.stopId === boardingId)
  const di = stops.findIndex((s) => s.stopId === destId)
  if (bi < 0 || di < 0) {
    return {
      boardingStopId: stops[0].stopId,
      destinationStopId: stops[stops.length - 1].stopId,
    }
  }
  if (bi < di) {
    return { boardingStopId: boardingId, destinationStopId: destId }
  }
  return {
    boardingStopId: stops[0].stopId,
    destinationStopId: stops[stops.length - 1].stopId,
  }
}

export default function App() {
  const initial = loadPrefs()
  const [routeId] = useState(initial.routeId)
  const [direction, setDirection] = useState<TripDirection>(initial.direction)
  const [boardingStopId, setBoardingStopId] = useState(initial.boardingStopId)
  const [destinationStopId, setDestinationStopId] = useState(
    initial.destinationStopId,
  )
  const [stops, setStops] = useState<StopInfo[]>([])
  const [stopsLoading, setStopsLoading] = useState(true)
  const directionPrevRef = useRef<TripDirection | null>(null)
  const [nextBus, setNextBus] = useState<NextBusResponse | null>(null)
  const [congestionLatest, setCongestionLatest] =
    useState<CongestionLatestResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [now, setNow] = useState(() => new Date())
  const [congestionBusy, setCongestionBusy] = useState(false)

  useEffect(() => {
    savePrefs({
      routeId,
      direction,
      boardingStopId,
      destinationStopId,
    })
  }, [routeId, direction, boardingStopId, destinationStopId])

  useEffect(() => {
    let cancelled = false
    setStopsLoading(true)
    void fetchStops(routeId, direction)
      .then((list) => {
        if (cancelled) {
          return
        }
        setStops(list)
        const flipped =
          directionPrevRef.current !== null &&
          directionPrevRef.current !== direction
        directionPrevRef.current = direction
        let b = boardingStopId
        let d = destinationStopId
        if (flipped) {
          b = direction === 'OUTBOUND' ? 'STOP_MAIN' : 'STOP_OFFICE'
          d = direction === 'OUTBOUND' ? 'STOP_OFFICE' : 'STOP_MAIN'
        }
        const fixed = ensureOrder(list, b, d)
        setBoardingStopId(fixed.boardingStopId)
        setDestinationStopId(fixed.destinationStopId)
      })
      .catch(() => {
        if (!cancelled) {
          setStops([])
        }
      })
      .finally(() => {
        if (!cancelled) {
          setStopsLoading(false)
        }
      })
    return () => {
      cancelled = true
    }
  }, [routeId, direction, boardingStopId, destinationStopId])

  const loadData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const fixed = ensureOrder(stops, boardingStopId, destinationStopId)
      if (fixed.boardingStopId !== boardingStopId) {
        setBoardingStopId(fixed.boardingStopId)
      }
      if (fixed.destinationStopId !== destinationStopId) {
        setDestinationStopId(fixed.destinationStopId)
      }
      const [bus, latest] = await Promise.all([
        fetchNextBus(
          routeId,
          fixed.boardingStopId,
          fixed.destinationStopId,
          direction,
        ),
        fetchCongestionLatest(routeId, fixed.boardingStopId),
      ])
      setNextBus(bus)
      setCongestionLatest(latest)
    } catch (e) {
      setError(e instanceof Error ? e.message : '読み込みに失敗しました')
      setNextBus(null)
      setCongestionLatest(null)
    } finally {
      setLoading(false)
    }
  }, [
    routeId,
    direction,
    boardingStopId,
    destinationStopId,
    stops,
  ])

  useEffect(() => {
    if (stopsLoading || stops.length < 2) {
      return
    }
    void loadData()
  }, [stopsLoading, stops, loadData])

  useEffect(() => {
    const t = window.setInterval(() => setNow(new Date()), 10_000)
    return () => window.clearInterval(t)
  }, [])

  useEffect(() => {
    const t = window.setInterval(() => void loadData(), 60_000)
    return () => window.clearInterval(t)
  }, [loadData])

  const targetTime = useMemo(() => {
    if (!nextBus) {
      return null
    }
    return parseTodayTime(nextBus.departureTime)
  }, [nextBus])

  const minutesLeft = useMemo(() => {
    if (!targetTime) {
      return null
    }
    return minutesUntil(targetTime, now)
  }, [targetTime, now])

  async function onCongestion(level: CongestionLevel) {
    if (congestionBusy) {
      return
    }
    setCongestionBusy(true)
    try {
      await postCongestion(routeId, boardingStopId, level)
      const latest = await fetchCongestionLatest(routeId, boardingStopId)
      setCongestionLatest(latest)
    } catch (e) {
      setError(e instanceof Error ? e.message : '送信に失敗しました')
    } finally {
      setCongestionBusy(false)
    }
  }

  function onBoardingChange(id: string) {
    setBoardingStopId(id)
    const bi = stops.findIndex((s) => s.stopId === id)
    const di = stops.findIndex((s) => s.stopId === destinationStopId)
    if (bi >= 0 && di >= 0 && bi >= di) {
      const next = stops[bi + 1]
      if (next) {
        setDestinationStopId(next.stopId)
      }
    }
  }

  function onDestinationChange(id: string) {
    setDestinationStopId(id)
    const bi = stops.findIndex((s) => s.stopId === boardingStopId)
    const di = stops.findIndex((s) => s.stopId === id)
    if (bi >= 0 && di >= 0 && bi >= di) {
      const prev = stops[di - 1]
      if (prev) {
        setBoardingStopId(prev.stopId)
      }
    }
  }

  const boardingName =
    stops.find((s) => s.stopId === boardingStopId)?.stopName ?? boardingStopId
  const destName =
    stops.find((s) => s.stopId === destinationStopId)?.stopName ??
    destinationStopId

  return (
    <div className="app">
      <header className="header">
        <h1 className="title">次のバス</h1>
        <p className="subtitle">
          {nextBus?.routeName ?? '路線'} · {routeId}
        </p>
      </header>

      <section className="prefs">
        <div className="direction-toggle" role="group" aria-label="方面">
          <button
            type="button"
            className={direction === 'OUTBOUND' ? 'dir active' : 'dir'}
            onClick={() => setDirection('OUTBOUND')}
          >
            行き
          </button>
          <button
            type="button"
            className={direction === 'INBOUND' ? 'dir active' : 'dir'}
            onClick={() => setDirection('INBOUND')}
          >
            帰り
          </button>
        </div>
        <div className="field">
          <label htmlFor="boarding">乗車</label>
          <select
            id="boarding"
            value={boardingStopId}
            onChange={(e) => onBoardingChange(e.target.value)}
            disabled={stopsLoading || stops.length < 2}
          >
            {stops.map((s) => (
              <option key={s.stopId} value={s.stopId}>
                {s.stopName}
              </option>
            ))}
          </select>
        </div>
        <div className="field">
          <label htmlFor="destination">目的地</label>
          <select
            id="destination"
            value={destinationStopId}
            onChange={(e) => onDestinationChange(e.target.value)}
            disabled={stopsLoading || stops.length < 2}
          >
            {stops.map((s) => (
              <option key={s.stopId} value={s.stopId}>
                {s.stopName}
              </option>
            ))}
          </select>
        </div>
      </section>

      <main className="main">
        {(loading || stopsLoading) && <p className="muted">読み込み中…</p>}
        {error && <p className="error">{error}</p>}

        {!loading && !stopsLoading && !error && nextBus && (
          <section className="next-card">
            <div className="label">乗車（{boardingName}）発車</div>
            <div className="time">{nextBus.departureTime}</div>
            <div className="label arrival-label">目的地（{destName}）到着</div>
            <div className="time arrival-time">{nextBus.arrivalAtDestination}</div>
            {minutesLeft !== null && (
              <div className="countdown">出発まで あと {minutesLeft} 分</div>
            )}
            {nextBus.followingDepartureTime && (
              <div className="following-wrap">
                <div className="following-label">その次</div>
                <div className="following-line">
                  <span>{nextBus.followingDepartureTime}</span>
                  <span className="following-arrow">→</span>
                  <span>{nextBus.followingArrivalAtDestination ?? '—'}</span>
                </div>
                <div className="following-caption">発車 → 到着</div>
              </div>
            )}
          </section>
        )}

        {!loading && !stopsLoading && !error && !nextBus && (
          <p className="muted">本日のこの条件以降の便はありません。</p>
        )}

        {!loading && !error && congestionLatest && (
          <section className="congestion-status" aria-live="polite">
            <span className="congestion-status-label">登録されている混雑</span>
            <span className="congestion-status-value">
              {levelLabel(congestionLatest.level)}
            </span>
          </section>
        )}

        <section className="congestion">
          <h2 className="section-title">混雑を記録（乗車地）</h2>
          <div className="row">
            <button
              type="button"
              className="btn empty"
              disabled={congestionBusy}
              onClick={() => void onCongestion('EMPTY')}
            >
              空き
            </button>
            <button
              type="button"
              className="btn normal"
              disabled={congestionBusy}
              onClick={() => void onCongestion('NORMAL')}
            >
              普通
            </button>
            <button
              type="button"
              className="btn crowded"
              disabled={congestionBusy}
              onClick={() => void onCongestion('CROWDED')}
            >
              混雑
            </button>
          </div>
        </section>
      </main>
    </div>
  )
}
