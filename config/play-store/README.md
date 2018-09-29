# Publish browser on the play store via app bundle

- Add the jks in the `<project-root>/config/signing/browser.jks`
- Complete `<project-root>/config/signing/signing.gradle`
- Remove .template and replace TO_FILL in JSON files in this folder.
- PlayStore authentication could be found in you PlayStore console settings, API access, and display OAuth in the Google developer console.
- Run `./publish.sh`
- See this [PlayStore](https://github.com/Mercandj/play-store) project.

