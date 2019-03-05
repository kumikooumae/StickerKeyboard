package kumiko.stickerkeyboard;

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
import kumiko.stickerkeyboard.data.Sticker;
import kumiko.stickerkeyboard.data.StickerPack;

class StickerPackListAdapter extends RecyclerView.Adapter<StickerPackListAdapter.PackViewHolder> {

    private List<StickerPack> packs;

    static class PackViewHolder extends RecyclerView.ViewHolder {

        ImageView cover;

        TextView title;

        TextView text;

        PackViewHolder(View view) {
            super(view);
            cover = view.findViewById(R.id.pack_card_cover);
            title = view.findViewById(R.id.pack_card_title);
            text = view.findViewById(R.id.pack_card_text);
        }
    }

    StickerPackListAdapter(List<StickerPack> packs) {
        this.packs = packs;
    }

    @NonNull
    @Override
    public PackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_pack_card, parent, false);
        final PackViewHolder holder = new PackViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackActivity.startPackActivity(view.getContext(), packs.get(holder.getAdapterPosition()));
            }
        });
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

    void update(List<StickerPack> packs) {
        this.packs = packs;
        notifyDataSetChanged();
    }
}
