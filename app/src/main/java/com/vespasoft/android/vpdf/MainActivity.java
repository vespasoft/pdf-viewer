package com.vespasoft.android.vpdf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vespasoft.android.pdfviewer.activities.PdfViewerActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    /**
     * The filename of the PDF.
     */
    private static final String FILENAME = "sample.pdf";

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In this sample, we read a PDF from the assets directory.
                File file = new File(getApplicationContext().getCacheDir(), FILENAME);
                launch(getApplicationContext(), Uri.fromFile(file).getPath());
            }
        });

    }

    public static void launch(Context ctx, String filePath) {

        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(ctx, PdfViewerActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        ctx.startActivity(intent);

    }
}
