package kumiko.stickerkeyboard.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = Sticker.TABLE_NAME,
        foreignKeys = @ForeignKey(
            entity = StickerPack.class,
            parentColumns = StickerPack.ID,
            childColumns = Sticker.PACK_ID,
            onDelete = CASCADE,
            onUpdate = CASCADE),
        indices = {@Index(Sticker.PACK_ID)})
public class Sticker {

    static final String TABLE_NAME = "stickers";

    static final String ID = "id";

    static final String FILE_NAME = "file_name";

    static final String PACK_ID = "pack_id";

    static final String POSITION = "position";

    static final String TYPE = "type";

    public enum Type {
        JPEG, PNG, GIF, WEBP, UNKNOWN
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;

    @NonNull
    @ColumnInfo(name = FILE_NAME)
    private String fileName;

    @ColumnInfo(name = PACK_ID)
    private final long packId;

    @ColumnInfo(name = POSITION)
    private int position;

    @NonNull
    @ColumnInfo(name = TYPE)
    @TypeConverters(Sticker.class)
    private final Type type;

    @Ignore
    @Nullable
    private History history;

    Sticker(@NonNull String fileName, long packId, @NonNull Type type) {
        this.fileName = fileName;
        this.packId = packId;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    @NonNull
    String getFileName() {
        return fileName;
    }

    public long getPackId() {
        return packId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @NonNull
    public Type getType() {
        return type;
    }

    @Nullable
    History getHistory() {
        return history;
    }

    void setHistory(@Nullable History history) {
        this.history = history;
    }

    @TypeConverter
    public static Type fromOrdinal(int ordinal) {
        return Type.values()[ordinal];
    }

    @TypeConverter
    public static int ordinalFromType(Type type) {
        return type.ordinal();
    }
}
