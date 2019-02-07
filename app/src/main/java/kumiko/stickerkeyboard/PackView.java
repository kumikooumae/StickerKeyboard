package kumiko.stickerkeyboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class PackView extends RecyclerView {

    private int stickerSize;

    private GridLayoutManager manager;

    public PackView(@NonNull Context context) {
        super(context);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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
