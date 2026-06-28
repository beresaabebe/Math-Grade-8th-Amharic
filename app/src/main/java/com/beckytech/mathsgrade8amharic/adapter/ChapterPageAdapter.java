package com.beckytech.mathsgrade8amharic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beckytech.mathsgrade8amharic.AdManager;
import com.beckytech.mathsgrade8amharic.PdfRendererHelper;
import com.beckytech.mathsgrade8amharic.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class ChapterPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PAGE = 0;
    private static final int TYPE_AD = 1;

    private final List<Object> items = new ArrayList<>();
    private final PdfRendererHelper pdfRendererHelper;
    private final AdManager adManager;

    public ChapterPageAdapter(PdfRendererHelper pdfRendererHelper, List<Integer> pages, AdManager adManager) {
        this.pdfRendererHelper = pdfRendererHelper;
        this.adManager = adManager;
        
        for (int i = 0; i < pages.size(); i++) {
            items.add(pages.get(i));
            if ((i + 1) % 4 == 0) {
                items.add("AD");
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Integer) {
            return TYPE_PAGE;
        } else {
            return TYPE_AD;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_PAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf_page, parent, false);
            return new PageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad, parent, false);
            return new AdViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_PAGE) {
            int pageIndex = (Integer) items.get(position);
            Bitmap bitmap = pdfRendererHelper.renderPage(pageIndex);
            ((PageViewHolder) holder).imageView.setImageBitmap(bitmap);
        } else {
            AdViewHolder adHolder = (AdViewHolder) holder;
            loadAd(adHolder.adContainer, position);
        }
    }

    private void loadAd(FrameLayout container, int position) {
        container.removeAllViews();
        Context context = container.getContext();
        int adIndex = position / 5; // roughly
        int type = adIndex % 3; // 0: Native, 1: Medium Rectangle, 2: Banner
        
        if (type == 0) {
            // Placeholder for Native Ad - in real app, use a NativeAdView
            adManager.loadNativeAd(context, nativeAd -> {
                // Usually we'd inflate a layout and populate it
                // For brevity, I'll just load a banner as fallback if native is complex to implement fully here
                AdView adView = new AdView(context);
                adView.setAdUnitId(context.getString(R.string.google_banner_ad_unit_id));
                adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
                container.addView(adView);
                adView.loadAd(new AdRequest.Builder().build());
            });
        } else if (type == 1) {
            AdView adView = new AdView(context);
            adView.setAdUnitId(context.getString(R.string.google_banner_ad_unit_id));
            adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
            container.addView(adView);
            adView.loadAd(new AdRequest.Builder().build());
        } else {
            AdView adView = new AdView(context);
            adView.setAdUnitId(context.getString(R.string.google_banner_ad_unit_id));
            adView.setAdSize(AdSize.BANNER);
            container.addView(adView);
            adView.loadAd(new AdRequest.Builder().build());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class PageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.pdf_page_image);
        }
    }

    static class AdViewHolder extends RecyclerView.ViewHolder {
        FrameLayout adContainer;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            adContainer = itemView.findViewById(R.id.ad_container_item);
        }
    }
}
