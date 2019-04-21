package kumiko.stickerkeyboard;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import kumiko.stickerkeyboard.data.Database;
import kumiko.stickerkeyboard.data.Sticker;
import kumiko.stickerkeyboard.data.StickerPack;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

public class InfoActivity extends AppCompatActivity {

    private int packPosition;

    private int stickerPosition;

    private boolean type;

    private static final String EXTRA_TYPE = "kumiko.stickerkeyboard.extra.TYPE";

    private static final boolean TYPE_PACK = true;

    private static final boolean TYPE_STICKER = false;

    private static final String EXTRA_PACK_POSITION = "kumiko.stickerkeyboard.extra.PACK_POSITION";

    private static final String EXTRA_STICKER_POSITION = "kumiko.stickerkeyboard.extra.STICKER_POSITION";

    static void startInfoActivity(Activity activity, int packPosition) {
        Intent intent = new Intent(activity, InfoActivity.class);
        intent.putExtra(EXTRA_TYPE, TYPE_PACK);
        intent.putExtra(EXTRA_PACK_POSITION, packPosition);
        activity.startActivity(intent);
    }

    public static void startInfoActivity(Activity activity, int packPosition, int stickerPosition) {
        Intent intent = new Intent(activity, InfoActivity.class);
        intent.putExtra(EXTRA_TYPE, TYPE_STICKER);
        intent.putExtra(EXTRA_PACK_POSITION, packPosition);
        intent.putExtra(EXTRA_STICKER_POSITION, stickerPosition);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        type = getIntent().getBooleanExtra(EXTRA_TYPE, TYPE_PACK);
        packPosition = getIntent().getIntExtra(EXTRA_PACK_POSITION, -1);
        stickerPosition = getIntent().getIntExtra(EXTRA_STICKER_POSITION, -1);

        Database db = Database.getInstance(this);
        LoadInfoTask loadInfoTask = null;
        if (type == TYPE_STICKER) {
            loadInfoTask = new LoadInfoTask(db.getAllStickerPacks().get(packPosition).getStickers().get(stickerPosition));
        } else {
            loadInfoTask = new LoadInfoTask(db.getAllStickerPacks().get(packPosition));
        }
        LoadInfoTask.setListener((descriptions, authors, tags) -> {
            TextView descriptionList = findViewById(R.id.description_list);
            descriptionList.setText(descriptions);
            TextView authorList = findViewById(R.id.author_list);
            authorList.setText(authors);
            TextView tagList = findViewById(R.id.tag_list);
            tagList.setText(tags);
        });
        loadInfoTask.execute();


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Info");
    }

    static class LoadInfoTask extends AsyncTask<Void, Void, Void> {

        @Nullable private static Listener listener;

        @Nullable private Sticker sticker;

        @Nullable private StickerPack stickerPack;

        private boolean type;

        private String descriptions;

        private String authors;

        private String tags;

        LoadInfoTask(@NonNull Sticker sticker) {
            this.sticker = sticker;
            this.type = TYPE_STICKER;
        }

        LoadInfoTask(@NonNull StickerPack stickerPack) {
            this.stickerPack = stickerPack;
            this.type = TYPE_PACK;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Database db = Database.getInstance(MyApplication.getAppContext());
            if (type == InfoActivity.TYPE_PACK) {
                descriptions = db.getDescriptions(stickerPack);
                authors = db.getAuthors(stickerPack);
                tags = db.getTags(stickerPack);
            } else {
                descriptions = db.getDescriptions(sticker);
                authors = db.getAuthors(sticker);
                tags = db.getTags(sticker);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (listener != null) {
                listener.fillText(descriptions, authors, tags);
            }
        }

        interface Listener {
            void fillText(String descriptions, String authors, String tags);
        }

        static void setListener(@Nullable Listener listener) {
            LoadInfoTask.listener = listener;
        }
    }

    @Override
    protected void onDestroy() {
        LoadInfoTask.setListener(null);
        super.onDestroy();
    }
}
