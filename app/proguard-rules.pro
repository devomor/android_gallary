# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/dnld/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep fullSizeOptions here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes *Annotation*
-keepclassmembers class * {
    @com.* <methods>;
}
-keep enum com.* { *; }

# And if you use AsyncExecutor:
-keepclassmembers class * extends com.* {
    <init>(*);
}

-dontwarn com.androidlab.bokehoverlay.**

#-keepclassmembers class com** { <fields>; }
#-keepclassmembers class com** { <methods>; }
