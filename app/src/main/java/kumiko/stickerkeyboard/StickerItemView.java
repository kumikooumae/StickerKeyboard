package kumiko.stickerkeyboard;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

import androidx.annotation.Nullable;

public class StickerItemView extends AppCompatImageView {
    public StickerItemView(Context context) {
        super(context);
    }

    public StickerItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
