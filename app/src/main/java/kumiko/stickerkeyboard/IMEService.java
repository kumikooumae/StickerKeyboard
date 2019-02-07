package kumiko.stickerkeyboard;

import android.inputmethodservice.InputMethodService;
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
