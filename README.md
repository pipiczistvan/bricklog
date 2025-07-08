<div align="center">

<img src="readme/app_icon.png" width="128">

# Bricklog

</div>

<div align="center">

<a href="https://github.com/pipiczistvan/bricklog/releases/latest">
  <img src="https://img.shields.io/github/v/release/pipiczistvan/bricklog?logo=github&labelColor=1a1a1a">
</a>
<a href="https://github.com/pipiczistvan/bricklog/blob/main/LICENSE">
  <img src="https://img.shields.io/github/license/pipiczistvan/bricklog?logo=gnu&color=blue&labelColor=1a1a1a">
</a>
<img src="https://img.shields.io/badge/API-26+-blue?logo=android&labelColor=1a1a1a">

A simple lego catalog app built
with [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html).

Supports light mode, dark mode.

<a href="https://play.google.com/store/apps/details?id=hu.piware.bricklog">
    <img src="readme/get_it_on_google_play.svg" width="200">
</a>
(work in progress)

</div>

# Screenshots

<p align="center" width="100%">
  <img src="readme/phone_screenshot_light_1.png" width="30%">
  <img src="readme/phone_screenshot_light_2.png" width="30%">
  <img src="readme/phone_screenshot_light_3.png" width="30%">
  <img src="readme/phone_screenshot_dark_1.png" width="30%">
  <img src="readme/phone_screenshot_dark_2.png" width="30%">
  <img src="readme/phone_screenshot_dark_3.png" width="30%">
</p>

# Features

- **Offline access:** After the initial download, the whole lego database is available offline.
- **Notifications:** Get notifications when a new set is available in the database.
- **Favourite sets:** Mark a set as favourite to access it in the favourites list.
- **Set details:** View various details about each set.
- **Set images:** Check out the set images in full screen with zoom support.
- **Fast search:** Search sets by name or filters, and specify the order.
- **Scan barcode and CMF codes:** Scan a set barcode or collectible minifigure box code and access
  its details instantly.
- **Set instructions:** Download set instructions directly from the app.

# Development

<div align="center">

  <img src="readme/one_does_not_simply.jpg" width="500"/>

</div>

## Environment setup

Configure build flavor in [local.properties](local.properties) like this: `buildkonfig.flavor=mock`

### Available flavors

| 	                           | prod (default) 	 | dev 	 | mock 	 |
|-----------------------------|------------------|-------|--------|
| Requires Firebase setup   	 | yes            	 | yes 	 | no   	 |
| Requires Brickset API key 	 | yes            	 | yes 	 | no   	 |
| Developer options enabled 	 | no             	 | yes 	 | yes  	 |

### Brickset API key

You can obtain a brickset api key [here](https://brickset.com/tools/webservices/requestkey) and
include `BRICKSET_API_KEY=<API_KEY>` in [local.properties](local.properties)

### Firebase setup

#### Android

Download a `google-services.json` from Firebase and place in `composeApp` folder

#### iOS

1. Build the project in Android Studio to generate necessary files.
2. Open the iosApp.xcworkspace file in Xcode
3. Download a `GoogleService-Info.plist` and place in `iosApp/iosApp`

## Baseline Profile (android)

Generate baseline profile using BaselineProfileGenerator on a rooted emulator.
Run benchmarks on real device.

1. Use mock remote implementations and turn off startup permission requests
2. Select benchmarkRelease build variant
3. Execute ./gradlew :composeApp:generateReleaseBaselineProfile
4. Test with benchmarks
5. Copy baseline-prof.txt and startup-prof.txt to composeApp/src/androidMain folder

## Troubleshoot

- On iOS JDK 17 is necessary to build. It can be specified manually
  in [gradle.properties](gradle.properties) via `org.gradle.java.home`.

  Afterwards, build the project and you're ready to use it.

- On iOS the background task can be triggered manually by pausing program execution and type the
  following command:

  `e -l objc -- (void)[[BGTaskScheduler sharedScheduler] _simulateLaunchForTaskWithIdentifier:@"hu.piware.bricklog.sync_sets"]`

  After resuming the task should trigger.

# Technical details

## iOS support

Although the project technically supports iOS builds, it is not distributed on App Store at the
moment and my iOS support is secondary. I used KMP for learning purposes only.

## Set data flow

In order to reduce 3rd party api usage, and make the data availability robust I wrote a
bricklog-data-service kotlin application which scrapes the lego data every day and uploads to a
fileserver as a compressed csv.

If there was any modification since the last data fetch, it follows these steps:

1. Store modifications in local db.
2. Export db as compressed csv file.
3. Upload file to public fileserver(s) (backup files supported).
4. Update FireStore records with file url and modification date.
5. If there was a new set added, it also sends a notification via FCM.

The mobile client checks on FireStore if there are any modified file uploaded. If yes, it downloads
the file, processes, and stores the data in local sqlite db.
