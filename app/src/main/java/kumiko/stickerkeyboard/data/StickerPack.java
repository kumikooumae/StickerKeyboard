package kumiko.stickerkeyboard.data;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = StickerPack.TABLE_NAME)
public class StickerPack {

    static final String TABLE_NAME = "stickerPacks";

    static final String ID = "id";

    private static final String NAME = "name";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;

    @NonNull
    @ColumnInfo(name = NAME)
    private String name;

    @NonNull
    @Ignore
    public List<Sticker> stickers;

    public StickerPack(@NonNull String name) {
        this.name = name;
        this.stickers = new ArrayList<>();
    }

    long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public List<Sticker> getStickers() {
        return stickers;
    }

    public void setStickers(@NonNull List<Sticker> stickers) {
        this.stickers = stickers;
    }
}
