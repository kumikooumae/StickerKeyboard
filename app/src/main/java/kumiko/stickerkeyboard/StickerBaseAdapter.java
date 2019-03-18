package kumiko.stickerkeyboard;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;
import kumiko.stickerkeyboard.data.Sticker;

abstract class StickerBaseAdapter extends RecyclerView.Adapter {

    List<Sticker> stickers;

    static class StickerViewHolder extends RecyclerView.ViewHolder {
        ImageView sticker;

        StickerViewHolder(ImageView view) {
            super(view);
            sticker = view;
        }
    }

    StickerBaseAdapter(List<Sticker> stickers) {
        this.stickers = stickers;
    }

    StickerViewHolder createStickerViewHolder(@NonNull ViewGroup parent) {
        ImageView sticker = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_item, parent, false);
        return new StickerViewHolder(sticker);
    }

    void loadSticker(@NonNull ImageView view, Sticker sticker) {
        Context context = view.getContext();
        Glide.with(context)
                .load(FileHelper.getStickerFile(context, sticker))
                .apply(RequestOptions.fitCenterTransform())
                .error(Glide.with(context).load(android.R.drawable.stat_notify_error))
                .into(view);
    }

    abstract Sticker getSticker(int onBindPosition);
}
