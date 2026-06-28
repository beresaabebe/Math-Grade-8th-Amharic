package com.beckytech.mathsgrade8amharic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.beckytech.mathsgrade8amharic.activity.AboutActivity;
import com.beckytech.mathsgrade8amharic.activity.BookDetailActivity;
import com.beckytech.mathsgrade8amharic.activity.PrivacyActivity;
import com.beckytech.mathsgrade8amharic.adapter.Adapter;
import com.beckytech.mathsgrade8amharic.adapter.MoreAppsAdapter;
import com.beckytech.mathsgrade8amharic.contents.MoreAppImages;
import com.beckytech.mathsgrade8amharic.contents.MoreAppUrl;
import com.beckytech.mathsgrade8amharic.contents.MoreAppsName;
import com.beckytech.mathsgrade8amharic.contents.SubTitleContents;
import com.beckytech.mathsgrade8amharic.contents.TitleContents;
import com.beckytech.mathsgrade8amharic.model.Model;
import com.beckytech.mathsgrade8amharic.model.MoreAppsModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Adapter.onBookClicked, MoreAppsAdapter.MoreAppsClicked {

    private final List<Model> list = new ArrayList<>();
    private final MoreAppImages images = new MoreAppImages();
    private final MoreAppUrl url = new MoreAppUrl();
    private final MoreAppsName appsName = new MoreAppsName();
    private List<MoreAppsModel> moreAppsModelList;
    private DrawerLayout drawerLayout;
    private AdManager adManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        adManager = new AdManager();
        adManager.loadCollapsibleBanner(this, findViewById(R.id.banner_container));
        adManager.loadInterstitial(this);

        AppRate.app_launched(this);
        AppUpdateReviewHelper.checkUpdate(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
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
        list.clear();
        for (int i = 0; i < TitleContents.title.length; i++) {
            list.add(new Model(TitleContents.title[i].substring(0, 1).toUpperCase() +
                    TitleContents.title[i].substring(1).toLowerCase(),
                    SubTitleContents.subTitle[i],
                    0, // not used in new flow
                    0)); // not used in new flow
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
        drawerLayout.closeDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.action_privacy) {
            startActivity(new Intent(this, PrivacyActivity.class));
        }
        if (item.getItemId() == R.id.action_about_us) {
            adManager.showInterstitial(this, () -> startActivity(new Intent(this, AboutActivity.class)));
        }

        if (item.getItemId() == R.id.action_rate) {
            adManager.showInterstitial(this, () -> {
                String pkg = getPackageName();
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + pkg)));
            });
        }

        if (item.getItemId() == R.id.action_more_apps) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/dev?id=6669279757479011928")));
        }

        if (item.getItemId() == R.id.action_share) {
            adManager.showInterstitial(this, () -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_TEXT, "Download this app from Play store \n" + url);
                startActivity(Intent.createChooser(intent, "Choose to send"));
            });
        }

        if (item.getItemId() == R.id.action_update) {
            AppUpdateReviewHelper.checkUpdate(this);
        }
        if (item.getItemId() == R.id.action_exit) {
            adManager.showInterstitial(this, () -> {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.MyAlertDialog);
                builder.setTitle(getString(R.string.exit))
                        .setMessage("Do you want to exit?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            finish();
                            System.exit(0);
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .show();
            });
        }
    }

    @Override
    public void clickedBook(Model model) {
        adManager.showInterstitial(this, () -> startActivity(new Intent(this, BookDetailActivity.class).putExtra("data", model)));
    }
}
