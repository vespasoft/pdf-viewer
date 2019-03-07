package com.vespasoft.android.pdfviewer.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.vespasoft.android.pdfviewer.R;
import com.vespasoft.android.pdfviewer.adapter.DocumentPageAdapter;
import com.vespasoft.android.pdfviewer.presenter.PdfViewerPresenter;
import com.vespasoft.android.pdfviewer.views.PdfViewer;

import java.io.File;

public class PdfViewerActivity extends AppCompatActivity implements PdfViewer, View.OnClickListener {

    public static final String FRAGMENT_PDF_RENDERER_BASIC = "pdf_renderer_basic";

    /**
     * {@link Button} to move to the previous page.
     */
    private Button mButtonPrevious;

    /**
     * {@link Button} to move to the next page.
     */
    private Button mButtonNext;

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    /**
     * {@link PdfViewerPresenter} content a PDFViewer Presenter instance
     */
    private PdfViewerPresenter pdfViewerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        mButtonPrevious = findViewById(R.id.previous);
        mButtonNext = findViewById(R.id.next);
        mButtonPrevious.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);

        setUpToolbar();

        Uri uri = getIntent().getData();

        // create instance of View Presenter
        pdfViewerPresenter = new PdfViewerPresenter();
        pdfViewerPresenter.setView(this, new File(uri.getPath()), 0);

    }

    @Override
    protected void onStart() {
        super.onStart();
        pdfViewerPresenter.renderFile();
    }

    @Override
    public void renderViewPager(int pages) {
        mViewPager = findViewById(R.id.vpPager);
        mPagerAdapter = new DocumentPageAdapter(getSupportFragmentManager(), pages);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setClipToPadding(true);
        //mViewPager.setPageTransformer(true, new BookFlipPageTransformer());
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.previous) {
            mViewPager.arrowScroll(View.FOCUS_LEFT);
        } else if (i == R.id.next) {
            mViewPager.arrowScroll(View.FOCUS_RIGHT);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            goToBack();
            return true;
        } else if (i == R.id.action_info) {
            showAlertInfo();
            return true;
        } else if (i == R.id.search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void renderTitle(String title) {
        setTitle(title);
    }

    @Override
    public Context context() {
        return this;
    }

    @Override
    public Resources resources() {
        return getResources();
    }

    public void showAlertInfo() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.intro_message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    public void goToBack() {
        onBackPressed();
    }
}
