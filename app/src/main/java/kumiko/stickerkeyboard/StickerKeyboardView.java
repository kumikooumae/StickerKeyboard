package kumiko.stickerkeyboard;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import kumiko.stickerkeyboard.data.Database;
import kumiko.stickerkeyboard.data.Sticker;
import kumiko.stickerkeyboard.data.StickerPack;

public class StickerKeyboardView extends FrameLayout {

    private IMEService service;

    private LayoutInflater inflater;

    private Database db;

    private List<PackView> packViews = new ArrayList<>();

    private StickerKeyboardAdapter historyAdapter;

    public StickerKeyboardView(@NonNull Context context) {
        super(context);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        service = (IMEService) context;
        inflater = LayoutInflater.from(context);
        db = Database.getInstance(context);
        inflater.inflate(R.layout.image_keyboard, this);

        loadPacks();

        ImageButton imeSwitcher = findViewById(R.id.ime_switcher);
        final InputMethodManager inputMethodManager = (InputMethodManager) service.getSystemService(Context.INPUT_METHOD_SERVICE);
        imeSwitcher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputMethodManager != null) {
                    inputMethodManager.getLastInputMethodSubtype();
                }
            }
        });
        imeSwitcher.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (inputMethodManager != null) {
                    inputMethodManager.showInputMethodPicker();
                }
                return true;
            }
        });
    }

    private void loadPacks() {
        PackView historyView = new PackView(service);
        historyAdapter = new StickerKeyboardAdapter(db.getHistoryStickersReversed(), getResources().getString(R.string.history_pack_name));
        historyView.setAdapter(historyAdapter);
        packViews.add(historyView);

        List<StickerPack> packs = db.getAllStickerPacks();
        List<Sticker> covers = new ArrayList<>();
        for (StickerPack pack: packs) {
            List<Sticker> stickers = db.getStickers(pack);
            PackView packView = new PackView(service);
            packView.setAdapter(new StickerKeyboardAdapter(stickers, pack.name));
            packViews.add(packView);
            covers.add(stickers.isEmpty() ? null : stickers.get(0));
        }

        ViewPager packsPager = findViewById(R.id.packs_pager);
        packsPager.setAdapter(new StickerPackPagerAdapter(packViews));
        TabLayout packsTab = findViewById(R.id.packs_tab);
        packsTab.setupWithViewPager(packsPager);
        packsTab.setTabMode(TabLayout.MODE_SCROLLABLE);

        for (int i = 0; i < packsTab.getTabCount(); i++) {
            ImageView tabItemView = (ImageView) inflater.inflate(R.layout.tab_item, new LinearLayout(service), false);
            if (i == 0) {
                tabItemView.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_recent_history));
            } else {
                Sticker cover = covers.get(i-1);
                Glide.with(service)
                        .load((cover != null) ? FileHelper.getStickerFile(service, cover) : null)
                        .into(tabItemView);
            }
            TabLayout.Tab packTab = packsTab.getTabAt(i);
            if (packTab != null) {
                packTab.setCustomView(tabItemView);
            }
        }
    }

    void refreshHistory(Sticker sticker) {
        db.refreshHistory(sticker);
        historyAdapter.update(db.getHistoryStickersReversed());
    }
}
