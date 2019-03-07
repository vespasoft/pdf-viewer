package com.vespasoft.android.pdfviewer.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

public interface PdfViewer {

    void renderViewPager(int pages);

    void renderTitle(String title);

    void onShowPage(int index);

    Context context();

    Resources resources();

}
