package kumiko.stickerkeyboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import kumiko.stickerkeyboard.data.Sticker;

class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.StickerViewHolder> {

    private List<Sticker> stickers;

    private Context context;

    private IMEService service;

    static class StickerViewHolder extends RecyclerView.ViewHolder {
        ImageView sticker;

        StickerViewHolder(ImageView view) {
            super(view);
            sticker = view;
        }
    }

    StickerAdapter(List<Sticker> stickers, Context context) {
        this.stickers = stickers;
        this.context = context;
        if (context instanceof IMEService) {
            this.service = (IMEService) context;
        }
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView sticker = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_item, parent, false);
        return new StickerViewHolder(sticker);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder holder, int position) {
        final Sticker sticker = stickers.get(position);

        Glide.with(context)
                .load(FileHelper.getStickerFile(context, sticker))
                .apply(RequestOptions.fitCenterTransform())
                .error(Glide.with(context).load(android.R.drawable.stat_notify_error))
                .into(holder.sticker);

        holder.sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (service != null) {
                    service.sendSticker(sticker);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return stickers.size();
    }

    void update(List<Sticker> stickers) {
        this.stickers = stickers;
        notifyDataSetChanged();
    }
}
