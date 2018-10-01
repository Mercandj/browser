[![Build Status](https://travis-ci.com/Mercandj/browser.svg?branch=master)](https://travis-ci.com/Mercandj/browser)
[![Version](https://img.shields.io/badge/download-1.00.23-607D8B.svg?style=flat-square&label=version)](https://github.com/Mercandj/browser/releases/tag/1.00.23)
[![Kotlin](https://img.shields.io/badge/kotlin-1.2.71-f5801e.svg?style=flat-square)](http://kotlinlang.org) 
[![PlayStore URL](https://img.shields.io/badge/play%20store-team%20mercan-1DA1F2.svg?style=flat-square&logo=android)](https://play.google.com/store/apps/details?id=com.mercandalli.android.browser)

# G - Browser - Android web browser
**Android web browser app focused on easy search and clear history**

Light web browser app. Browse the web and stay incognito.

<a href='https://play.google.com/store/apps/details?id=com.mercandalli.android.browser'>
    <img 
        src="app/src/main/res/icon/mipmap-xxxhdpi/ic_launcher.png"
        align="left"
        width="190"
        hspace="10"
        vspace="10" />
</a>

* Light / Dark theme
* Floating window
* Clear all cache / history
* Ad blocker
* Multiple search engines supported as Google, DuckDuckGo, Yahoo, Qwant...

<a href='https://play.google.com/store/apps/details?id=com.mercandalli.android.browser&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'>
    <img 
        alt='Get it on Google Play' 
        src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'
        height="80" />
</a>

<br/>

<a margin="20px 0 20px 40px" href="https://play.google.com/store/apps/details?id=com.mercandalli.android.browser">
	<img 
	    src="https://raw.github.com/Mercandj/browser/master/config/screenshot/android_web_browser_mercandalli.png" 
	    align="right"
	    width="250" />
</a>


## Description

G Browser is an app focused on quick search.
This application is open source on github !!!
Do not hesitate to submit merge request, open issue...

Developed in kotlin with <3 by Team Mercan.
Try the app via the [Play Store](https://play.google.com/store/apps/details?id=com.mercandalli.android.browser).

Enjoy G Browser =)

<br/>

## Project structure

- `./app` [here](./app)

Module of the Android app for mobile and tablet devices.
Apk generated is produced on the 
[PlayStore](https://play.google.com/store/apps/details?id=com.mercandalli.android.browser) or 
downloadable in the [release](https://github.com/Mercandj/browser/releases) section.

- `./config` [here](./config)

Contains all the shell scripts, extra gradle files, authentication, signature...

- `./config/play-store` [here](./config/play-store)

Automatic publish Android app bundle on the play store.

- `./config/quality` [here](./config/quality)

Code quality gradle tools.

- `./config/screenshot` [here](./config/screenshot)

Automatic tool to take ./app screenshot for multiple locales and themes.

- `./config/signing` [here](./config/signing)

App signing data.

- `./gradle/wrapper` [here](./gradle/wrapper)

Default location of gradle wrapper. All Android Studio and gradle project have this kind of folder.

- `./tv` [here](./tv)

Module of the Android TV devices.
This module development is not ready to be used.

- `./wear` [here](./wear)

Module of the Android Wear app for Wear OS.
This module development is not ready to be used.

<br/>

## Build or Publish

- Build Debug
    - Run `./gradlew app:assembleDebug`
- Build release candidate (RC)
    - Add the jks in the `./config/signing/browser.jks`
    - Complete `./config/signing/signing.gradle`
    - Run `./gradlew app:assembleRelease`
- Build release
    - Add the jks in the `./config/signing/browser.jks`
    - Complete `./config/signing/signing.gradle`
    - Run `./gradlew app:bundleRelease`
- Build and publish alpha release on the Play Store
    - Add the jks in the `./config/signing/browser.jks`
    - Complete `./config/signing/signing.gradle`
    - Read ./config/play-store/README.md
    - Run `./config/play-store/publish.sh`
- Take screenshots to publish on the store
    - Read ./config/screenshot/README.md
    - `./config/screenshot/screenshot.sh`

<br/>

## Check

- Check dependencies updates
    - ```./gradlew dependencyUpdates -Drevision=release -DoutputFormatter=json``` : Check 
    [dependencies](https://github.com/ben-manes/gradle-versions-plugin): 
- Check unit tests
    - ```./gradlew :app:assembleDebug :app:testUniversalDebugUnitTest``` : Unit tests

<br/>

## Dev rules

### Typo

Java typo, fields without `m` prefix.

### Commit message

In order to have a clean commit list and to easily find feature of a commit (goal, fix bug...), 
the commit message should respect some rules.
Commit messages are important. They are the only way to give context to the changes that are made 
in a commit.

* Mandatory rules: `[Player] Improve the player X control` where the `[` and `]` surround the 
feature.
* Optional rules emoji: `[Player] :fire: Fix the player X control` where emoji could be the one 
you want or one of this one:
    * :fire: `:fire:` Fix a bug.
    * :wrench: `:wrench:` Feature dev that is not ui.
    * :art: `:art:` UI commit, readme, all that is visible.
    * :recycle: `:recycle:` Clean, reformat, refactor, ortho, typo...
    * :seedling: `:seedling:` New feature, first commit.
* Optional: Write the `Why`, the `Reason` of your commit in the message description.

Do our future selves a favour and start writing better commit messages now :)