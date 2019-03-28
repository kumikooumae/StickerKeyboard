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
import java.util.Objects;

import kumiko.stickerkeyboard.data.Sticker;

abstract class StickerBaseAdapter extends RecyclerView.Adapter {

    List<Sticker> stickers;

    static class StickerViewHolder extends RecyclerView.ViewHolder {
        ImageView sticker;

        StickerViewHolder(@NonNull ImageView view) {
            super(view);
            sticker = view;
        }
    }

    StickerBaseAdapter(@NonNull List<Sticker> stickers) {
        this.stickers = stickers;
    }

    @NonNull
    StickerViewHolder createStickerViewHolder(@NonNull ViewGroup parent) {
        ImageView sticker = Objects.requireNonNull((ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_item, parent, false));
        return new StickerViewHolder(sticker);
    }

    void loadSticker(@NonNull ImageView view, @NonNull Sticker sticker) {
        Context context = view.getContext();
        Glide.with(context)
                .load(FileHelper.getStickerFile(context, sticker))
                .apply(RequestOptions.fitCenterTransform())
                .error(Glide.with(context).load(android.R.drawable.stat_notify_error))
                .into(view);
    }

    @NonNull
    abstract Sticker getSticker(int onBindPosition);
}
