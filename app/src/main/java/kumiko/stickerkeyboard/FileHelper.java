package kumiko.stickerkeyboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.util.List;

import kumiko.stickerkeyboard.data.Sticker;

class FileHelper {
    private static final String TAG = "FileHelper";

    static File getStickerFile(@NonNull Context context, @NonNull Sticker sticker) {
        return new File(new File(context.getFilesDir(), Integer.toString(sticker.packId)), sticker.fileName);
    }

    static void deleteSticker(@NonNull Context context, @NonNull Sticker sticker) {
        File file = new File(new File(context.getFilesDir(), Integer.toString(sticker.packId)), sticker.fileName);
        if (!file.delete()) {
            Log.d(TAG, "deleteSticker: Failed to delete " + sticker.fileName);
        }
    }

    static void deleteStickers(@NonNull Context context, @NonNull List<Sticker> stickers) {
        for (Sticker sticker: stickers) {
            deleteSticker(context, sticker);
        }
    }
}
