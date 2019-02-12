package kumiko.stickerkeyboard;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;
import kumiko.stickerkeyboard.data.Sticker;
import kumiko.stickerkeyboard.data.StickerPack;

class StickerPackListAdapter extends RecyclerView.Adapter<StickerPackListAdapter.PackViewHolder> {

    private ArrayList<StickerPack> packs;

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

    StickerPackListAdapter(ArrayList<StickerPack> packs) {
        this.packs = packs;
    }

    @NonNull
    @Override
    public PackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_pack_card, parent, false);
        final PackViewHolder holder = new PackViewHolder(view);
        final StickerPack pack = packs.get(holder.getAdapterPosition());
        final Context context = view.getContext();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PackActivity.class);
                intent.putExtra(context.getResources().getString(R.string.pack_name_extra_key), pack.getName());
                intent.putParcelableArrayListExtra(context.getResources().getString(R.string.stickers_extra_key), pack.getStickers());
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PackViewHolder holder, int position) {
        StickerPack pack = packs.get(position);
        Context context = holder.cover.getContext();
        ArrayList<Sticker> stickers = pack.getStickers();
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

    void update(ArrayList<StickerPack> packs) {
        this.packs = packs;
        notifyDataSetChanged();
    }
}
