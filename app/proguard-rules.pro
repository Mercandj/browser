# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Programmation_Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

 # Retrofit
-dontwarn okio.**
-dontnote okio.**
-dontwarn retrofit2.**
-dontnote retrofit2.**
-keep class retrofit2.** { *; }

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }

# com.makeramen.roundedimageview
-dontwarn com.squareup.picasso.**
