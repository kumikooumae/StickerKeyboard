package kumiko.stickerkeyboard;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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
        return new PackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackViewHolder holder, int position) {
        StickerPack pack = packs.get(position);
        holder.title.setText(pack.getName());
    }

    @Override
    public int getItemCount() {
        return packs.size();
    }
}
