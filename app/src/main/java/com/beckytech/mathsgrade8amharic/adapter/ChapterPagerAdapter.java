package com.beckytech.mathsgrade8amharic.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.beckytech.mathsgrade8amharic.ChapterFragment;
import com.beckytech.mathsgrade8amharic.contents.ContentEndPage;
import com.beckytech.mathsgrade8amharic.contents.ContentStartPage;
import com.beckytech.mathsgrade8amharic.contents.TitleContents;

public class ChapterPagerAdapter extends FragmentStateAdapter {

    public ChapterPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int start = ContentStartPage.pageStart[position];
        int end = ContentEndPage.pageEnd[position];
        return ChapterFragment.newInstance(start, end);
    }

    @Override
    public int getItemCount() {
        return TitleContents.title.length;
    }
}
