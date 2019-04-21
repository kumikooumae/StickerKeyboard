package kumiko.stickerkeyboard.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = AuthorContact.TABLE_NAME,
        foreignKeys = @ForeignKey(
                entity = Author.class,
                parentColumns = Author.ID,
                childColumns = AuthorContact.AUTHOR,
                onUpdate = CASCADE,
                onDelete = CASCADE))
class AuthorContact {

    static final String TABLE_NAME = "author_contact";

    static final String ID = "id";

    static final String AUTHOR = "author";

    static final String CONTACT = "contact";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;

    @ColumnInfo(name = AUTHOR)
    private final long author;

    @ColumnInfo(name = CONTACT)
    private final String contact;

    AuthorContact(long author, String contact) {
        this.author = author;
        this.contact = contact;
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

    String getContact() {
        return contact;
    }
}
