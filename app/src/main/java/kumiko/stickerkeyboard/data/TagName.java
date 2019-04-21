package kumiko.stickerkeyboard.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = TagName.TABLE_NAME,
    foreignKeys = @ForeignKey(
            entity = Tag.class,
            parentColumns = Tag.ID,
            childColumns = TagName.TAG,
            onUpdate = CASCADE,
            onDelete = CASCADE))
class TagName {

    static final String TABLE_NAME = "tag_name";

    static final String ID = "id";

    static final String TAG = "tag";

    static final String NAME = "name";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;

    @ColumnInfo(name = TAG)
    private final long tag;

    @ColumnInfo(name = NAME)
    private final String name;

    TagName(long tag, String name) {
        this.tag = tag;
        this.name = name;
    }

    long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    long getTag() {
        return tag;
    }

    String getName() {
        return name;
    }
}
