# RAKIBX Child Safety — Parent Dashboard build

This project reports permission state for two enrolled child devices and supports visible, user-started location sharing and SOS.

## Telegram commands
- `/start`
- `/devices`
- `/loc1`, `/loc2`
- `/permissions1`, `/permissions2`

## Permission reporting behavior
- The app declares camera, microphone, SMS and call-log permissions only so Android can report their current grant state.
- It NEVER requests or uses camera, microphone, SMS or call-log access.
- Location is requested only after the user taps START LOCATION SHARING.
- Permission status is sent when the app opens and when the user taps SEND PERMISSION STATUS.
- Android permission flags cannot always distinguish a first-time denial from never requested on every device/OS version. Treat `NOT_REQUESTED` as an estimate where Android has not recorded a user decision.

## Setup
1. Revoke the bot token previously shared in chat; create a new one with @BotFather. Keep it secret.
2. Deploy `server/bot_server.py` on a trusted HTTPS host.
3. Set `BOT_TOKEN`, `OWNER_CHAT_ID`, `DEVICE1_KEY`, and `DEVICE2_KEY` as environment secrets. Use different long random keys.
4. Set `API_BASE_URL` in `app/build.gradle.kts` to your HTTPS server base URL.
5. For the first phone, use `DEVICE_ID="device1"` and `DEVICE_KEY` matching `DEVICE1_KEY`.
6. For the second phone, use `DEVICE_ID="device2"` and `DEVICE_KEY` matching `DEVICE2_KEY`.
7. Open in Android Studio with JDK 17 and Android SDK 36, then Build > Build APK(s).

## Limits
No hidden camera/microphone activation, no SMS or call-log collection, no covert app-use surveillance, and no forced mobile-data toggling. Location requires consent, permission, power and network. This is a starter project and needs security review before real deployment.
