package kumiko.stickerkeyboard;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

public class PackView extends RecyclerView {

    private int stickerSize;

    private GridLayoutManager manager;

    public PackView(@NonNull Context context) {
        super(context);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        init(context);
    }

    public PackView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PackView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        stickerSize = getResources().getDimensionPixelSize(R.dimen.sticker_size);

        manager = new GridLayoutManager(context, 1);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Adapter adapter = getAdapter();
                if (adapter instanceof StickerKeyboardAdapter && adapter.getItemViewType(position) == StickerKeyboardAdapter.VIEW_TYPE_HEADER) {
                    return manager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
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
