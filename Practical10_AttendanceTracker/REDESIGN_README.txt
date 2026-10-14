LECTURE ATTENDANCE TRACKER — BLUE & WHITE REDESIGN

This is a Java + XML Android Studio project based on the original practical project.

IMPORTANT: Make a copy of your current project before replacing files. The redesigned app keeps the same SharedPreferences file name (attendance_data), the same subjects key, and the same JSON subject fields, so existing attendance data should remain available when you update the existing app/project rather than clearing app storage. Keep the applicationId/package name unchanged.

WHAT CHANGED
- XML-based screen shell in app/src/main/res/layout/activity_main.xml
- Blue/white palette in res/values/colors.xml
- Rounded card, button, and input backgrounds in res/drawable/
- Refreshed Java screens: login, dashboard, subject/target management, attendance marking, history
- Preserves Present, Absent, Cancelled, Undo, add/delete subject, target, and history features

HOW TO APPLY TO YOUR EXISTING PROJECT
1. Close Android Studio or stop any running build.
2. Back up the entire project folder first.
3. In the existing project, copy the new MainActivity.java to:
   app/src/main/java/com/example/attendancetracker/MainActivity.java
4. Copy activity_main.xml into:
   app/src/main/res/layout/activity_main.xml
5. Copy the drawable XML files into app/src/main/res/drawable/.
6. Copy colors.xml into app/src/main/res/values/colors.xml. If your existing colors.xml already contains other colors, merge these entries instead of overwriting it.
7. Keep your existing AndroidManifest.xml, Gradle files, applicationId, and package name.
8. In Android Studio choose File > Sync Project with Gradle Files if prompted, then Build > Make Project.
9. Run the app and test login, add subject, Present/Absent/Cancelled, Undo, history, and target saving.
10. When satisfied, choose Build > Generate App Bundles or APKs > Build APK(s).

DATA NOTE
SharedPreferences data is stored on the device/emulator. Updating the same installed application normally preserves it, but uninstalling/clearing app data can erase it. Keep the backup regardless.

LOGIN NOTE
This is a classroom demo login screen; it does not implement real account/password authentication. The password field is for presentation only.
