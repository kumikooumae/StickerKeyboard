package kumiko.stickerkeyboard.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Fts4
@Entity(tableName = StickerPack.TABLE_NAME)
public class StickerPack implements Parcelable {

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

    private StickerPack(Parcel in) {
        id = in.readInt();
        name = in.readString();
        stickers = in.createTypedArrayList(Sticker.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeTypedList(stickers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StickerPack> CREATOR = new Creator<StickerPack>() {
        @Override
        public StickerPack createFromParcel(Parcel in) {
            return new StickerPack(in);
        }

        @Override
        public StickerPack[] newArray(int size) {
            return new StickerPack[size];
        }
    };

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
