# Android browser

[![Build Status](https://travis-ci.com/Mercandj/browser.svg?branch=master)](https://travis-ci.com/Mercandj/browser)

----

<p align="center">
	<a margin="20px 0" href="https://play.google.com/store/apps/details?id=com.mercandalli.android.browser">
		<img src="https://raw.github.com/Mercandj/browser/master/config/screenshot/android_web_browser_mercandalli.png" width="300" />
	</a>
</p>

<a href='https://play.google.com/store/apps/details?id=com.mercandalli.android.browser&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'>
    <img 
    alt='Get it on Google Play' 
    src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'
    width="220" />
</a>

----

## Description

Android web browser in Kotlin <3

Try the app via the [Play Store](https://play.google.com/store/apps/details?id=com.mercandalli.android.browser)

----

## Dev rules

### Typo

Java typo, fields without `m` prefix.

### Commit message

In order to have a clean commit list and to easily find feature of a commit (goal, fix bug...), the commit message should respect some rules.
Commit messages are important. They are the only way to give context to the changes that are made in a commit.

* Mandatory rules: `[Player] Improve the player X control` where the `[` and `]` surround the feature.
* Optional rules emoji: `[Player] :fire: Fix the player X control` where emoji could be the one you want or one of this one:
    * :fire: `:fire:` Fix a bug.
    * :wrench: `:wrench:` Feature dev that is not ui.
    * :art: `:art:` UI commit, readme, all that is visible.
    * :recycle: `:recycle:` Clean, reformat, refactor, ortho, typo...
    * :seedling: `:seedling:` New feature, first commit.
    * :skeleton: `:skeleton:` Architecture/skeleton of a feature.
* Optional: Write the `Why`, the `Reason` of your commit in the message description.

Do our future selves a favour and start writing better commit messages now :)


## Gradle

* ```./gradlew dependencyUpdates -Drevision=release -DoutputFormatter=json``` : Check [dependencies](https://github.com/ben-manes/gradle-versions-plugin): 
* ```./gradlew :app:assembleDebug :app:testUniversalDebugUnitTest``` : Unit tests


## Build Debug

* Run `./gradlew app:assembleDebug`


## Build release candidate (RC)

* Add the jks in the `./config/signing/browser.jks`
* Complete `./config/signing/signing.gradle`
* Run `./gradlew app:assembleRelease`


## Build release

* Add the jks in the `./config/signing/browser.jks`
* Complete `./config/signing/signing.gradle`
* Run `./gradlew app:bundleRelease`


## Build and publish alpha release on the Play Store

* Add the jks in the `./config/signing/browser.jks`
* Complete `./config/signing/signing.gradle`
* Remove .template and replace TO_FILL in files inside `./config/play-store/` folder.
* Run `./gradlew app:publish`


## Take screenshots

* Read /config/screenshot/README.md
* `cd config/screenshot`
* `./screenshot.sh`