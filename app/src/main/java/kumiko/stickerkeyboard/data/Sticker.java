package kumiko.stickerkeyboard.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
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
public class Sticker implements Parcelable {

    static final String TABLE_NAME = "stickers";

    static final String ID = "id";

    private static final String FILE_NAME = "file_name";

    static final String PACK_ID = "pack_id";

    private static final String POSITION = "position";

    private static final String TYPE = "type";

    public enum Type {
        JPEG, PNG, GIF, WEBP, UNKNOWN
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    public int id;

    @ColumnInfo(name = FILE_NAME)
    public String fileName;

    @ColumnInfo(name = PACK_ID)
    public int packId;

    @ColumnInfo(name = POSITION)
    public int position;

    @ColumnInfo(name = TYPE)
    @TypeConverters(Sticker.class)
    public Type type;

    public Sticker(String fileName, int packId, Type type) {
        this.fileName = fileName;
        this.packId = packId;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public int getPackId() {
        return packId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Type getType() {
        return type;
    }

    private Sticker(Parcel in) {
        id = in.readInt();
        fileName = in.readString();
        packId = in.readInt();
        position = in.readInt();
        type = (Type) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(fileName);
        out.writeInt(packId);
        out.writeInt(position);
        out.writeSerializable(type);
    }

    public static final Parcelable.Creator<Sticker> CREATOR = new Parcelable.Creator<Sticker>() {
        @Override
        public Sticker createFromParcel(Parcel in) {
            return new Sticker(in);
        }

        @Override
        public Sticker[] newArray(int size) {
            return new Sticker[size];
        }
    };

    @TypeConverter
    public static Type fromOrdinal(int ordinal) {
        return Type.values()[ordinal];
    }

    @TypeConverter
    public static int ordinalFromType(Type type) {
        return type.ordinal();
    }
}
