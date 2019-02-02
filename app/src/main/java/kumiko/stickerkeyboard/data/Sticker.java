package kumiko.stickerkeyboard.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Fts4
@Entity(tableName = Sticker.TABLE_NAME,
        foreignKeys = @ForeignKey(
            entity = StickerPack.class,
            parentColumns = StickerPack.ROWID,
            childColumns = Sticker.PACK_ID,
            onDelete = CASCADE,
            onUpdate = CASCADE
        ))
public class Sticker {

    static final String TABLE_NAME = "stickers";

    static final String ROWID = "rowid";

    private static final String FILE_NAME = "file_name";

    static final String PACK_ID = "pack_id";

    private static final String POSITION = "position";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ROWID)
    public int id;

    @ColumnInfo(name = FILE_NAME)
    public String fileName;

    @ColumnInfo(name = PACK_ID)
    public int packId;

    @ColumnInfo(name = POSITION)
    public int position;

    public Sticker(String fileName, int packId, int position) {
        this.fileName = fileName;
        this.packId = packId;
        this.position = position;
    }
}
