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
#-keep class * extends androidx.room.RoomDatabase {
#    public static <methods>;
#}

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

# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

# Kotlin
-keep class kotlin.Metadata { *; }
-keepclassmembers class **$WhenMappings { *; }
-dontwarn kotlinx.coroutines.debug.**

# Hilt
-keep class dagger.hilt.internal.aggregatedroot.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager { *; }
-keep class * extends dagger.hilt.android.internal.managers.ActivityComponentManager { *; }
-keep class * extends dagger.hilt.android.internal.managers.FragmentComponentManager { *; }
-keep class * extends dagger.hilt.android.internal.managers.ServiceComponentManager { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager { *; }
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory$ViewModelModule { *; }
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory { *; }

# Retrofit + OkHttp
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Kotlin Serialization
-keepclassmembers class kotlinx.serialization.json.** { *; }
-keep class kotlinx.serialization.json.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# Navigation Component
-keep class androidx.navigation.** { *; }
-keepclassmembers class * implements androidx.navigation.NavController {
    public <init>(...);
}

# ViewModel
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# DataStore
-keep class androidx.datastore.** { *; }

# SkeletonLayout
-keep class com.faltenreich.skeletonlayout.** { *; }

# Confetti
-keep class nl.dionsegijn.** { *; }

# Arrow
-keep class arrow.core.** { *; }

# Material Components
-keep class com.google.android.material.** { *; }

# AndroidX
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
-keep public class * extends androidx.fragment.app.Fragment

# Сериализуемые модели
-keepclassmembers class com.eltex.androidschool.model.** {
    public *;
}

# Для дебага
-keepattributes SourceFile,LineNumberTable
-keep class kotlin.coroutines.jvm.internal.** { *; }

# Room (если раскомментируете)
#-keep class * extends androidx.room.RoomDatabase
#-keep class * extends androidx.room.Entity

#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

# =====================================================================================
# Правила для Kotlin
# =====================================================================================

# Сохраняем метаданные Kotlin, необходимые для рефлексии и корутин
-keep class kotlin.Metadata { *; }
-keep class kotlin.** { *; } # Может быть избыточно, но безопасно
-keep interface kotlin.jvm.functions.** { *; }

# Сохраняем классы и члены, аннотированные JvmField/JvmStatic
-keepclasseswithmembers class * {
    @kotlin.jvm.JvmField <fields>;
}
-keepclasseswithmembers class * {
    @kotlin.jvm.JvmStatic <methods>;
}

# Сохраняем companion objects, если они используются (например, для сериализации)
# Это правило может быть слишком широким, но часто необходимо.
# Если возникнут проблемы или размер APK будет слишком большим, можно уточнить.
-keep class * {*** Companion;}

# Сохраняем корутины (особенно если используются debug probes)
# -keep class kotlinx.coroutines.debug.** { *; } # Раскомментируй, если используешь или есть проблемы

# Подавляем предупреждения для Kotlin, т.к. правила выше должны покрывать основное
-dontwarn kotlin.**
-dontwarn kotlinx.**

# =====================================================================================
# Правила для Kotlin Serialization (kotlinx.serialization)
# =====================================================================================
# Необходимо сохранять классы, помеченные @Serializable, их поля и конструкторы,
# а также сгенерированные классы-сериализаторы.

-keepnames class kotlinx.serialization.Serializable
-keep class * implements kotlinx.serialization.Serializable { *; }

# Сохраняем поля, помеченные @SerialName или @Transient (на всякий случай)
-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
    @kotlinx.serialization.Transient <fields>;
}

# Сохраняем сгенерированные классы-сериализаторы (обычно имеют суффикс $$serializer)
-keep class **$$serializer { *; }
-keepclassmembers class * implements kotlinx.serialization.KSerializer { *; }

# Сохраняем методы enum'ов, если они используются в сериализации
-keepclassmembers enum * {
    <methods>; # Включает values() и valueOf()
}

# =====================================================================================
# Правила для Hilt (Dagger Hilt)
# =====================================================================================
# Hilt сильно полагается на аннотации и сгенерированный код.

# Сохраняем аннотации Hilt
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.** class * { *; }

# Сохраняем классы, аннотированные Hilt'ом
-keep @dagger.hilt.InstallIn class *
-keep @dagger.hilt.components.SingletonComponent class *
-keep @dagger.hilt.DefineComponent class * { *; }
-keep @dagger.hilt.EntryPoint class * { *; }
-keep @dagger.hilt.android.HiltAndroidApp class * { <init>(...); } # Сохраняем конструктор Application класса
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { <init>(...); } # Сохраняем конструктор ViewModel

# Сохраняем сгенерированный Hilt код (имена могут немного варьироваться)
-keep class *_HiltModules* { *; }
-keep class Hilt_* { *; }
-keep class Dagger*Component { *; }
-keep class *Dagger*Component { *; }
-keep class *_*Factory { *; }
-keep class *_*MembersInjector { *; }

# Подавляем предупреждения для Hilt/Dagger/Javax.inject
-dontwarn dagger.hilt.**
-dontwarn javax.inject.**

