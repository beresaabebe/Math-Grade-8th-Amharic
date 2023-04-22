package com.beckytech.mathsgrade8amharic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.beckytech.mathsgrade8amharic.activity.AboutActivity;
import com.beckytech.mathsgrade8amharic.activity.BookDetailActivity;
import com.beckytech.mathsgrade8amharic.adapter.Adapter;
import com.beckytech.mathsgrade8amharic.adapter.MoreAppsAdapter;
import com.beckytech.mathsgrade8amharic.contents.ContentEndPage;
import com.beckytech.mathsgrade8amharic.contents.ContentStartPage;
import com.beckytech.mathsgrade8amharic.contents.MoreAppImages;
import com.beckytech.mathsgrade8amharic.contents.MoreAppUrl;
import com.beckytech.mathsgrade8amharic.contents.MoreAppsName;
import com.beckytech.mathsgrade8amharic.contents.SubTitleContents;
import com.beckytech.mathsgrade8amharic.contents.TitleContents;
import com.beckytech.mathsgrade8amharic.model.Model;
import com.beckytech.mathsgrade8amharic.model.MoreAppsModel;
import com.facebook.ads.Ad;
import com.facebook.ads.AdSize;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.github.barteksc.pdfviewer.BuildConfig;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Adapter.onBookClicked, MoreAppsAdapter.MoreAppsClicked {

    private final List<Model> list = new ArrayList<>();
    private final ContentStartPage startPage = new ContentStartPage();
    private final TitleContents titleContents = new TitleContents();
    private final ContentEndPage endPage = new ContentEndPage();
    private final SubTitleContents subTitleContents = new SubTitleContents();
    private final MoreAppImages images = new MoreAppImages();
    private final MoreAppUrl url = new MoreAppUrl();
    private final MoreAppsName appsName = new MoreAppsName();
    private final String TAG = BookDetailActivity.class.getSimpleName();
    com.facebook.ads.InterstitialAd interstitialAd;
    private InterstitialAd mInterstitialAd;
    private List<MoreAppsModel> moreAppsModelList;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        callAds();

        AppRate.app_launched(this);

        MobileAds.initialize(this, initializationStatus -> {
        });

        setAds();

        //get the reference to your FrameLayout
        FrameLayout adContainerView = findViewById(R.id.adView_container);
        //Create an AdView and put it into your FrameLayout
        adView = new AdView(this);
        adContainerView.addView(adView);
        adView.setAdUnitId("ca-app-pub-8504401574247581/9009648795");
//        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        //start requesting banner ads
        loadBanner();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(item -> {
            MenuOptions(item);
            return true;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView_main_item);
        getData();
        Adapter adapter = new Adapter(list, this);
        recyclerView.setAdapter(adapter);

        RecyclerView moreAppsRecyclerView = findViewById(R.id.moreAppsRecycler);
        getMoreApps();
        MoreAppsAdapter moreAppsAdapter = new MoreAppsAdapter(moreAppsModelList, this);
        moreAppsRecyclerView.setAdapter(moreAppsAdapter);
    }

    private void getMoreApps() {
        moreAppsModelList = new ArrayList<>();
        for (int i = 0; i < appsName.appNames.length; i++) {
            moreAppsModelList.add(new MoreAppsModel(appsName.appNames[i], url.url[i], images.images[i]));
        }
    }

    private void getData() {
        for (int i = 0; i < titleContents.title.length; i++) {
            list.add(new Model(titleContents.title[i].substring(0, 1).toUpperCase() + "" + titleContents.title[i].substring(1).toLowerCase(),
                    subTitleContents.subTitle[i],
                    endPage.pageEnd[i],
                    startPage.pageStart[i]));
        }
    }

    @Override
    public void appClicked(MoreAppsModel model) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(model.getUrl()));
        startActivity(intent);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void MenuOptions(MenuItem item) {
        if (item.getItemId() == R.id.action_about_us) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(MainActivity.this);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();

                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        mInterstitialAd = null;
                        setAds();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });
            } else {
                startActivity(new Intent(this, AboutActivity.class));
            }
        }

        if (item.getItemId() == R.id.action_rate) {
            String pkg = getPackageName();
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + pkg)));
        }

        if (item.getItemId() == R.id.action_more_apps) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(MainActivity.this);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/dev?id=6669279757479011928")));
                        mInterstitialAd = null;
                        setAds();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });
            } else {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/dev?id=6669279757479011928")));
            }
        }

        if (item.getItemId() == R.id.action_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, "Download this app from Play store \n" + url);
            startActivity(Intent.createChooser(intent, "Choose to send"));
        }

        if (item.getItemId() == R.id.action_update) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            int lastVersion = pref.getInt("lastVersion", 0);
            String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
            if (lastVersion < BuildConfig.VERSION_CODE) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                Toast.makeText(this, "New update is available download it from play store!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No update available!", Toast.LENGTH_SHORT).show();
            }
        }
        if (item.getItemId() == R.id.action_exit) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle(getString(R.string.exit))
                    .setMessage("መዝጋት ይፈልጋሉ?")
                    .setPositiveButton("አዎ", (dialog, which) -> {
                        System.exit(0);
                        finish();
                    })
                    .setNegativeButton("ተወው", (dialog, which) -> dialog.dismiss())
                    .setBackground(getResources().getDrawable(R.drawable.nav_header_bg, null))
                    .show();
        }
    }

    @Override
    public void clickedBook(Model model) {
        int rand = (int) (Math.random() * 100);
        if (rand % 2 != 0) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(MainActivity.this);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        startActivity(new Intent(MainActivity.this, BookDetailActivity.class).putExtra("data", model));
                        mInterstitialAd = null;
                        setAds();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });
            } else {
                startActivity(new Intent(this, BookDetailActivity.class).putExtra("data", model));
            }
        } else {
            startActivity(new Intent(this, BookDetailActivity.class).putExtra("data", model));
        }
    }

    private void setAds() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, getString(R.string.test_interstitial_ads_unit_id), adRequest,
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

    private void callAds() {
        AudienceNetworkAds.initialize(this);

        //        513372960928869_513374324262066
        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(this, "587359836775376_587361160108577", AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = findViewById(R.id.banner_container);
        adContainer.addView(adView);
        adView.loadAd();

        interstitialAd = new com.facebook.ads.InterstitialAd(this, "587359836775376_587369096774450");
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

    private com.google.android.gms.ads.AdSize getAdSize() {
        //Determine the screen width to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        //you can also pass your selected width here in dp
        int adWidth = (int) (widthPixels / density);

        //return the optimal size depends on your orientation (landscape or portrait)
        return com.google.android.gms.ads.AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    private void loadBanner() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        com.google.android.gms.ads.AdSize adSize = getAdSize();
        // Set the adaptive ad size to the ad view.
        adView.setAdSize(adSize);

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }
}