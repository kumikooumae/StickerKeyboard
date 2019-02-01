package kumiko.stickerkeyboard.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

@Fts4
@Entity
public class StickerPack {

    @PrimaryKey
    @ColumnInfo(name = "rowid")
    public int id;

    public String name;
}
