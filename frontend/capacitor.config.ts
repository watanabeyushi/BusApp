import type { CapacitorConfig } from '@capacitor/cli'

const config: CapacitorConfig = {
  appId: 'com.busapp.app',
  appName: 'BusApp',
  webDir: 'dist',
  server: {
    /**
     * 実機から PC のバックエンドに届ける場合: 例 `http://192.168.1.10:8080`
     * 未設定時はビルド済み Web をそのまま読み込み、API は `VITE_API_BASE_URL` で指定。
     */
    androidScheme: 'https',
  },
}

export default config
