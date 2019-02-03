package kumiko.stickerkeyboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import kumiko.stickerkeyboard.data.Sticker;

class StickerEditorAdapter extends StickerBaseAdapter {
    StickerEditorAdapter(List<Sticker> stickers, Context context) {
        super(stickers, context);
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final StickerViewHolder holder = super.onCreateViewHolder(parent, viewType);
        holder.sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pop up, share
            }
        });
        holder.sticker.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Edit
                return false;
            }
        });
        return holder;
    }
}
