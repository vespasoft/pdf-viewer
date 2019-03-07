package com.vespasoft.android.pdfviewer.views;

import android.content.Context;
import android.content.res.Resources;

public interface PdfViewer {

    void renderViewPager(int pages);

    void renderTitle(String title);

    Context context();

    Resources resources();

}
