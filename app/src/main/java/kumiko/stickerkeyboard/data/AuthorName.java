package kumiko.stickerkeyboard.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = AuthorName.TABLE_NAME,
        foreignKeys = @ForeignKey(
                entity = Author.class,
                parentColumns = Author.ID,
                childColumns = AuthorName.AUTHOR,
                onUpdate = CASCADE,
                onDelete = CASCADE))
class AuthorName {

    static final String TABLE_NAME = "author_name";

    static final String ID = "id";

    static final String AUTHOR = "author";

    static final String NAME = "name";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;

    @ColumnInfo(name = AUTHOR)
    private final long author;

    @ColumnInfo(name = NAME)
    private final String name;

    AuthorName(long author, String name) {
        this.author = author;
        this.name = name;
    }

    long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    long getAuthor() {
        return author;
    }

    String getName() {
        return name;
    }
}
