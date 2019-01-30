package kumiko.stickerkeyboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

public class StickerKeyboardView extends FrameLayout {
    public StickerKeyboardView(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        LayoutInflater.from(context).inflate(R.layout.image_keyboard, this);
    }
}
