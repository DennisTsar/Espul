# Espul

Keep up with the GitHub events of your favorite GitHub users.

## Features

### Track user events

- Add GitHub users to track & view their public GitHub events
- Titles and comments are rendered as markdown
- Unread indicators for users with new events
- Links to the relevant GitHub web pages for each event

### Data syncing

- Sync your followed users & view data across devices
- Syncing is done serverlessly through GitHub Repositories
    - Create a repository & a Personal Access Token with read/write permissions
    - Enter the repository name & token in the app settings

### Multiplatform

- Android, Desktop, and Browser support, powered
  by [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform)

### Bookmarks

Coming soon

## Build Instructions

### Before running!

- install JDK 17 on your machine
- add `local.properties` file to the project root and set a path to Android SDK there

### Android

To run the application on android device/emulator:

- open project in Android Studio and run imported android run configuration

To build the application bundle:

- run `./gradlew :composeApp:assembleDebug`
- find `.apk` file in `composeApp/build/outputs/apk/debug/composeApp-debug.apk`

### Desktop

Run the desktop application: `./gradlew :composeApp:run`

### Browser

Run the browser application: `./gradlew :composeApp:jsBrowserDevelopmentRun`

### Api Keys

You can run and use the project with no API keys, but some features will be unavailable.
GitHub API keys can be created for free from [here](https://github.com/settings/tokens?type=beta).
To enable data syncing, the key must have the  "Contents" read/write permission for the target repository.

The API key & repository can be set through the UI, or
hard-coded [here](composeApp/src/commonMain/kotlin/io/github/opletter/espul/state/EspulViewModel.kt)

## Credits

- Created with [Compose Multiplatform Wizard](https://terrakok.github.io/Compose-Multiplatform-Wizard/)
- Inspired by [Tracktor](https://github.com/theapache64/tracktor)
