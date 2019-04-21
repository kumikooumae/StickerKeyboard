package kumiko.stickerkeyboard.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import kumiko.stickerkeyboard.PackActivity;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = PackAuthor.TABLE_NAME,
        foreignKeys = {
                @ForeignKey(
                        entity = StickerPack.class,
                        parentColumns = StickerPack.ID,
                        childColumns = PackAuthor.PACK,
                        onDelete = CASCADE,
                        onUpdate = CASCADE),
                @ForeignKey(
                        entity = Author.class,
                        parentColumns = Author.ID,
                        childColumns = PackAuthor.AUTHOR,
                        onDelete = CASCADE,
                        onUpdate = CASCADE)})
class PackAuthor {

    static final String TABLE_NAME = "pack_author";

    static final String ID = "id";

    static final String PACK = "pack";

    static final String AUTHOR = "author";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;

    @ColumnInfo(name = PACK)
    private final long pack;

    @ColumnInfo(name = AUTHOR)
    private final long author;

    PackAuthor(long pack, long author) {
        this.pack = pack;
        this.author = author;
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

    long getAuthor() {
        return author;
    }
}
