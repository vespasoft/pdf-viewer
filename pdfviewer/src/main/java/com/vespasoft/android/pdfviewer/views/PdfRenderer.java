package com.vespasoft.android.pdfviewer.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

public interface PdfRenderer {

    void renderPage(Bitmap bitmap);

    void renderTitle(String title);

    void onNextPage();

    void onPreviousPage();

    void onShowPage(int index);

    Context context();

    Resources resources();

}
