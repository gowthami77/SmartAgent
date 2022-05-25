package ai.yantranet.smartagent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ai.yantranet.smartagent.model.Data;
import ai.yantranet.smartagent.model.Note;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "notes_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Note.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public  void insertNote(Data data) {


        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        boolean isPresent =false;
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        if(data.getDependencies().size()>0){

            for(int i=0;i<data.getDependencies().size();i++){
                isPresent =CheckIsDataAlreadyInDBorNot(Note.TABLE_NAME,Note.COLUMN_ID,data.getDependencies().get(i).getId());
                if(!isPresent) {

                    Log.e("inside"+i,"....");
                    values.put(Note.COLUMN_ID, data.getDependencies().get(i).getId());
                    values.put(Note.COLUMN_NAME, data.getDependencies().get(i).getName());
                    values.put(Note.COLUMN_PATH, data.getDependencies().get(i).getCdnPath());
                    values.put(Note.COLUMN_SIZE, data.getDependencies().get(i).getSizeInBytes());
                    values.put(Note.COLUMN_TYPE, data.getDependencies().get(i).getType());
                    db.insert(Note.TABLE_NAME, null, values);
                }

            }
        }
        db.close();

    }

    public  boolean CheckIsDataAlreadyInDBorNot(String TableName,
                                                      String dbfield, String fieldValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT EXISTS (SELECT * FROM "+TableName+" WHERE "+dbfield+"='"+fieldValue+"' LIMIT 1)";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        // cursor.getInt(0) is 1 if column with value exists
        if (cursor.getInt(0) == 1) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }

    }

}