package kumiko.stickerkeyboard;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import kumiko.stickerkeyboard.data.Sticker;

class FileHelper {

    static final String MIME_IMAGE = "image/*";

    static final String MIME_JPEG = "image/jpeg";

    static final String MIME_PNG = "image/png";

    static final String MIME_GIF = "image/gif";

    static final String MIME_WEBP = "image/webp";

    static final String EXT_JPEG = ".jpg";

    static final String EXT_PNG = ".png";

    static final String EXT_GIF = ".gif";

    static final String EXT_WEBP = ".webp";

    private static final String TAG = "FileHelper";

    static File getStickerFile(@NonNull Context context, @NonNull Sticker sticker) {
        return new File(new File(context.getFilesDir(), Integer.toString(sticker.getPackId())), getStickerFileName(sticker));
    }

    static void saveStickerFrom(Uri uri, int packId) {

    }

    static void deleteSticker(@NonNull Context context, @NonNull Sticker sticker) {
        File file = new File(new File(context.getFilesDir(), Integer.toString(sticker.getPackId())), getStickerFileName(sticker));
        if (!file.delete()) {
            Log.d(TAG, "deleteSticker: Failed to delete " + getStickerFileName(sticker));
        }
    }

    private static String getStickerFileName(@NonNull Sticker sticker) {
        String ext;
        switch (sticker.getType()) {
            case JPEG:
                ext = EXT_JPEG;
                break;
            case PNG:
                ext = EXT_PNG;
                break;
            case GIF:
                ext = EXT_GIF;
                break;
            case WEBP:
                ext = EXT_WEBP;
                break;
            default:    // case UNKNOWN
                ext = "";
                break;
        }
        return Integer.toString(sticker.getId()) + ext;
    }

    static String getMime(Sticker.Type type) {
        switch (type) {
            case JPEG:
                return MIME_JPEG;
            case PNG:
                return MIME_PNG;
            case GIF:
                return MIME_GIF;
            case WEBP:
                return MIME_WEBP;
            default:    // case UNKNOWN
                return "";
        }
    }

    static Sticker.Type getStickerType(String mimeType) {
        if (MIME_JPEG.equals(mimeType)) {
            return Sticker.Type.JPEG;
        } else if (MIME_PNG.equals(mimeType)) {
            return Sticker.Type.PNG;
        } else if (MIME_GIF.equals(mimeType)) {
            return Sticker.Type.GIF;
        } else if (MIME_WEBP.equals(mimeType)) {
            return Sticker.Type.WEBP;
        } else {
            return Sticker.Type.UNKNOWN;
        }
    }
}
