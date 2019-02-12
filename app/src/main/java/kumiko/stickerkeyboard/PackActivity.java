package kumiko.stickerkeyboard;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import java.util.ArrayList;
import kumiko.stickerkeyboard.data.Sticker;

public class PackActivity extends AppCompatActivity {

    ArrayList<Sticker> stickers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        stickers = getIntent().getParcelableArrayListExtra(getResources().getString(R.string.stickers_extra_key));

        PackView packView = findViewById(R.id.pack_view);
        packView.setAdapter(new StickerEditorAdapter(stickers));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getIntent().getStringExtra(getResources().getString(R.string.pack_name_extra_key)));
        }
    }
}
