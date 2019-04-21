package kumiko.stickerkeyboard.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = PackDescription.TABLE_NAME,
        foreignKeys = @ForeignKey(
                entity = StickerPack.class,
                parentColumns = StickerPack.ID,
                childColumns = PackDescription.PACK,
                onDelete = CASCADE,
                onUpdate = CASCADE))
class PackDescription {

    static final String TABLE_NAME = "pack_description";

    static final String ID = "id";

    static final String PACK = "pack";

    static final String DESCRIPTION = "description";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;

    @ColumnInfo(name = PACK)
    private final long pack;

    @ColumnInfo(name = DESCRIPTION)
    private final String description;

    PackDescription(long pack, String description) {
        this.pack = pack;
        this.description = description;
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

    String getDescription() {
        return description;
    }
}
