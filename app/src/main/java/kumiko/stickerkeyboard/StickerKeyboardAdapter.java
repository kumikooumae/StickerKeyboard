package kumiko.stickerkeyboard;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import kumiko.stickerkeyboard.data.Sticker;

class StickerKeyboardAdapter extends StickerBaseAdapter {

    private IMEService service;

    StickerKeyboardAdapter(List<Sticker> stickers, IMEService service) {
        super(stickers, service);
        this.service = service;
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final StickerViewHolder holder = super.onCreateViewHolder(parent, viewType);
        holder.sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (service != null) {
                    service.sendSticker(stickers.get(holder.getAdapterPosition()));
                }
            }
        });
        holder.sticker.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Pop up
                return false;
            }
        });
        return holder;
    }
}
