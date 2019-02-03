package kumiko.stickerkeyboard;

import android.app.Service;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.os.IBinder;
import android.view.View;

import kumiko.stickerkeyboard.data.Sticker;

public class IMEService extends InputMethodService {
    @Override
    public View onCreateInputView() {
        return new StickerKeyboardView(this);
    }

    void sendSticker(Sticker sticker) {

    }
}
