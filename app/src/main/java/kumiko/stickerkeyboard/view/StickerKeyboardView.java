package kumiko.stickerkeyboard.view;

import android.content.Context;
import android.os.AsyncTask;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import kumiko.stickerkeyboard.FileHelper;
import kumiko.stickerkeyboard.IMEService;
import kumiko.stickerkeyboard.MyApplication;
import kumiko.stickerkeyboard.R;
import kumiko.stickerkeyboard.adapter.StickerPackKeyboardAdapter;
import kumiko.stickerkeyboard.adapter.StickerPacksPagerAdapter;
import kumiko.stickerkeyboard.data.Database;
import kumiko.stickerkeyboard.data.Sticker;
import kumiko.stickerkeyboard.data.StickerPack;

public class StickerKeyboardView extends FrameLayout {

    private final IMEService service;

    private final LayoutInflater inflater;

    private List<StickerPackKeyboardAdapter> stickerAdapters;

    private static StickerPackKeyboardAdapter historyAdapter;

    private GetStickerPacksTask getStickerPacksTask;

    public StickerKeyboardView(@NonNull Context context) {
        super(context);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        service = (IMEService) context;
        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.image_keyboard, this);

        loadPacks();

        ImageButton imeSwitcher = findViewById(R.id.ime_switcher);
        final InputMethodManager inputMethodManager = (InputMethodManager) service.getSystemService(Context.INPUT_METHOD_SERVICE);
        imeSwitcher.setOnClickListener(v -> {
            final IBinder token = service.getWindow().getWindow().getAttributes().token;
            if (token != null && inputMethodManager != null) {
                inputMethodManager.switchToLastInputMethod(token);
            }
        });
        imeSwitcher.setOnLongClickListener(v -> {
            if (inputMethodManager != null) {
                inputMethodManager.showInputMethodPicker();
            }
            return true;
        });
    }

    public void refreshHistory(@NonNull Sticker sticker) {
        new RefreshHistoryTask().execute(sticker);
    }

    public void loadPacks() {
        getStickerPacksTask = new GetStickerPacksTask();
        getStickerPacksTask.setListener(getOnLoadedStickerPacksListener());
        getStickerPacksTask.execute();
    }

    public void refreshPack(int position) {
        stickerAdapters.get(position+1).notifyDataSetChanged();
    }

    private static class GetStickerPacksTask extends AsyncTask<Void, Void, Void> {

        @Nullable private Listener listener;

        private List<StickerPack> packs;

        private StickerPack historyPack;

        @Override
        protected Void doInBackground(Void... params) {
            Context appContext = MyApplication.getAppContext();
            Database db = Database.getInstance(appContext);
            packs = db.getAllStickerPacks();
            historyPack = new StickerPack(appContext.getResources().getString(R.string.history_pack_name));
            historyPack.setStickers(db.getHistoryStickersReversed());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (listener != null) {
                listener.onFinish(packs, historyPack);
            }
        }

        void setListener(@Nullable Listener listener) {
            this.listener = listener;
        }

        interface Listener {
            void onFinish(@NonNull List<StickerPack> packs, @NonNull StickerPack historyPack);
        }
    }

    @NonNull
    private GetStickerPacksTask.Listener getOnLoadedStickerPacksListener() {
        return (packs, historyPack) -> {
            historyAdapter = new StickerPackKeyboardAdapter(historyPack.getStickers(), historyPack.getName());
            stickerAdapters = new ArrayList<>();
            stickerAdapters.add(historyAdapter);
            for (StickerPack pack: packs) {
                stickerAdapters.add(new StickerPackKeyboardAdapter(pack.getStickers(), pack.getName()));
            }

            ViewPager packsPager = findViewById(R.id.packs_pager);
            packsPager.setAdapter(new StickerPacksPagerAdapter(stickerAdapters));
            TabLayout packsTab = findViewById(R.id.packs_tab);
            packsTab.setupWithViewPager(packsPager);
            packsTab.setTabMode(TabLayout.MODE_SCROLLABLE);

            for (int i = 0; i < packsTab.getTabCount(); i++) {
                ImageView tabItemView = (ImageView) inflater.inflate(R.layout.tab_item, new LinearLayout(service), false);
                if (i == 0) {
                    tabItemView.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_recent_history));
                } else if (!packs.get(i-1).getStickers().isEmpty()) {
                    Sticker cover = packs.get(i-1).getStickers().get(0);
                    if (cover != null) {
                        Glide.with(service)
                                .load(FileHelper.getStickerFile(service, cover))
                                .apply(RequestOptions.fitCenterTransform())
                                .error(Glide.with(service).load(android.R.drawable.stat_notify_error))
                                .into(tabItemView);
                    }

                }
                TabLayout.Tab packTab = packsTab.getTabAt(i);
                if (packTab != null) {
                    packTab.setCustomView(tabItemView);
                }
            }
        };
    }

    private static class RefreshHistoryTask extends AsyncTask<Sticker, Void, Void> {

        private final Database db;

        RefreshHistoryTask() {
            super();
            db = Database.getInstance(MyApplication.getAppContext());
        }

        @Override
        protected Void doInBackground(@NonNull Sticker... stickers) {
            db.refreshHistory(Objects.requireNonNull(stickers[0]));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (historyAdapter != null) {
                historyAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        getStickerPacksTask.setListener(null);
        super.onDetachedFromWindow();
    }
}
