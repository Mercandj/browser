#!/usr/bin/env bash

# Region - Color
CONFIG_COLOR_BOLD=$(tput bold)
CONFIG_COLOR_RED=`tput setaf 1`
CONFIG_COLOR_GREEN=`tput setaf 2`
CONFIG_COLOR_GREEN_LIGHT="$CONFIG_COLOR_BOLD$CONFIG_COLOR_GREEN"
CONFIG_COLOR_CYAN=`tput setaf 6`
CONFIG_COLOR_GRAY=`tput setaf 7`
CONFIG_COLOR_FAWN=$(tput setaf 3); CONFIG_COLOR_BEIGE="$CONFIG_COLOR_FAWN"
CONFIG_COLOR_YELLOW="$CONFIG_COLOR_BOLD$CONFIG_COLOR_FAWN"
CONFIG_COLOR_PURPLE=$(tput setaf 5);
CONFIG_COLOR_PINK="$CONFIG_COLOR_BOLD$CONFIG_COLOR_PURPLE"
CONFIG_COLOR_DARKCYAN=$(tput setaf 6)
CONFIG_COLOR_CYAN="$CONFIG_COLOR_BOLD$CONFIG_COLOR_DARKCYAN"
CONFIG_COLOR_RESET=`tput sgr0`
# EndRegion - Color


log_d() {
    printf "${CONFIG_COLOR_CYAN}[PlayStore][Publish]${CONFIG_COLOR_RESET} $1\n"
}
log_jump() {
    printf "\n"
}
log_line() {
    printf "${CONFIG_COLOR_RED}-------------------------------------------------------------------------------------------${CONFIG_COLOR_RESET}\n"
}


log_jump
log_jump

log_line
log_d "Play store publishing!!!"
log_line

log_jump
log_jump


BASEDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
log_d "Script base directory: $BASEDIR"
log_jump

pushd "$BASEDIR"
    log_jump

    log_d "Remove pre-existing $BASEDIR/build/app.aab"
    rm "./build/app.aab"
    log_jump

    log_d "Remove pre-existing $BASEDIR/build/app-release.apk"
    rm "./build/app-release.apk"
    log_jump

    log_d "Remove pre-existing $BASEDIR/build/mapping.txt"
    rm "./build/mapping.txt"
    log_jump

    log_d "Remove pre-existing $BASEDIR/build/play-store.jar"
    rm "./build/play-store.jar"
    log_jump

    if [ -d "$BASEDIR/build/play-store" ]; then
        log_d "Pull play-store GitHub project"
        pushd "$BASEDIR/build/play-store"
            git pull
        popd
    else
        log_d "Clone play-store GitHub project"
        mkdir -p build
        pushd build
            git clone https://github.com/Mercandj/play-store.git
        popd
    fi

    bash ./build/play-store/generate-jar.sh ../play-store.jar

    pushd ../../

        log_d "Remove folder: ./app/build/outputs"
        rm -r "./app/build/outputs"
        log_jump

        log_d "Generate bundleRelease file ./config/play-store/build/app.aab\n\n"
        bash ./gradlew app:bundleRelease
        cp ./app/build/outputs/bundle/release/app.aab ./config/play-store/build/app.aab
        log_d "Generate bundleRelease ended\n\n"
        log_jump

        log_d "Copy mapping file ./app/build/outputs/mapping/r8/release/mapping.txt to ./config/play-store/build/mapping.txt\n\n"
        cp ./app/build/outputs/mapping/r8/release/mapping.txt ./config/play-store/build/mapping.txt
        log_d "Copy mapping ended\n\n"
        log_jump

        log_d "Copy apk release file ./config/play-store/build/app-release to test on device and upload on GitHub\n\n"
        cp ./app/build/outputs/apk/release/app-release.apk ./config/play-store/build/app-release.apk
        log_d "Copy apk release ended\n\n"
        log_jump

    popd

    log_d "Publish app bundle to the PlayStore\n\n"
    java -jar ./build/play-store.jar --force
    log_jump

    log_line
    log_d "Script ended."
    log_d "If all succeeded, AppBundle should have been uploaded in the internal chanel."
    log_d "If all succeeded, mapping.txt should be in the ./build folder."
    log_d "If all succeeded, app-release.apk should be in the ./build folder."
    log_d "Please create a GitHub release with the apk rename browser-app-release-<version>.apk"
    log_d "Please upload the mapping to the PlayStore"
    log_line

popd
