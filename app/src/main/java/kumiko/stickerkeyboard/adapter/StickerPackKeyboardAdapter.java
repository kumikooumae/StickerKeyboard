package kumiko.stickerkeyboard.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import java.util.Objects;

import kumiko.stickerkeyboard.IMEService;
import kumiko.stickerkeyboard.R;
import kumiko.stickerkeyboard.data.Sticker;

public class StickerPackKeyboardAdapter extends StickerPackBaseAdapter {

    public static final int VIEW_TYPE_HEADER = 1;

    private static final int VIEW_TYPE_STICKER = 0;

    private final String packName;

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        final TextView packTitle;

        HeaderViewHolder(@NonNull TextView view) {
            super(view);
            packTitle = view;
        }
    }

    public StickerPackKeyboardAdapter(@NonNull List<Sticker> stickers, @NonNull String packName) {
        super(stickers);
        this.packName = packName;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            TextView packTitle = Objects.requireNonNull((TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_view_header, parent, false));
            return new HeaderViewHolder(packTitle);
        } else {    // VIEW_TYPE_STICKER
            final StickerViewHolder holder = createStickerViewHolder(parent);
            if (parent.getContext() instanceof IMEService) {
                final IMEService service = (IMEService) parent.getContext();
                holder.sticker.setOnClickListener(view -> service.sendSticker(stickers.get(holder.getAdapterPosition() - 1)));
                holder.sticker.setOnLongClickListener(view -> {
                    // Pop up
                    return false;
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

    @NonNull
    @Override
    Sticker getSticker(int onBindPosition) {
        return stickers.get(onBindPosition - 1);
    }
}
