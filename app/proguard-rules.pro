# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Основные правила для Android
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose

# Сохраняем аннотации (например, для Retrofit, Dagger и других библиотек)
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses

# Сохраняем исходные имена файлов и номера строк для stack traces
-keepattributes SourceFile,LineNumberTable

# Сохраняем классы, которые используются в манифесте
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

# Сохраняем View и их методы, чтобы они не были обфусцированы
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

# Сохраняем классы, которые используются в ресурсах (например, в XML-макетах)
-keep class **.R$* {
    public static final int *;
}

# Сохраняем классы, которые используются в нативных методах (JNI)
-keepclasseswithmembernames class * {
    native <methods>;
}

# Сохраняем классы, которые используются в сериализации (например, Parcelable)
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Сохраняем классы, которые используются в Gson
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }
#-keep class * implements com.google.gson.TypeAdapterFactory
#-keep class * implements com.google.gson.JsonSerializer
#-keep class * implements com.google.gson.JsonDeserializer

# Сохраняем классы, которые используются в Retrofit
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep class okio.** { *; }

# Сохраняем классы, которые используются в Dagger
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
#-keep class * extends dagger.internal.Binding
#-keep class * extends dagger.internal.ModuleAdapter
#-keep class * extends dagger.internal.StaticInjection

# Сохраняем классы, которые используются в Room
-keep class * extends androidx.room.RoomDatabase {
    public static <methods>;
}

# Сохраняем классы, которые используются в Coroutines
-keep class kotlinx.coroutines.** { *; }

# Сохраняем классы, которые используются в ViewModel
-keep class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}

# Сохраняем классы, которые используются в LiveData
-keep class * extends androidx.lifecycle.LiveData {
    public <init>(...);
}

# Сохраняем классы, которые используются в Navigation Component
-keep class * extends androidx.navigation.NavController {
    public <init>(...);
}

# Сохраняем классы, которые используются в WorkManager
-keep class androidx.work.** { *; }

# Сохраняем классы, которые используются в Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Сохраняем классы, которые используются в Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# Сохраняем классы, которые используются в Moshi
-keep class com.squareup.moshi.** { *; }
#-keep class * implements com.squareup.moshi.JsonAdapter$Factory

# Сохраняем классы, которые используются в Kotlin
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }

# Сохраняем классы, которые используются в вашем приложении
# (Замените com.eltex.androidschool на ваш пакет)
-keep class com.eltex.androidschool.** { *; }
-keep class com.eltex.androidschool.model.** { *; }
-keep class com.eltex.androidschool.viewmodel.** { *; }
-keep class com.eltex.androidschool.repository.** { *; }

# Сохраняем классы, которые используются в ресурсах (например, ButterKnife, DataBinding)
-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }
-keep class **$$DataBinder { *; }

# Сохраняем классы, которые используются в ресурсах (например, для анимаций)
-keep class **.R$* {
    public static final int *;
}
