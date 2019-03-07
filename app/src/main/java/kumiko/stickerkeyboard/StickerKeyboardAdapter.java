package kumiko.stickerkeyboard;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import kumiko.stickerkeyboard.data.Sticker;

class StickerKeyboardAdapter extends StickerBaseAdapter {

    private static final int VIEW_TYPE_HEADER = 0;

    private static final int VIEW_TYPE_STICKER = 1;

    private String packName;

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView packTitle;

        HeaderViewHolder(TextView view) {
            super(view);
            packTitle = view;
        }
    }

    StickerKeyboardAdapter(List<Sticker> stickers, String packName) {
        super(stickers);
        this.packName = packName;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            TextView packTitle = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_view_header, parent, false);
            return new HeaderViewHolder(packTitle);
        } else {    // VIEW_TYPE_STICKER
            final StickerViewHolder holder = createStickerViewHolder(parent);
            if (parent.getContext() instanceof IMEService) {
                final IMEService service = (IMEService) parent.getContext();
                holder.sticker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        service.sendSticker(stickers.get(holder.getAdapterPosition() - 1));
                    }
                });
                holder.sticker.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        // Pop up
                        return false;
                    }
                });
            }
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER:
                ((HeaderViewHolder) holder).packTitle.setText(packName);
                break;
            case VIEW_TYPE_STICKER:
                loadSticker(((StickerViewHolder) holder).sticker, getSticker(position));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_STICKER;
        }
    }

    @Override
    public int getItemCount() {
        return stickers.size() + 1;
    }

    @Override
    Sticker getSticker(int onBindPosition) {
        return stickers.get(onBindPosition - 1);
    }
}
