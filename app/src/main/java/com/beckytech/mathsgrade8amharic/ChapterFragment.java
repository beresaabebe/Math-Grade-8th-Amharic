package com.beckytech.mathsgrade8amharic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beckytech.mathsgrade8amharic.adapter.ChapterPageAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChapterFragment extends Fragment {

    private static final String ARG_START_PAGE = "start_page";
    private static final String ARG_END_PAGE = "end_page";

    private PdfRendererHelper pdfRendererHelper;
    private AdManager adManager;

    public static ChapterFragment newInstance(int startPage, int endPage) {
        ChapterFragment fragment = new ChapterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_START_PAGE, startPage);
        args.putInt(ARG_END_PAGE, endPage);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_chapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adManager = new AdManager();
        try {
            pdfRendererHelper = new PdfRendererHelper(requireContext(), "mat8.pdf");
            
            int start = getArguments().getInt(ARG_START_PAGE);
            int end = getArguments().getInt(ARG_END_PAGE);
            
            List<Integer> pages = new ArrayList<>();
            for (int i = start; i <= end; i++) {
                pages.add(i);
            }
            
            ChapterPageAdapter adapter = new ChapterPageAdapter(pdfRendererHelper, pages, adManager);
            recyclerView.setAdapter(adapter);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pdfRendererHelper != null) {
            pdfRendererHelper.close();
        }
    }
}
