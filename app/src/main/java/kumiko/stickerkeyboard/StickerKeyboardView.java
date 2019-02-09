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

    private StickerKeyboardAdapter historyAdapter;

    private CreateStickerAdaptersTask createStickerAdaptersTask;

    private RefreshHistoryTask refreshHistoryTask;

    public StickerKeyboardView(@NonNull Context context) {
        super(context);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        service = (IMEService) context;
        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.image_keyboard, this);

        createStickerAdaptersTask = new CreateStickerAdaptersTask();
        createStickerAdaptersTask.setListener(getOnCreatedStickerAdaptersListener());
        createStickerAdaptersTask.execute();

        refreshHistoryTask = new RefreshHistoryTask();
        refreshHistoryTask.setListener(new RefreshHistoryTask.Listener() {
            @Override
            public void onFinish(List<Sticker> historyStickers) {
                historyAdapter.update(historyStickers);
            }
        });

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

    void refreshHistory(Sticker sticker) {
        refreshHistoryTask.execute(sticker);
    }

    private static class CreateStickerAdaptersTask extends AsyncTask<Void, Void, List<StickerKeyboardAdapter>> {

        private Listener listener;

        @Override
        protected List<StickerKeyboardAdapter> doInBackground(Void... params) {
            Database db = Database.getInstance(MyApplication.getAppContext());
            List<StickerKeyboardAdapter> adapters = new ArrayList<>();
            adapters.add(new StickerKeyboardAdapter(db.getHistoryStickersReversed(), MyApplication.getAppContext().getResources().getString(R.string.history_pack_name)));
            List<StickerPack> packs = db.getAllStickerPacks();
            for (StickerPack pack: packs) {
                List<Sticker> stickers = db.getStickers(pack);
                adapters.add(new StickerKeyboardAdapter(stickers, pack.name));
            }
            return adapters;
        }

        @Override
        protected void onPostExecute(List<StickerKeyboardAdapter> stickerKeyboardAdapters) {
            if (listener != null) {
                listener.onFinish(stickerKeyboardAdapters);
            }
        }

        void setListener(Listener listener) {
            this.listener = listener;
        }

        interface Listener {
            void onFinish(List<StickerKeyboardAdapter> stickerKeyboardAdapters);
        }
    }

    private CreateStickerAdaptersTask.Listener getOnCreatedStickerAdaptersListener() {
        return new CreateStickerAdaptersTask.Listener() {
            /**
             * Create views with adapters, and load thumbnail into TabLayout item
             *
             * @param stickerKeyboardAdapters List<StickerKeyboardAdapter> created in CreateStickerAdaptersTask
             */
            @Override
            public void onFinish(List<StickerKeyboardAdapter> stickerKeyboardAdapters) {
                historyAdapter = stickerKeyboardAdapters.get(0);
                List<PackView> packViews = new ArrayList<>();
                List<Sticker> covers = new ArrayList<>();
                for (StickerKeyboardAdapter stickerKeyboardAdapter: stickerKeyboardAdapters) {
                    PackView packView = new PackView(service);
                    packView.setAdapter(stickerKeyboardAdapter);
                    packViews.add(packView);
                    covers.add(stickerKeyboardAdapter.getCover());
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
                        Sticker cover = covers.get(i);
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
        };
    }

    private static class RefreshHistoryTask extends AsyncTask<Sticker, Void, List<Sticker>> {

        private Database db;

        private Listener listener;

        RefreshHistoryTask() {
            super();
            db = Database.getInstance(MyApplication.getAppContext());
        }

        @Override
        protected List<Sticker> doInBackground(Sticker... stickers) {
            db.refreshHistory(stickers[0]);
            return db.getHistoryStickersReversed();
        }

        @Override
        protected void onPostExecute(List<Sticker> historyStickers) {
            if (listener != null) {
                listener.onFinish(historyStickers);
            }
        }

        void setListener(Listener listener) {
            this.listener = listener;
        }

        interface Listener {
            void onFinish(List<Sticker> historyStickers);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        createStickerAdaptersTask.setListener(null);
        refreshHistoryTask.setListener(null);
        super.onDetachedFromWindow();
    }
}
