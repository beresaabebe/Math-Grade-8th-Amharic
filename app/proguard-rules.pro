# AdMob
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.ads.mediation.** { *; }

# Facebook Audience Network
-keep class com.facebook.ads.** { *; }

# Unity Ads
-keep class com.unity3d.ads.** { *; }
-keep class com.unity3d.services.** { *; }

# Firebase
-keep class com.google.firebase.** { *; }

# Play Core
-keep class com.google.android.play.core.** { *; }

# Models
-keep class com.beckytech.mathsgrade8amharic.model.** { *; }

# Support for 16KB and other crashes
-keep class androidx.work.impl.** { *; }
-keep class androidx.lifecycle.ProcessLifecycleOwnerInitializer { *; }

# R8 missing classes rules
-dontwarn com.facebook.infer.annotation.Nullsafe
-dontwarn com.unity3d.ads.AdFormat
-dontwarn com.unity3d.ads.IUnityAdsInitializationListener
-dontwarn com.unity3d.ads.IUnityAdsLoadListener
-dontwarn com.unity3d.ads.IUnityAdsShowListener
-dontwarn com.unity3d.ads.IUnityAdsTokenListener
-dontwarn com.unity3d.ads.TokenConfiguration
-dontwarn com.unity3d.ads.UnityAds$UnityAdsInitializationError
-dontwarn com.unity3d.ads.UnityAds$UnityAdsLoadError
-dontwarn com.unity3d.ads.UnityAds$UnityAdsShowCompletionState
-dontwarn com.unity3d.ads.UnityAds$UnityAdsShowError
-dontwarn com.unity3d.ads.UnityAds
-dontwarn com.unity3d.ads.UnityAdsLoadOptions
-dontwarn com.unity3d.ads.UnityAdsShowOptions
-dontwarn com.unity3d.ads.metadata.MediationMetaData
-dontwarn com.unity3d.ads.metadata.MetaData
-dontwarn com.unity3d.services.banners.BannerErrorCode
-dontwarn com.unity3d.services.banners.BannerErrorInfo
-dontwarn com.unity3d.services.banners.BannerView$IListener
-dontwarn com.unity3d.services.banners.BannerView
-dontwarn com.unity3d.services.banners.UnityBannerSize
