package kumiko.stickerkeyboard.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Fts4
@Entity(foreignKeys = @ForeignKey(
        entity = StickerPack.class,
        parentColumns = "rowid",
        childColumns = "packId",
        onDelete = CASCADE,
        onUpdate = CASCADE))
public class Sticker {

    @PrimaryKey
    @ColumnInfo(name = "rowid")
    public int id;

    public String fileName;

    public int packId;
}
