package kumiko.stickerkeyboard;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import java.util.ArrayList;
import kumiko.stickerkeyboard.data.StickerPack;

public class PackActivity extends AppCompatActivity {

    private static final String MIME_IMAGE = "image/*";

    private static final int DOC_REQUEST_CODE = 1;

    private StickerPack pack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pack = getIntent().getParcelableExtra(getResources().getString(R.string.pack_extra_key));

        PackView packView = findViewById(R.id.pack_view);
        packView.setAdapter(new StickerEditorAdapter(pack.getStickers()));
        FloatingActionButton addStickerFab = findViewById(R.id.addStickerFab);
        addStickerFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType(MIME_IMAGE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, DOC_REQUEST_CODE);
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(pack.getName());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == DOC_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    FileHelper.saveStickerFrom(clipData.getItemAt(i).getUri(), pack.getId());
                }
            } else {
                FileHelper.saveStickerFrom(data.getData(), pack.getId());
            }
        }
    }
}
