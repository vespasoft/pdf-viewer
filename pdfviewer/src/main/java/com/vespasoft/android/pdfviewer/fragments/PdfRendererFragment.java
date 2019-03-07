/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vespasoft.android.pdfviewer.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.vespasoft.android.pdfviewer.R;
import com.vespasoft.android.pdfviewer.activities.PdfViewerActivity;
import com.vespasoft.android.pdfviewer.presenter.PdfRendererPresenter;
import com.vespasoft.android.pdfviewer.views.PdfRenderer;

import java.io.File;

/**
 * This fragment has a big {@ImageView} that shows PDF pages, and 2
 * {@link Button}s to move between pages. We use a
 * {@link android.graphics.pdf.PdfRenderer} to render PDF pages as
 * {@link Bitmap}s.
 */
public class PdfRendererFragment extends Fragment implements PdfRenderer {

    /**
     * Key string for saving the state of current page index.
     */
    private static final String STATE_CURRENT_PAGE_INDEX = "current_page_index";

    /**
     * {@link ImageView} that shows a PDF page as a {@link Bitmap}
     */
    private ImageView mViewer;

    private PdfRendererPresenter pdfRendererPresenter;

    private int mPageIndex = 0;

    public static PdfRendererFragment newInstance(int page) {
        PdfRendererFragment fragmentFirst = new PdfRendererFragment();
        Bundle args = new Bundle();
        args.putInt(STATE_CURRENT_PAGE_INDEX, page);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    public PdfRendererFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // when the fragment has instancied by newInstance method
        mPageIndex = getArguments().getInt(STATE_CURRENT_PAGE_INDEX, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pdf_renderer, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PdfViewerActivity activity = (PdfViewerActivity) getActivity();
        Uri uri = activity.getIntent().getData();
        File file = new File(uri.getPath());

        // Retain view references.
        mViewer = view.findViewById(R.id.viewer);


        // If there is a savedInstanceState (screen orientations, etc.), we restore the page index.
        if (null != savedInstanceState) {
            mPageIndex = savedInstanceState.getInt(STATE_CURRENT_PAGE_INDEX, 0);
        }
        // create instance of View Presenter
        pdfRendererPresenter = new PdfRendererPresenter();
        pdfRendererPresenter.setView(this, file, mPageIndex);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        pdfRendererPresenter.renderFile();
    }

    @Override
    public void onStop() {
        /*try {
            pdfViewerPresenter.onDestroy();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != pdfRendererPresenter) {
            outState.putInt(STATE_CURRENT_PAGE_INDEX, pdfRendererPresenter.getPageIndex());
        }
    }

    @Override
    public void renderTitle(String title) {
        //getActivity().setTitle(title);
    }

    @Override
    public void renderPage(Bitmap bitmap) {
        // We are ready to show the Bitmap to user.
        mViewer.setImageBitmap(bitmap);
    }

    @Override
    public void onNextPage() {
        pdfRendererPresenter.showNextPage();
    }

    @Override
    public void onPreviousPage() {
        pdfRendererPresenter.showPreviousPage();
    }

    @Override
    public void onShowPage(int index) {
        pdfRendererPresenter.showPage(index);
    }

    @Override
    public Resources resources() {
        return getResources();
    }

    @Override
    public Context context() {
        return getContext();
    }
}
