package kumiko.stickerkeyboard.data;

import java.util.ArrayList;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = Tag.TABLE_NAME)
class Tag {

    static final String TABLE_NAME = "tag";

    static final String ID = "id";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;

    long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }
}
