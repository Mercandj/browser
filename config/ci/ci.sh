#!/usr/bin/env bash

# Region - Constants
BASE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
PROJECT_DIR="$BASE_DIR/../../"

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
# EndRegion - Constants

# Region - Methods
log_d() {
    printf "${CONFIG_COLOR_CYAN}[Gradle][CI]${CONFIG_COLOR_RESET} $1\n"
}
log_d_tag() {
    printf "${CONFIG_COLOR_CYAN}[Gradle][CI]${CONFIG_COLOR_RESET}$1\n"
}
log_e() {
    printf "${CONFIG_COLOR_RED}[Gradle][CI][Error]${CONFIG_COLOR_RESET} $1\n"
}
log_e_tag() {
    printf "${CONFIG_COLOR_RED}[Gradle][CI][Error]${CONFIG_COLOR_RESET}$1\n"
}
log_jump() {
    printf "\n"
}
log_delimiter() {
    printf "${CONFIG_COLOR_RED}~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~${CONFIG_COLOR_RESET}\n"
}
# EndRegion - Methods

gradle_task() {
    module_name=$1
    task_name=$2

    log_d_tag "[${module_name}] ${task_name}"
    ./gradlew ${module_name}:${task_name}
    exit_status=$?
    if [ ${exit_status} -eq 1 ]; then
        log_e_tag "[${module_name}] ${task_name} failed"
        cat "$PROJECT_DIR/${module_name}/build/reports/tests/test/index.html"
        log_e_tag "[${module_name}] ${task_name} failed"
        exit ${exit_status}
    fi
}

log_jump
log_jump

log_delimiter
log_d "CI script!!!"
log_delimiter

log_jump
log_jump

log_d "Script base directory: $BASE_DIR"
log_d "Script project directory: $PROJECT_DIR"
log_jump

pushd "$PROJECT_DIR"

    pwd

    log_delimiter
    log_d "APP"
    log_delimiter

    gradle_task "app" "clean"
    gradle_task "app" "assembleDebug"
    gradle_task "app" "check"
    gradle_task "app" "ktlint"

    log_delimiter
    log_d "TV"
    log_delimiter

    gradle_task "tv" "clean"
    gradle_task "tv" "assembleDebug"
    gradle_task "tv" "check"
    gradle_task "tv" "ktlint"

    log_delimiter
    log_d "WEAR"
    log_delimiter

    gradle_task "wear" "clean"
    gradle_task "wear" "assembleDebug"
    gradle_task "wear" "check"
    gradle_task "wear" "ktlint"

popd