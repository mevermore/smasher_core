
#############################################

-keepattributes *Annotation*
#noinspection ShrinkerUnresolvedReference
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

# OkHttp3
-dontwarn okhttp3.**
#noinspection ShrinkerUnresolvedReference
-keep class okhttp3.** { *;}
-dontwarn okhttp3.logging.**
#noinspection ShrinkerUnresolvedReference
-keep class okhttp3.internal.**{*;}

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

# Retrofit
-dontwarn retrofit2.**
#noinspection ShrinkerUnresolvedReference
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-dontwarn javax.annotation.**
-dontwarn javax.inject.**

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}

#noinspection ShrinkerUnresolvedReference
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
#noinspection ShrinkerUnresolvedReference
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

#noinspection ShrinkerUnresolvedReference
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
#noinspection ShrinkerUnresolvedReference
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}


# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
