# BusApp フロントエンド（設計）

## 役割

バックエンドの REST API を呼び出し、**次の発車時刻**と**あと○分**を表示し、**混雑度の投稿**を行う単一ページの SPA（Single Page Application）です。Capacitor で Web 資産をネイティブに載せられる構成です。

## 技術スタック

| 項目 | 内容 |
|------|------|
| ビルド | Vite 5 |
| UI | React 18 + TypeScript |
| スタイル | コンポーネントローカル CSS（`App.css` 等） |
| HTTP | `fetch`（ラッパーは `src/api.ts`） |
| ネイティブ | Capacitor 6（`@capacitor/android` 等） |

## ディレクトリ構成（主要）

| パス | 説明 |
|------|------|
| `src/main.tsx` | エントリ、React ルートのマウント |
| `src/App.tsx` | メイン画面（次のバス表示・カウントダウン・混雑ボタン） |
| `src/App.css` / `src/index.css` | レイアウト・スタイル |
| `src/api.ts` | API 基底 URL 解決、`fetchNextBus` / `postCongestion` |
| `vite.config.ts` | 開発サーバのプロキシ（`/api` → `http://localhost:8080`） |
| `capacitor.config.ts` | アプリ ID、Web 出力ディレクトリ `dist`、スキーム等 |

## 画面設計（論理）

- **表示対象の路線・停車地**: 現状は `App.tsx` 内の定数（`DEFAULT_ROUTE_ID` / `DEFAULT_STOP_ID`）で固定。将来は設定画面や URL パラメータに拡張可能。
- **次のバス**: `loadNext` が `GET /api/bus/next` を呼び、404 の場合は「本日以降の便なし」として扱う。
- **カウントダウン**: サーバが返す `departureTime`（当日の `HH:mm:ss`）を `Date` に解釈し、現在時刻（10 秒間隔で更新）との差分から **分単位（切り上げ）** で算出。
- **混雑**: 3 ボタンが `POST /api/congestion` を送る。送信中は `congestionBusy` で二重送信を抑止。

## API 通信設計

| 項目 | 内容 |
|------|------|
| 基底 URL | `apiBase()` = `import.meta.env.VITE_API_BASE_URL` をトリム。未設定時は空文字 → **相対パス** `/api/...`（開発時は Vite のプロキシがバックエンドへ転送） |
| 実機・本番 | `.env` に `VITE_API_BASE_URL=http://<ホスト>:8080` を設定しビルド後、同一オリジンでない API に直接接続 |
| エラー | `fetch` で `response.ok` でない場合は `Error` を投げ、`App` でメッセージ表示 |

## 開発時の動作

1. バックエンドを `0.0.0.0:8080` で起動。
2. `npm run dev` で Vite（通常 `5173`）。ブラウザからの `/api/*` は `vite.config.ts` の `proxy` によりバックエンドへ中継されるため、**CORS を意識せず**開発できる。

## ビルド・Capacitor

| コマンド | 用途 |
|----------|------|
| `npm run build` | `tsc -b` のあと `vite build` → `dist/` |
| `npm run cap:sync` | `package.json` で定義。ビルド後に `cap sync`（Web をネイティブへ反映） |
| `npm run cap:add:android` | 初回のみ Android プラットフォームを追加 |

実機では `localhost` のバックエンドに届かないため、**`VITE_API_BASE_URL` に PC の LAN IP** を指定する必要がある。詳細はリポジトリ直下の [README.md](../README.md) を参照。

## 環境変数

| 変数 | 説明 |
|------|------|
| `VITE_API_BASE_URL` | API のベース URL（末尾スラッシュなし推奨）。未設定時は相対 `/api` |
