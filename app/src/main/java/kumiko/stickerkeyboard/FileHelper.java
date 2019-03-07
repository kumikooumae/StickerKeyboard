package kumiko.stickerkeyboard;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import kumiko.stickerkeyboard.data.Sticker;

public class FileHelper {

    static final String MIME_IMAGE = "image/*";

    static final String MIME_JPEG = "image/jpeg";

    static final String MIME_PNG = "image/png";

    static final String MIME_GIF = "image/gif";

    static final String MIME_WEBP = "image/webp";

    static final String EXT_JPEG = ".jpg";

    static final String EXT_PNG = ".png";

    static final String EXT_GIF = ".gif";

    static final String EXT_WEBP = ".webp";

    private static final String STICKER_DIR = "stickers";

    private static final String TAG = "FileHelper";

    static File getStickerFile(@NonNull Context context, @NonNull Sticker sticker) {
        return new File(new File(new File(context.getFilesDir(), STICKER_DIR), Integer.toString(sticker.getPackId())), getStickerFileName(sticker));
    }

    /**
     * Save sticker from uri. Could be time consuming.
     *
     * @param uri Sticker Uri
     * @param sticker Sticker object
     */
    static synchronized void saveStickerFrom(Uri uri, Sticker sticker) {
        // TODO: 2019/2/28 stickerId.ext -> save to disk
        Context context = MyApplication.getAppContext();
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        File destFile = getStickerFile(context, sticker);
        File parentDir = destFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                bufferedInputStream = new BufferedInputStream(inputStream);
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destFile, false));
                byte [] b = new byte[1024];
                while (bufferedInputStream.read(b) != -1) {
                    bufferedOutputStream.write(b);
                }
            } else {
                Log.d(TAG, "saveStickerFrom: Null input stream: " + uri.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Delete sticker by Sticker. Could be time consuming.
     *
     * @param context Context
     * @param sticker Sticker to be deleted
     */
    static synchronized void deleteSticker(@NonNull Context context, @NonNull Sticker sticker) {
        File file = getStickerFile(context, sticker);
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

    static String getMime(@NonNull Sticker.Type type) {
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

    public static Sticker.Type getStickerType(String mimeType) {
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
