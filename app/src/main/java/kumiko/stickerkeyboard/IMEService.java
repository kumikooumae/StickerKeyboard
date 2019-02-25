package kumiko.stickerkeyboard;

import android.content.ClipDescription;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v13.view.inputmethod.InputConnectionCompat;
import android.support.v13.view.inputmethod.InputContentInfoCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import java.io.File;
import kumiko.stickerkeyboard.data.Database;
import kumiko.stickerkeyboard.data.Sticker;

public class IMEService extends InputMethodService {

    private static final String AUTHORITY = "kumiko.stickerkeyboard";

    private static final String TAG = "IMEService";

    private Database db;

    private StickerKeyboardView stickerKeyboardView;

    @Override
    public void onCreate() {
        super.onCreate();
        db = Database.getInstance(this);
    }

    @Override
    public View onCreateInputView() {
        stickerKeyboardView = new StickerKeyboardView(this);
        return stickerKeyboardView;
    }

    void sendSticker(Sticker sticker) {
        File stickerFile = FileHelper.getStickerFile(this, sticker);
        switch (sticker.getType()) {
            case JPEG:
                doCommitContent(stickerFile, FileHelper.MIME_JPEG);
                break;
            case PNG:
                doCommitContent(stickerFile, FileHelper.MIME_PNG);
                break;
            case GIF:
                doCommitContent(stickerFile, FileHelper.MIME_GIF);
                break;
            case WEBP:
                doCommitContent(stickerFile, FileHelper.MIME_WEBP);
                break;
        }
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
}
