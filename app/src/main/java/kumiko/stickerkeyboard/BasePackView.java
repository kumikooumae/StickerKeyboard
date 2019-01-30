package kumiko.stickerkeyboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class BasePackView extends RecyclerView {

    private int stickerSize;

    private GridLayoutManager manager;

    public BasePackView(@NonNull Context context) {
        super(context);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        init(context);
    }

    public BasePackView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BasePackView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        stickerSize = getResources().getDimensionPixelSize(R.dimen.sticker_size);

        manager = new GridLayoutManager(context, 1);
        setLayoutManager(manager);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (stickerSize > 0) {
            manager.setSpanCount(Math.max(1, getMeasuredWidth() / stickerSize));
        }
    }
}
