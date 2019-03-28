package kumiko.stickerkeyboard.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ViewGroup;
import java.util.List;
import kumiko.stickerkeyboard.data.Sticker;

public class StickerPackEditorAdapter extends StickerPackBaseAdapter {

    public StickerPackEditorAdapter(@NonNull List<Sticker> stickers) {
        super(stickers);
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final StickerViewHolder holder = createStickerViewHolder(parent);
        holder.sticker.setOnClickListener(view -> {
            // Pop up, share
        });
        holder.sticker.setOnLongClickListener(view -> {
            // Edit
            return false;
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

    @NonNull
    @Override
    Sticker getSticker(int onBindPosition) {
        return stickers.get(onBindPosition);
    }
}
