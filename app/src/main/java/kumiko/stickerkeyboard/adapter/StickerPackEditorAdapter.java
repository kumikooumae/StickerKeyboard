package kumiko.stickerkeyboard.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import kumiko.stickerkeyboard.IMEService;
import kumiko.stickerkeyboard.InfoActivity;
import kumiko.stickerkeyboard.MyApplication;
import kumiko.stickerkeyboard.R;
import kumiko.stickerkeyboard.data.Database;
import kumiko.stickerkeyboard.data.Sticker;
import kumiko.stickerkeyboard.view.StickerKeyboardView;

public class StickerPackEditorAdapter extends StickerPackBaseAdapter {

    private int packPosition;

    public StickerPackEditorAdapter(@NonNull List<Sticker> stickers, int packPosition) {
        super(stickers);
        this.packPosition = packPosition;
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final StickerViewHolder holder = createStickerViewHolder(parent);
        holder.sticker.setOnClickListener(view -> {
            // Pop up, share
            if (parent.getContext() instanceof Activity) {
                InfoActivity.startInfoActivity((Activity) parent.getContext(), packPosition, holder.getAdapterPosition());
            }
        });
        holder.sticker.setOnLongClickListener(view -> {
            // Edit
            AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
            builder.setTitle(R.string.confirm_delete_sticker);
            builder.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                int stickerPosition = holder.getAdapterPosition();
                new Thread(() -> {
                    Database db = Database.getInstance(parent.getContext());
                    db.removeSticker(packPosition, stickerPosition);
                    IMEService.notifyStickerPackUpdated(parent.getContext(), packPosition);
                }).start();
                notifyItemRemoved(stickerPosition);
            });
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.show();
            return true;
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        loadSticker(((StickerViewHolder) holder).sticker, getSticker(position));
    }

    @Override
    public int getItemCount() {
        return stickers.size();
    }

    @NonNull
    @Override
    Sticker getSticker(int onBindPosition) {
        return stickers.get(onBindPosition);
    }
}
