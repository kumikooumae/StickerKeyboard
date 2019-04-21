package kumiko.stickerkeyboard.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = PackTag.TABLE_NAME,
        foreignKeys = {
            @ForeignKey(
                entity = StickerPack.class,
                parentColumns = StickerPack.ID,
                childColumns = PackTag.PACK,
                onDelete = CASCADE,
                onUpdate = CASCADE),
            @ForeignKey(
                entity = Tag.class,
                parentColumns = Tag.ID,
                childColumns = PackTag.TAG,
                onDelete = CASCADE,
                onUpdate = CASCADE)})
class PackTag {

    static final String TABLE_NAME = "pack_tag";

    static final String ID = "id";

    static final String PACK = "pack";

    static final String TAG = "tag";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;

    @ColumnInfo(name = PACK)
    private final long pack;

    @ColumnInfo(name = TAG)
    private final long tag;

    PackTag(long pack, long tag) {
        this.pack = pack;
        this.tag = tag;
    }

    long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    long getPack() {
        return pack;
    }

    long getTag() {
        return tag;
    }
}
