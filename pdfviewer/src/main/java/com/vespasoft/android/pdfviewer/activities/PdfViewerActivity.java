package com.vespasoft.android.pdfviewer.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.vespasoft.android.pdfviewer.R;
import com.vespasoft.android.pdfviewer.fragments.PdfRendererBasicFragment;
import com.vespasoft.android.pdfviewer.views.PdfView;

public class PdfViewerActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String FRAGMENT_PDF_RENDERER_BASIC = "pdf_renderer_basic";

    /**
     * {@link android.widget.Button} to move to the previous page.
     */
    private Button mButtonPrevious;

    /**
     * {@link android.widget.Button} to move to the next page.
     */
    private Button mButtonNext;

    /**
     * {@link PdfRendererBasicFragment} content a fragment instance of the pdf viewer
     */
    private PdfRendererBasicFragment mPdfRendererFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        mButtonPrevious = findViewById(R.id.previous);
        mButtonNext = findViewById(R.id.next);
        mButtonPrevious.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);

        Uri uri = getIntent().getData();

        if (savedInstanceState == null) {
            mPdfRendererFragment = new PdfRendererBasicFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mPdfRendererFragment,
                            FRAGMENT_PDF_RENDERER_BASIC)
                    .commit();
        } else {
            mPdfRendererFragment = (PdfRendererBasicFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.previous) {
            mPdfRendererFragment.onPreviousPage();
        } else if (i == R.id.next) {
            mPdfRendererFragment.onNextPage();
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
        if (i == R.id.action_info) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.intro_message)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
