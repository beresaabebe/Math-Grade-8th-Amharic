package com.beckytech.mathsgrade8amharic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;

import java.util.Random;

public class AdManager {

    private InterstitialAd mInterstitialAd;
    private RewardedAd mRewardedAd;
    private RewardedInterstitialAd mRewardedInterstitialAd;
    
    public interface AdListenerCallback {
        void onAdClosed();
    }

    public void loadInterstitial(Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, context.getString(R.string.google_interstitial_ads_unit_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }

    public void showInterstitial(Activity activity, AdListenerCallback callback) {
        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    mInterstitialAd = null;
                    loadInterstitial(activity);
                    if (callback != null) callback.onAdClosed();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                    mInterstitialAd = null;
                    if (callback != null) callback.onAdClosed();
                }
            });
            mInterstitialAd.show(activity);
        } else {
            if (callback != null) callback.onAdClosed();
            loadInterstitial(activity);
        }
    }

    public void loadRewarded(Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(context, context.getString(R.string.google_rewarded_ads_unit_id), adRequest,
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mRewardedAd = null;
                    }
                });
    }

    public void showRewarded(Activity activity, AdListenerCallback callback) {
        if (mRewardedAd != null) {
            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    mRewardedAd = null;
                    loadRewarded(activity);
                    if (callback != null) callback.onAdClosed();
                }
            });
            mRewardedAd.show(activity, rewardItem -> {});
        } else {
            if (callback != null) callback.onAdClosed();
            loadRewarded(activity);
        }
    }

    public void loadRewardedInterstitial(Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedInterstitialAd.load(context, context.getString(R.string.google_rewarded_interstitial_ads_unit_id), adRequest,
                new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedInterstitialAd rewardedInterstitialAd) {
                        mRewardedInterstitialAd = rewardedInterstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mRewardedInterstitialAd = null;
                    }
                });
    }

    public void showRewardedInterstitial(Activity activity, AdListenerCallback callback) {
        if (mRewardedInterstitialAd != null) {
            mRewardedInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    mRewardedInterstitialAd = null;
                    loadRewardedInterstitial(activity);
                    if (callback != null) callback.onAdClosed();
                }
            });
            mRewardedInterstitialAd.show(activity, rewardItem -> {});
        } else {
            if (callback != null) callback.onAdClosed();
            loadRewardedInterstitial(activity);
        }
    }
    
    public void showRandomRewarded(Activity activity, AdListenerCallback callback) {
        if (new Random().nextInt(2) == 0) {
            showRewarded(activity, callback);
        } else {
            showRewardedInterstitial(activity, callback);
        }
    }

    public void loadCollapsibleBanner(Activity activity, FrameLayout adContainer) {
        AdView adView = new AdView(activity);
        adView.setAdUnitId(activity.getString(R.string.google_banner_ad_unit_id));
        adView.setAdSize(getAdSize(activity));
        adContainer.removeAllViews();
        adContainer.addView(adView);

        Bundle extras = new Bundle();
        extras.putString("collapsible", "bottom");
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize(Activity activity) {
        // Step 2 - Determine the screen width (less offsetting margins) to use for the ad width.
        android.view.Display display = activity.getWindowManager().getDefaultDisplay();
        android.util.DisplayMetrics outMetrics = new android.util.DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }
    
    public void loadNativeAd(Context context, NativeAd.OnNativeAdLoadedListener listener) {
        AdLoader adLoader = new AdLoader.Builder(context, context.getString(R.string.google_native_ad_unit_id))
                .forNativeAd(listener)
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        .setVideoOptions(new VideoOptions.Builder()
                                .setStartMuted(true)
                                .build())
                        .build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }
}
