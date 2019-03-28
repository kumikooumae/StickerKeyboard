package kumiko.stickerkeyboard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.widget.EditText;
import java.util.List;
import java.util.Objects;

import kumiko.stickerkeyboard.adapter.StickerPacksListAdapter;
import kumiko.stickerkeyboard.data.Database;
import kumiko.stickerkeyboard.data.StickerPack;

public class MainActivity extends AppCompatActivity {

    private static List<StickerPack> packs;

    private LoadPacksTask loadPacksTask;

    private static StickerPacksListAdapter stickerPacksListAdapter;

    static final int STICKER_PACK_UPDATED_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadPacksTask = new LoadPacksTask();
        loadPacksTask.setListener(getOnLoadedPacksListener());
        loadPacksTask.execute();

        FloatingActionButton addPackFab = findViewById(R.id.add_pack_fab);
        addPackFab.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getResources().getString(R.string.add_pack));
            final EditText packNameField = new EditText(MainActivity.this);
            packNameField.setHint(getResources().getString(R.string.pack_name));
            builder.setView(packNameField);
            builder.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> new AddNewEmptyPackTask().execute(packNameField.getText().toString()));
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.show();
        });
    }

    private static class LoadPacksTask extends AsyncTask<Void, Void, Void> {

        private Listener listener;

        @Override
        protected Void doInBackground(Void... voids) {
            packs = Database.getInstance(MyApplication.getAppContext()).getAllStickerPacks();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (listener != null) {
                listener.onFinish();
            }
        }

        void setListener(Listener listener) {
            this.listener = listener;
        }

        interface Listener {
            void onFinish();
        }
    }

    private LoadPacksTask.Listener getOnLoadedPacksListener() {
        return () -> {
            RecyclerView packsList = findViewById(R.id.packs_list);
            packsList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            stickerPacksListAdapter = new StickerPacksListAdapter(packs);
            packsList.setAdapter(stickerPacksListAdapter);
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case STICKER_PACK_UPDATED_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    stickerPacksListAdapter.notifyDataSetChanged();
                }
        }
    }

    private static class AddNewEmptyPackTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(@NonNull String... strings) {
            Database.getInstance(MyApplication.getAppContext()).addNewEmptyPack(Objects.requireNonNull(strings[0]));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            stickerPacksListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        loadPacksTask.setListener(null);
        super.onDestroy();
    }
}
