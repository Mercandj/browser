#!/usr/bin/env bash

BASEDIR=$(dirname "$0")
echo "[Publish] Script base directory: $BASEDIR"

pushd "$BASEDIR"

echo "[Publish] Generate bundleRelease\n\n"

pushd ../../
bash ./gradlew app:bundleRelease
cp ./app/build/outputs/bundle/release/app.aab ./config/play-store/app.aab
popd

echo "[Publish] Generate bundleRelease: ended\n\n"

echo "[Publish] Publish app bundle\n\n"

java -jar play-store-1-00-00.jar --force

popd