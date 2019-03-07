package com.vespasoft.android.pdfviewer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vespasoft.android.pdfviewer.fragments.PdfRendererFragment;

public class DocumentPageAdapter extends FragmentPagerAdapter {
    private int pages = 0;

    public DocumentPageAdapter(FragmentManager fragmentManager, int totalPages) {
        super(fragmentManager);
        this.pages = totalPages;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return pages;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return PdfRendererFragment.newInstance(position);
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}
