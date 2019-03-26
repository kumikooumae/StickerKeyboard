package kumiko.stickerkeyboard;

import android.content.BroadcastReceiver;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.inputmethodservice.InputMethodService;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import java.io.File;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import kumiko.stickerkeyboard.data.Sticker;

public class IMEService extends InputMethodService {

    private static final String AUTHORITY = "kumiko.stickerkeyboard";

    private static final String ACTION_PACKS_LIST_UPDATED = "kumiko.stickerkeyboard.ACTION_PACKS_LIST_UPDATED";

    private static final String ACTION_STICKER_PACK_UPDATED = "kumiko.stickerkeyboard.ACTION_STICKER_PACK_UPDATED";

    static final String EXTRA_PACK_POSITION = "kumiko.stickerkeyboard.extra.PACK_POSITION";

    private static final String TAG = "IMEService";

    private StickerKeyboardView stickerKeyboardView;

    private LocalBroadcastManager localBroadcastManager;

    private StickerUpdateReceiver stickerUpdateReceiver;

    @Override
    public View onCreateInputView() {
        stickerKeyboardView = new StickerKeyboardView(this);
        return stickerKeyboardView;
    }

    void sendSticker(Sticker sticker) {
        File stickerFile = FileHelper.getStickerFile(this, sticker);
        doCommitContent(stickerFile, FileHelper.getMime(sticker.getType()));
        stickerKeyboardView.refreshHistory(sticker);
    }

    private void doCommitContent(@NonNull File file, @NonNull String mime) {
        final EditorInfo editorInfo = getCurrentInputEditorInfo();
        final Uri contentUri = FileProvider.getUriForFile(this, AUTHORITY, file);
        final int flag;
        if (Build.VERSION.SDK_INT >= 25) {
            flag = InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION;
        } else {
            flag = 0;
            try {
                grantUriPermission(editorInfo.packageName, contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (Exception e) {
                Log.e(TAG, "grantUriPermission failed packageName=" + editorInfo.packageName + " contentUri=" + contentUri, e);
            }
        }
        final InputContentInfoCompat inputContentInfoCompat = new InputContentInfoCompat(
                contentUri,
                new ClipDescription(file.getName(), new String[]{mime}), null);
        InputConnectionCompat.commitContent(
                getCurrentInputConnection(),
                getCurrentInputEditorInfo(),
                inputContentInfoCompat, flag, null);
    }

    public static void notifyPacksListUpdated(Context context) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.sendBroadcast(new Intent(ACTION_PACKS_LIST_UPDATED));
    }

    public static void notifyStickerPackUpdated(Context context, int position) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        Intent intent = new Intent(ACTION_PACKS_LIST_UPDATED);
        intent.putExtra(EXTRA_PACK_POSITION, position);
        localBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        this.setTheme(R.style.AppTheme);    // Theme must be set before onCreate
        super.onCreate();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_PACKS_LIST_UPDATED);
        intentFilter.addAction(ACTION_STICKER_PACK_UPDATED);
        stickerUpdateReceiver = new StickerUpdateReceiver();
        localBroadcastManager.registerReceiver(stickerUpdateReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(stickerUpdateReceiver);
        super.onDestroy();
    }

    class StickerUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (stickerKeyboardView != null) {
                if (ACTION_PACKS_LIST_UPDATED.equals(intent.getAction())) {
                    stickerKeyboardView.loadPacks();
                } else if (ACTION_STICKER_PACK_UPDATED.equals(intent.getAction())) {
                    stickerKeyboardView.refreshPack(intent.getIntExtra(EXTRA_PACK_POSITION, 0));
                }
            }
        }
    }
}
