package com.vespasoft.android.pdfviewer.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;

import com.vespasoft.android.pdfviewer.R;
import com.vespasoft.android.pdfviewer.views.PdfView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class PdfViewerPresenter {

    /**
     * The filename of the PDF.
     */
    private static final String FILENAME = "sample.pdf";

    /**
     * File descriptor of the PDF.
     */
    private ParcelFileDescriptor mFileDescriptor;

    /**
     * {@link android.graphics.pdf.PdfRenderer} to render the PDF.
     */
    private PdfRenderer mPdfRenderer;

    /**
     * Page that is currently shown on the screen.
     */
    private PdfRenderer.Page mCurrentPage;

    /**
     * PDF page index
     */
    private int mPageIndex;

    private File mFile;

    private String mTitle;

    private WeakReference<PdfView> view;

    public PdfViewerPresenter() {
    }

    public void setView(PdfView pdfView, File file, Integer index){
        this.view = new WeakReference<>(pdfView);
        mFile = file;
        mPageIndex = index;
        mTitle = view.get().context().getString(R.string.app_name_with_index);
    }

    public void renderFile() {
        try {
            openRenderer(mFile);
            showPage(mPageIndex);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(view.get().context(), "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Shows the previous page of PDF to the screen.
     */
    public void showPreviousPage() {
        if (getPageIndex() > 0) showPage(getPageIndex() - 1);
    }

    /**
     * Shows the next page of PDF to the screen.
     */
    public void showNextPage() {
        showPage(getPageIndex() + 1);
    }

    public void onClosePage() {
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
    }

    /**
     * Closes the {@link android.graphics.pdf.PdfRenderer} and related resources.
     *
     * @throws java.io.IOException When the PDF file cannot be closed.
     */
    public void onDestroy() throws IOException {
        onClosePage();
        mPdfRenderer.close();
        mFileDescriptor.close();
    }

    public PdfRenderer.Page getCurrentPage() {
        return mCurrentPage;
    }

    public int getPageIndex() {
        return mPageIndex;
    }

    /**
     * Sets up a {@link android.graphics.pdf.PdfRenderer} and related resources.
     */
    private void openRenderer(File file) throws IOException {
        if (!file.exists()) {
            Uri uri = Uri.fromFile(file);
            // Since PdfRenderer cannot handle the compressed asset file directly, we copy it into
            // the cache directory.
            // InputStream asset = view.get().context().getAssets().open(FILENAME);
            InputStream asset = view.get().context().getContentResolver().openInputStream(uri);
            FileOutputStream output = new FileOutputStream(file);
            final byte[] buffer = new byte[1024];
            int size;
            while ((size = asset.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }
            asset.close();
            output.close();
        }
        mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        // This is the PdfRenderer we use to render the PDF.
        if (mFileDescriptor != null) {
            mPdfRenderer = new PdfRenderer(mFileDescriptor);
        }
    }

    /**
     * Shows the specified page of PDF to the screen.
     *
     * @param index The page index.
     */
    private void showPage(int index) {
        mPageIndex = index;
        if (getPageCount() <= index) {
            return;
        }
        // Make sure to close the current page before opening another one.
        onClosePage();

        // Use `openPage` to open a specific page in PDF.
        mCurrentPage = mPdfRenderer.openPage(index);
        // Important: the destination bitmap must be ARGB (not RGB).
        Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(), mCurrentPage.getHeight(),
                Bitmap.Config.ARGB_8888);
        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get
        // the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        // We are ready to show the Bitmap to user.
        view.get().renderPage(bitmap);
        renderTitle();
    }

    private void renderTitle() {
        mTitle = mFile.getName() + " (%1$d/%2$d)";
        mTitle = mTitle.replace("%1$d", String.valueOf(getPageIndex() + 1));
        mTitle = mTitle.replace("%2$d", String.valueOf(getPageCount()));
        view.get().renderTitle(mTitle);
    }

    private int getPageCount() {
        return mPdfRenderer.getPageCount();
    }

}
