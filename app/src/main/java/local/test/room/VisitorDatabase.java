package local.test.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Visitor.class}, version = 1)
public abstract class VisitorDatabase extends RoomDatabase {

    private static VisitorDatabase instance;

    // used to access visitordao database operations
    public abstract VisitorDao visitorDao();

    // instantiate if null otherwise return
    public static synchronized VisitorDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    VisitorDatabase.class, "visitor_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack).build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

}
