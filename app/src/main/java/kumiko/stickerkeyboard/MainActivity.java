package kumiko.stickerkeyboard;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;
import kumiko.stickerkeyboard.data.Database;
import kumiko.stickerkeyboard.data.StickerPack;

public class MainActivity extends AppCompatActivity {

    private LoadPacksTask loadPacksTask;

    private static StickerPackListAdapter stickerPackListAdapter;

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
        addPackFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getResources().getString(R.string.add_pack));
                final EditText packNameField = new EditText(MainActivity.this);
                packNameField.setHint(getResources().getString(R.string.pack_name));
                builder.setView(packNameField);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new AddNewEmptyPackTask().execute(packNameField.getText().toString());
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.show();
            }
        });
    }

    private static class LoadPacksTask extends AsyncTask<Void, Void, ArrayList<StickerPack>> {

        private Listener listener;

        @Override
        protected ArrayList<StickerPack> doInBackground(Void... voids) {
            return Database.getInstance(MyApplication.getAppContext()).getAllStickerPacks();
        }

        @Override
        protected void onPostExecute(ArrayList<StickerPack> packs) {
            if (listener != null) {
                listener.onFinish(packs);
            }
        }

        void setListener(Listener listener) {
            this.listener = listener;
        }

        interface Listener {
            void onFinish(ArrayList<StickerPack> packs);
        }
    }

    private LoadPacksTask.Listener getOnLoadedPacksListener() {
        return new LoadPacksTask.Listener() {
            @Override
            public void onFinish(ArrayList<StickerPack> packs) {
                RecyclerView packList = findViewById(R.id.pack_list);
                stickerPackListAdapter = new StickerPackListAdapter(packs);
                packList.setAdapter(stickerPackListAdapter);
            }
        };
    }

    private static class AddNewEmptyPackTask extends AsyncTask<String, Void, ArrayList<StickerPack>> {

        @Override
        protected ArrayList<StickerPack> doInBackground(String... strings) {
            return Database.getInstance(MyApplication.getAppContext()).addNewEmptyPack(strings[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<StickerPack> packs) {
            stickerPackListAdapter.update(packs);
        }
    }

    @Override
    protected void onDestroy() {
        loadPacksTask.setListener(null);
        super.onDestroy();
    }
}
