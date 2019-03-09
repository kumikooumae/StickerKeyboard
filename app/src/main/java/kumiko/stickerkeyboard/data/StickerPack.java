package kumiko.stickerkeyboard.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = StickerPack.TABLE_NAME)
public class StickerPack implements Parcelable {

    static final String TABLE_NAME = "stickerPacks";

    static final String ID = "id";

    private static final String NAME = "name";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    public int id;

    @ColumnInfo(name = NAME)
    public String name;

    @Ignore
    public List<Sticker> stickers;

    public StickerPack(String name) {
        this.name = name;
        this.stickers = new ArrayList<>();
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

    public List<Sticker> getStickers() {
        return stickers;
    }

    public void setStickers(List<Sticker> stickers) {
        this.stickers = stickers;
    }
}
