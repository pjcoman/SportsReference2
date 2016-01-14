package comapps.com.sportsreference2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by me on 1/13/2016.
 */
public class LinksDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "sportsreflinks.db";
    private static final int DATABASE_VERSION = 1;

    public LinksDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getInformation(LinksDatabase mydb, String sport) {

        SQLiteDatabase SQ = mydb.getReadableDatabase();
        String[] columns = {"name", "link"};
        Cursor CR = SQ.query(sport, columns, null, null, null, null, null);
        return CR;
    }
}