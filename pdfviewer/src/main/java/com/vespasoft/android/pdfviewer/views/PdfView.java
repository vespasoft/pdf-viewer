package com.vespasoft.android.pdfviewer.views;

import android.content.Context;
import android.graphics.Bitmap;

public interface PdfView {

    void renderPage(Bitmap bitmap);

    void renderTitle (String title);

    void onNextPage();

    void onPreviousPage();

    Context context();

}
