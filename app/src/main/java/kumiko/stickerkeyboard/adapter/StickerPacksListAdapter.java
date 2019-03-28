package kumiko.stickerkeyboard.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;
import java.util.Objects;

import kumiko.stickerkeyboard.FileHelper;
import kumiko.stickerkeyboard.PackActivity;
import kumiko.stickerkeyboard.R;
import kumiko.stickerkeyboard.data.Sticker;
import kumiko.stickerkeyboard.data.StickerPack;

public class StickerPacksListAdapter extends RecyclerView.Adapter<StickerPacksListAdapter.PackViewHolder> {

    private final List<StickerPack> packs;

    static class PackViewHolder extends RecyclerView.ViewHolder {

        final ImageView cover;

        final TextView title;

        final TextView text;

        PackViewHolder(@NonNull View view) {
            super(view);
            cover = view.findViewById(R.id.pack_card_cover);
            title = view.findViewById(R.id.pack_card_title);
            text = view.findViewById(R.id.pack_card_text);
        }
    }

    public StickerPacksListAdapter(@NonNull List<StickerPack> packs) {
        this.packs = packs;
    }

    @NonNull
    @Override
    public PackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardView = Objects.requireNonNull(LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_pack_card, parent, false));
        final PackViewHolder holder = new PackViewHolder(cardView);
        cardView.setOnClickListener(view -> PackActivity.startPackActivity(view.getContext(), packs.get(holder.getAdapterPosition())));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PackViewHolder holder, int position) {
        StickerPack pack = packs.get(position);
        Context context = holder.cover.getContext();
        List<Sticker> stickers = pack.getStickers();
        if (!stickers.isEmpty()) {
            Glide.with(context)
                    .load(FileHelper.getStickerFile(context, pack.getStickers().get(0)))
                    .apply(RequestOptions.fitCenterTransform())
                    .error(Glide.with(context).load(android.R.drawable.stat_notify_error))
                    .into(holder.cover);
        }
        holder.title.setText(pack.getName());
    }

    @Override
    public int getItemCount() {
        return packs.size();
    }
}
