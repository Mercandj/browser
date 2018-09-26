#!/usr/bin/env bash

# https://proandroiddev.com/automate-your-app-screenshots-1edd93f46e0
# https://github.com/laramartin/android_screenshots

BASEDIR=$(dirname "$0")
printf "\n\n"
printf "[Screenshot] Script base directory: $BASEDIR"

locales=('pt_rBR' 'en_US' 'es_ES' 'fr_FR')
tests_apk_path="/Users/jonathan/Documents/browser/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk"
app_apk_path="/Users/jonathan/Documents/browser/app/build/outputs/apk/debug/app-debug.apk"
device="emulator-5554"

pushd "$BASEDIR"
rm -r ./build
popd

pushd "$BASEDIR/../../"
./gradlew assembleDebug assembleAndroidTest
rm -r /Users/jonathan/Documents/browser/fastlane/metadata/android/
adb -s ${device} install -t -r /Users/jonathan/Documents/browser/app/build/outputs/apk/debug/app-debug.apk
adb -s ${device} install -t -r /Users/jonathan/Documents/browser/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
adb -s ${device} shell rm -r /sdcard/com.mercandalli.android.browser/screengrab
for i in "${locales[@]}"
do
    adb -s ${device} shell pm clear com.mercandalli.android.browser
    adb -s ${device} shell pm grant com.mercandalli.android.browser android.permission.CHANGE_CONFIGURATION
    adb -s ${device} shell getprop ro.build.version.sdk
    adb -s ${device} shell pm grant com.mercandalli.android.browser android.permission.WRITE_EXTERNAL_STORAGE
    adb -s ${device} shell pm grant com.mercandalli.android.browser android.permission.READ_EXTERNAL_STORAGE
    echo "[Screenshot] Locale: ${i}"
    adb -s ${device} shell am instrument --no-window-animation -w \
        -e testLocale ${i} \
        -e endingLocale en_US \
        com.mercandalli.android.browser.test/androidx.test.runner.AndroidJUnitRunner
done
pwd
mkdir -p ./config/screenshot/build/
adb -s ${device} pull /sdcard/com.mercandalli.android.browser/screengrab ./config/screenshot/build/
popd

pushd "$BASEDIR"
find ./build -mindepth 2 -type f -exec mv -i '{}' ./build ';'
popd

pushd "$BASEDIR/build"
set -- */
rm -R -- "${@%/}"
popd

echo "[Screenshot] See ./config/screenshot/build"