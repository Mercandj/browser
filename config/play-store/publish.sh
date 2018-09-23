#!/usr/bin/env bash

BASEDIR=$(dirname "$0")
echo "[Publish] Script base directory: $BASEDIR"


pushd "$BASEDIR"

pushd ../../

echo "[Publish] Remove folder: ./app/build/outputs"
rm -r "./app/build/outputs"

# Build app bundle to upload to the PlayStore
echo "[Publish] Generate bundleRelease\n\n"
bash ./gradlew app:bundleRelease
cp ./app/build/outputs/bundle/release/app.aab ./config/play-store/app.aab
echo "[Publish] Generate bundleRelease: ended\n\n"

# Build apk to upload to GitHub
echo "[Publish] Generate assembleRelease\n\n"
bash ./gradlew app:assembleRelease
echo "[Publish] Generate assembleRelease:ended\n\n"

popd


echo "[Publish] Publish app bundle\n\n"
# Upload app bundle to the PlayStore
java -jar play-store-1-00-00.jar --force


popd
