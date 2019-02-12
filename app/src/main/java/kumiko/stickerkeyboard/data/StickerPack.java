package kumiko.stickerkeyboard.data;

import java.util.ArrayList;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Fts4
@Entity(tableName = StickerPack.TABLE_NAME)
public class StickerPack {

    static final String TABLE_NAME = "stickerPacks";

    static final String ROWID = "rowid";

    private static final String NAME = "name";

    private static final String SIZE = "size";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ROWID)
    public int id;

    @ColumnInfo(name = NAME)
    public String name;

    @Ignore
    public ArrayList<Sticker> stickers;

    public StickerPack(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Sticker> getStickers() {
        return stickers;
    }

    public void setStickers(ArrayList<Sticker> stickers) {
        this.stickers = stickers;
    }
}
