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
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.vespasoft.android.pdfviewer.R;
import com.vespasoft.android.pdfviewer.activities.PdfViewerActivity;
import com.vespasoft.android.pdfviewer.presenter.PdfViewerPresenter;
import com.vespasoft.android.pdfviewer.views.PdfView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * This fragment has a big {@ImageView} that shows PDF pages, and 2
 * {@link android.widget.Button}s to move between pages. We use a
 * {@link android.graphics.pdf.PdfRenderer} to render PDF pages as
 * {@link android.graphics.Bitmap}s.
 */
public class PdfRendererBasicFragment extends Fragment implements PdfView {

    /**
     * Key string for saving the state of current page index.
     */
    private static final String STATE_CURRENT_PAGE_INDEX = "current_page_index";

    /**
     * {@link android.widget.ImageView} that shows a PDF page as a {@link android.graphics.Bitmap}
     */
    private ImageView mViewer;

    private PdfViewerPresenter pdfViewerPresenter;

    public PdfRendererBasicFragment() {
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

        int mPageIndex = 0;
        // If there is a savedInstanceState (screen orientations, etc.), we restore the page index.
        if (null != savedInstanceState) {
            mPageIndex = savedInstanceState.getInt(STATE_CURRENT_PAGE_INDEX, 0);
        }
        // create instance of View Presenter
        pdfViewerPresenter = new PdfViewerPresenter();
        pdfViewerPresenter.setView(this, file, mPageIndex);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int mPageIndex = 0;
        // If there is a savedInstanceState (screen orientations, etc.), we restore the page index.
        if (null != savedInstanceState) {
            mPageIndex = savedInstanceState.getInt(STATE_CURRENT_PAGE_INDEX, 0);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        pdfViewerPresenter.renderFile();
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
        if (null != pdfViewerPresenter) {
            outState.putInt(STATE_CURRENT_PAGE_INDEX, pdfViewerPresenter.getPageIndex());
        }
    }



    @Override
    public void renderTitle(String title) {
        getActivity().setTitle(title);
    }

    @Override
    public void renderPage(Bitmap bitmap) {
        // We are ready to show the Bitmap to user.
        mViewer.setImageBitmap(bitmap);
    }

    @Override
    public void onNextPage() {
        pdfViewerPresenter.showNextPage();
    }

    @Override
    public void onPreviousPage() {
        pdfViewerPresenter.showPreviousPage();
    }

    @Override
    public Context context() {
        return getContext();
    }
}
