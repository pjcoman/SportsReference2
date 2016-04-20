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
    private static final String DATABASE_ADDRESS = "https://api.backendless.com/B40D13D5-E84B-F009-FF57-3871FCA5AE00/v1/files/";
    private static final int DATABASE_VERSION = 1;






    public LinksDatabase(Context context) {


      //  super(context, DATABASE_NAME, context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), null, DATABASE_VERSION);

        super(context, DATABASE_NAME, String.valueOf(context.getExternalFilesDir(null).getAbsolutePath() + "/dbase"), null, DATABASE_VERSION);


    }

    public Cursor getInformation(LinksDatabase mydb, String sport) {

        SQLiteDatabase SQ = mydb.getReadableDatabase();
        String[] columns = {"name", "link"};
        Cursor CR = SQ.query(sport, columns, null, null, null, null, null);
        return CR;
    }
}