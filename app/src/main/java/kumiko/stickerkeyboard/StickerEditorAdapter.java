package kumiko.stickerkeyboard;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import kumiko.stickerkeyboard.data.Sticker;

class StickerEditorAdapter extends StickerBaseAdapter {

    StickerEditorAdapter(List<Sticker> stickers) {
        super(stickers);
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final StickerViewHolder holder = createStickerViewHolder(parent);
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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        loadSticker(((StickerViewHolder) holder).sticker, getSticker(position));
    }

    @Override
    public int getItemCount() {
        return stickers.size();
    }

    @Override
    Sticker getSticker(int onBindPosition) {
        return stickers.get(onBindPosition);
    }
}
