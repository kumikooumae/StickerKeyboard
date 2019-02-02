package kumiko.stickerkeyboard.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
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

    @ColumnInfo(name = SIZE)
    public int size;

    public StickerPack(String name, int size) {
        this.name = name;
        this.size = size;
    }
}
