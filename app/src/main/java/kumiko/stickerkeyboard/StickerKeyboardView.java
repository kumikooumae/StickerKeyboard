package kumiko.stickerkeyboard;

import android.content.Context;
import android.os.AsyncTask;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import kumiko.stickerkeyboard.data.Database;
import kumiko.stickerkeyboard.data.Sticker;
import kumiko.stickerkeyboard.data.StickerPack;

public class StickerKeyboardView extends FrameLayout {

    private IMEService service;

    private LayoutInflater inflater;

    private static StickerKeyboardAdapter historyAdapter;

    private GetStickerPacksTask getStickerPacksTask;

    private RefreshHistoryTask refreshHistoryTask;

    public StickerKeyboardView(@NonNull Context context) {
        super(context);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        service = (IMEService) context;
        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.image_keyboard, this);

        getStickerPacksTask = new GetStickerPacksTask();
        getStickerPacksTask.setListener(getOnLoadedStickerPacksListener());
        getStickerPacksTask.execute();

        refreshHistoryTask = new RefreshHistoryTask();

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

    private static class GetStickerPacksTask extends AsyncTask<Void, Void, List<StickerPack>> {

        private Listener listener;

        @Override
        protected List<StickerPack> doInBackground(Void... params) {
            Context appContext = MyApplication.getAppContext();
            Database db = Database.getInstance(appContext);
            List<StickerPack> packs = db.getAllStickerPacks();
            StickerPack historyPack = new StickerPack(appContext.getResources().getString(R.string.history_pack_name));
            historyPack.setStickers(db.getHistoryStickersReversed());
            packs.add(0, historyPack);
            return packs;
        }

        @Override
        protected void onPostExecute(List<StickerPack> packs) {
            if (listener != null) {
                listener.onFinish(packs);
            }
        }

        void setListener(Listener listener) {
            this.listener = listener;
        }

        interface Listener {
            void onFinish(List<StickerPack> packs);
        }
    }

    private GetStickerPacksTask.Listener getOnLoadedStickerPacksListener() {
        return new GetStickerPacksTask.Listener() {
            @Override
            public void onFinish(List<StickerPack> packs) {
                List<PackView> packViews = new ArrayList<>();
                for (StickerPack pack: packs) {
                    PackView packView = new PackView(service);
                    packView.setAdapter(new StickerKeyboardAdapter(pack.getStickers(), pack.getName()));
                    packViews.add(packView);
                }
                historyAdapter = (StickerKeyboardAdapter) packViews.get(0).getAdapter();

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
                        Sticker cover = packs.get(i).getStickers().get(0);
                        Glide.with(service)
                                .load((cover != null) ? FileHelper.getStickerFile(service, cover) : null)
                                .apply(RequestOptions.fitCenterTransform())
                                .error(Glide.with(service).load(android.R.drawable.stat_notify_error))
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
            if (historyAdapter != null) {
                historyAdapter.update(historyStickers);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        getStickerPacksTask.setListener(null);
        super.onDetachedFromWindow();
    }
}
