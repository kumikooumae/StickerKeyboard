package kumiko.stickerkeyboard;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import kumiko.stickerkeyboard.adapter.StickerPackEditorAdapter;
import kumiko.stickerkeyboard.data.Database;
import kumiko.stickerkeyboard.data.Sticker;
import kumiko.stickerkeyboard.data.StickerPack;
import kumiko.stickerkeyboard.view.PackView;

public class PackActivity extends AppCompatActivity {

    private static final String EXTRA_PACK = "kumiko.stickerkeyboard.extra.PACK";

    private static final int DOC_REQUEST_CODE = 1;

    private static StickerPack pack;

    private static StickerPackEditorAdapter adapter;

    private SwipeRefreshLayout refreshLayout;

    public static void startPackActivity(Context context, @NonNull StickerPack pack) {
        if (context instanceof Activity) {
            Intent intent = new Intent(context, PackActivity.class);
            intent.putExtra(EXTRA_PACK, pack);
            ((Activity) context).startActivityForResult(intent, MainActivity.STICKER_PACK_UPDATED_REQUEST_CODE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pack = getIntent().getParcelableExtra(EXTRA_PACK);

        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setEnabled(false);
        SaveStickersTask.setListener(getRefreshIndicatorListener());

        PackView packView = findViewById(R.id.pack_view);
        adapter = new StickerPackEditorAdapter(pack.getStickers());
        packView.setAdapter(adapter);
        FloatingActionButton addStickerFab = findViewById(R.id.addStickerFab);
        addStickerFab.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType(FileHelper.MIME_IMAGE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, DOC_REQUEST_CODE);
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(pack.getName());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == DOC_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            List<Uri> uris = new ArrayList<>();
            if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    uris.add(clipData.getItemAt(i).getUri());
                }
            } else {
                uris.add(data.getData());
            }
            new SaveStickersTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, uris.toArray(new Uri[0]));
            setResult(RESULT_OK);
        }
    }

    private static class SaveStickersTask extends AsyncTask<Uri, Void, Void> {

        @Nullable private static Listener listener;

        @Override
        protected void onPreExecute() {
            if (listener != null) {
                listener.showIndicator();
            }
        }

        @Override
        protected Void doInBackground(@NonNull Uri... uris) {
            Context context = MyApplication.getAppContext();
            Database db = Database.getInstance(context);
            for (Uri uri: uris) {
                String mimeType = context.getContentResolver().getType(uri);
                // TODO: you can pass in StickerPack to db.addNewSticker() so that no need to traverse
                Sticker sticker = db.addNewSticker(pack.getId(), FileHelper.getStickerType(mimeType));
                FileHelper.saveStickerFrom(uri, sticker);
                pack.getStickers().add(sticker);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (listener != null) {
                listener.hideIndicator();
                adapter.notifyItemInserted(pack.getStickers().size() - 1);
            }
        }

        static void setListener(@Nullable Listener listener) {
            SaveStickersTask.listener = listener;
        }

        interface Listener {
            void showIndicator();

            void hideIndicator();
        }
    }

    private SaveStickersTask.Listener getRefreshIndicatorListener() {
        return new SaveStickersTask.Listener() {
            @Override
            public void showIndicator() {
                refreshLayout.setRefreshing(true);
            }

            @Override
            public void hideIndicator() {
                refreshLayout.setRefreshing(false);
            }
        };
    }

    @Override
    protected void onDestroy() {
        SaveStickersTask.setListener(null);
        super.onDestroy();
    }
}