# =====================================================================================
# Правила для Retrofit & OkHttp
# =====================================================================================
# Основная задача - сохранить data-классы (DTO), которые используются для (де)сериализации JSON.
# Kotlin Serialization менее зависим от имен полей, чем Gson, но сохранять классы все равно нужно.

# Замени com.eltex.androidschool.dto.** на реальный пакет с твоими DTO/Model классами
-keep class com.eltex.androidschool.dto.** { *; }
# Если имена классов важны (например, для рефлексии где-то еще), можно добавить:
# -keepnames class com.eltex.androidschool.dto.**

# Сохраняем интерфейсы Retrofit API
# Замени com.eltex.androidschool.api.** на реальный пакет с твоими API интерфейсами
-keep interface com.eltex.androidschool.api.** { *; }

# OkHttp обычно не требует специальных правил, но подавим предупреждения
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**

# =====================================================================================
# Правила для Glide
# =====================================================================================
# Необходимо сохранять кастомные AppGlideModule и LibraryGlideModule, если они есть.

-keep public class * extends com.bumptech.glide.module.AppGlideModule {
    <init>(...);
}
-keep public class * extends com.bumptech.glide.module.LibraryGlideModule {
    <init>(...);
}
# Сохраняем сгенерированный API Glide
-keep public class com.bumptech.glide.GeneratedAppGlideModule { *; }
-keep class * implements com.bumptech.glide.load.model.ModelLoader
-keep class * implements com.bumptech.glide.load.ResourceDecoder

# Подавляем предупреждения для Glide
-dontwarn com.bumptech.glide.**

# =====================================================================================
# Правила для ViewBinding (Обычно не требуются)
# =====================================================================================
# R8/ProGuard обычно корректно обрабатывают ViewBinding. Правила нужны только при проблемах.
# -keep class * implements androidx.viewbinding.ViewBinding { *; }
# -keep class **.databinding.** { *; } # Замени ** на твой пакет

# =====================================================================================
# Правила для AndroidX (Lifecycle, Navigation, Core, etc.)
# =====================================================================================
# Большинство библиотек AndroidX включают свои собственные consumer Proguard rules,
# поэтому явные правила обычно не нужны, если только ты не используешь рефлексию с ними.

# Пример: сохранение кастомных View, используемых в layout XML
# -keep public class com.eltex.androidschool.view.** { *; }

# Пример: сохранение методов, вызываемых через android:onClick в XML
# -keepclassmembers class * {
#    public void *(android.view.View);
# }

# =====================================================================================
# Правила для Arrow-kt
# =====================================================================================
# Обычно не требуются для arrow-core, если не используются специфичные возможности рефлексии.
-dontwarn arrow.**

# =====================================================================================
# Правила для других библиотек (SkeletonLayout, Confetti)
# =====================================================================================
# SkeletonLayout и Confetti обычно не требуют специальных правил.
# Если возникнут проблемы с ними в release-сборке, добавь правила сюда.
# Например:
# -keep class com.faltenreich.skeletonlayout.** { *; }
# -keep class nl.dionsegijn.konfetti.** { *; }

# =====================================================================================
# Правила для закомментированных библиотек (на будущее)
# =====================================================================================

# --- Правила для Room (Если раскомментируешь) ---
# -keep class androidx.room.** { *; }
# -keep @androidx.room.Entity class * { *; }
# -keep @androidx.room.Dao interface * { *; }
# -keep @androidx.room.Database class * { *; }
# -keep @androidx.room.TypeConverter class * { *; }
# -keep @androidx.room.Embedded class * { *; }
# -keepclassmembers class * { @androidx.room.Relation *; }
# -dontwarn androidx.room.**

# --- Правила для @Parcelize (Если раскомментируешь kotlin-parcelize) ---
# -keep class * implements android.os.Parcelable {
#   public static final android.os.Parcelable$Creator *;
# }
# -keepnames class * implements android.os.Parcelable # Может понадобиться, если имя класса используется где-то еще

# --- Правила для Protobuf Lite (Если раскомментируешь DataStore + Protobuf) ---
# -keep class com.google.protobuf.** { *; }
# -keep public class * extends com.google.protobuf.GeneratedMessageLite { *; }
# -keep public class * extends com.google.protobuf.GeneratedMessageLite$Builder { *; }
# # Сохраняем сгенерированные классы (замени com.eltex.androidschool.proto.** на твой пакет)
# -keep class com.eltex.androidschool.proto.** { *; }
# -dontwarn com.google.protobuf.**

# =====================================================================================
# Общие рекомендации
# =====================================================================================

# Не удаляй классы, которые используются в AndroidManifest.xml (Activities, Services, etc.)
# R8 обычно справляется с этим автоматически.

# Сохраняй нативные методы, если используешь JNI
# -keepclasseswithmembernames class * {
#    native <methods>;
# }

# Можно добавить правила для сохранения имен классов/методов, если используешь рефлексию
# -keep public class MyClassWithReflection {
#    public void myReflectedMethod();
# }
# -keepnames class com.example.MyDataClass
