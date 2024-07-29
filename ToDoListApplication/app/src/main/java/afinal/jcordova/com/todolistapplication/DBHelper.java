package afinal.jcordova.com.todolistapplication;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public final class DBHelper {

    private static final String LOGTAG = "DBHelper";

    private static final String DATABASE_NAME = "todolist.db";
    //private static final int DATABASE_VERSION = 1;
    private static final int DATABASE_VERSION = 2; // Increased from 1 after schema change
    private static final String TABLE_NAME = "tododata";

    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_SHORTDESC = "shortdesc";
    public static final String KEY_DUEDATE = "duedate";
    public static final String KEY_ADDTLINFO = "addtldesc";
    public static final String KEY_COMPLETED = "completed";

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_TITLE = 1;
    public static final int COLUMN_SHORTDESC = 2;
    public static final int COLUMN_DUEDATE = 3;
    public static final int COLUMN_ADDTLINFO = 4;
    public static final int COLUMN_COMPLETED = 5;

    private Context context;
    private SQLiteDatabase db;
    private SQLiteStatement insertStmt;

    private static final String INSERT =
            "INSERT INTO " + TABLE_NAME + "(" + KEY_TITLE + ", " +
                    KEY_SHORTDESC + ", " + KEY_DUEDATE + ", " + KEY_ADDTLINFO + ", " +
                    KEY_COMPLETED + ") values (?, ?, ?, ?, ?);";

    public DBHelper(Context context) throws Exception {
        this.context = context;

        try {
            OpenHelper openHelper = new OpenHelper(this.context);
            db = openHelper.getWritableDatabase();
            insertStmt = db.compileStatement(INSERT);
        } catch (Exception e) {
            Log.e(LOGTAG, "DBHelper constructor: could not get database " + e);
            throw (e);
        }
    }//end of Constructor

    public long insert (ToDoItem todoInfo)
    {
        //insertStmt.clearBindings();

        insertStmt.bindString(COLUMN_TITLE, todoInfo.getTitle());
        insertStmt.bindString(COLUMN_SHORTDESC, todoInfo.getShortdesc());
        insertStmt.bindString(COLUMN_DUEDATE, todoInfo.getDuedate());
        insertStmt.bindString(COLUMN_ADDTLINFO, todoInfo.getAddtlinfo());
        insertStmt.bindLong(COLUMN_COMPLETED, todoInfo.isCompleted());
        long value =-1;
        try
        {
            value = insertStmt.executeInsert();
        }
        catch (Exception e)
        {
            Log.e(LOGTAG, " executeInsert problem: " + e);
        }
        Log.d (LOGTAG, "value="+value);
        return value;
    }



    private static class OpenHelper extends SQLiteOpenHelper {
        private static final String LOGTAG = "OpenHelper";

        private static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY, " +
                        KEY_TITLE + " TEXT, " + KEY_SHORTDESC + " TEXT, " + KEY_DUEDATE + " TEXT, " +
                        KEY_ADDTLINFO + " TEXT, " +
                        KEY_COMPLETED + " INTEGER DEFAULT 0);";


        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }//Constructor

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOGTAG, " onCreate");
            Log.d(LOGTAG, "Creating table with SQL: " + CREATE_TABLE);
            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                Log.e(LOGTAG, " onCreate:  Could not create SQL database: " + e);
            }
        }//end of method

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(LOGTAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will drop tables and recreate.");
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(db);
            } catch (Exception e) {
                Log.e(LOGTAG, " onUpgrade:  Could not update SQL database: " + e);
            }

        }//end of method

    }//end of inner static Class


    public void deleteAll()
    {
        db.delete(TABLE_NAME, null, null);
    }//end of method


    public boolean deleteRecord(long rowId)
    {
        return db.delete(TABLE_NAME, KEY_ID + "=" + rowId, null) > 0;
    }//end of method


    public List<ToDoItem> selectAll()
    {
        List<ToDoItem> list = new ArrayList<ToDoItem>();

        Cursor cursor = db.query(TABLE_NAME,
                new String[] { KEY_ID, KEY_TITLE, KEY_SHORTDESC, KEY_DUEDATE, KEY_ADDTLINFO, KEY_COMPLETED},
                null, null, null, null, null, null);

//        // Get column indices once, outside the loop
//        int idIndex = cursor.getColumnIndex(KEY_ID);
//        int titleIndex = cursor.getColumnIndex(KEY_TITLE);
//        int shortDescIndex = cursor.getColumnIndex(KEY_SHORTDESC);
//        int dueDateIndex = cursor.getColumnIndex(KEY_DUEDATE);
//        int addtlInfoIndex = cursor.getColumnIndex(KEY_ADDTLINFO);
//        int completedIndex = cursor.getColumnIndex(KEY_COMPLETED);

        if (cursor.moveToFirst())
        {
            do
            {
                ToDoItem todoInfo = new ToDoItem();
                todoInfo.setTitle(cursor.getString(COLUMN_TITLE));
                todoInfo.setShortdesc(cursor.getString(COLUMN_SHORTDESC));
                todoInfo.setDuedate(cursor.getString(COLUMN_DUEDATE));
                todoInfo.setAddtlinfo(cursor.getString(COLUMN_ADDTLINFO));
                todoInfo.setId(cursor.getLong(COLUMN_ID));

                // '1' is true, '0' is false
                todoInfo.setCompleted(cursor.getInt(COLUMN_COMPLETED));

                list.add(todoInfo);
            }
            while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }
        return list;
    }//end of selectAll method



}//end of main class