package epiphany_soft.wtw.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Camilo on 23/03/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    //Si se cambia el esquema de la base de datos entonces se debe incrementar la version
    public static final int DATABASE_VERSION = 1;
    private static String DB_PATH = "/data/data/epiphany_soft.wtw/databases/";
    private static String DB_NAME = "wtw.sqlite";
    private SQLiteDatabase myDataBase;
    private Context myContext = null;


    /*Ejemplo Creación
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String NOT_NULL = "NOT_NULL";
    public static final String SQL_CREATE_USUARIOS =
                "CREATE TABLE " + DataBaseContract.UsuarioContract.TABLE_NAME + " (" +
                    DataBaseContract.UsuarioContract._ID + " INTEGER" + COMMA_SEP +
                    DataBaseContract.UsuarioContract.COLUMN_NAME_USUARIO_ID + TEXT_TYPE + NOT_NULL+ COMMA_SEP +
                    DataBaseContract.UsuarioContract.COLUMN_NAME_USUARIO_NOMBRE + TEXT_TYPE + COMMA_SEP +
                    DataBaseContract.UsuarioContract.COLUMN_NAME_USUARIO_PASSWORD + TEXT_TYPE + COMMA_SEP +
                    "PRIMARY KEY("+DataBaseContract.UsuarioContract.COLUMN_NAME_USUARIO_ID+")"+
            " )";
    private static final String SQL_DELETE_USUARIOS = "DROP TABLE IF EXISTS " + DataBaseContract.UsuarioContract.TABLE_NAME;
    */
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        myContext=context;
       // context.getFilesDir().getPath();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Esta base de datos es solo una cache para datos online, así que su política de upgrade
        // es simplemente descargar los datos y empezar nuevamente
       // db.execSQL(SQL_DELETE_USUARIOS);
        //onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /*Codigo de: http://developinginandroid.blogspot.com.co/2014/01/connect-db-sqlite-to-android-with.html */
    public void createDataBase() throws IOException{
        boolean dbExist = checkDataBase();
        if(dbExist){
            //do nothing - database already exist
        }else{
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error al copiar la base de datos");
            }
        }

    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    public void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

        }catch(SQLiteException e){
            //database doesn't exist yet.
        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    public void openDataBase(){

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

}
