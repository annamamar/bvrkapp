package com.bvrk.mobile.android.util.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PocDbhelpher extends SQLiteOpenHelper {

    private static String TAG = "amarr";
    public static final String DB_NAME_HD = "com.android.bobby_nov08_db_5" ;
    private static final int DB_VERSION_HD = 3;

    // Table Names
    public static final String TABLE_LOCATION = "ulocation";
    public static final String TABLE_CLIENT_INFO = "clientinfo";
    public static final String TABLE_CLIENT_WALLET = "clientwallet";
    public static final String TABLE_CLIENT_SETTLEMENT = "clientsettlement";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_VENDOR_INFO = "vendorinfo";
    public static final String TABLE_VENDOR_WALLET = "vendorwallet";
    public static final String TABLE_VENDOR_SETTLEMENT = "vendorsettlement";

    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_UPDATED_AT = "updated_at";
    public static final String KEY_STATUS = "status";
    public static final String KEY_APPUSERREF = "appuserref";

    // LOCATION Table - column names
    public static final String KEY_TAG_NAME = "ulocation_name";

    // USER INFO Table - column names
    public static final String KEY_U_NAME = "u_name";
    public static final String KEY_U_PHONE = "u_phone";
    public static final String KEY_U_ADDRESS = "u_address";
    public static final String KEY_U_LOCATION = "u_location";
    public static final String KEY_U_UNAME = "u_uname";

    // ORDERS Table - column names
    public static final String KEY_ORDER_TYPE = "o_type";
    public static final String KEY_ORDER_CLIENTREF = "o_clientref";
    public static final String KEY_ORDER_VENDORREF = "o_vendorref";
    public static final String KEY_ORDER_DATA = "o_data";
    public static final String KEY_ORDER_CREDIT = "o_credit";
    public static final String KEY_ORDER_VENDORAMT = "o_vendoramt";
    public static final String KEY_ORDER_AMOUNT = "o_amount";
    public static final String KEY_ORDER_PAID = "o_paid";
    public static final String KEY_ORDER_YEAR = "o_year";
    public static final String KEY_ORDER_MONTH = "o_month";
    public static final String KEY_ORDER_DAY = "o_day";

    //client_wallet Table - column names
    public static final String KEY_CWALLET_CREF = "cw_cref";
    public static final String KEY_CWALLET_BAL = "cw_bal";

    //Vendor Settlement Table - column names
    public static final String KEY_VS_ORDERREF = "vs_orderref";
    public static final String KEY_VS_AMOUNT = "vs_amount";
    public static final String KEY_VS_PAID = "vs_paid";


    // LOCATION table create statement
    private static final String CREATE_TABLE_LOCATION = "CREATE TABLE " + TABLE_LOCATION
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_TAG_NAME + " TEXT,"
            + KEY_UPDATED_AT + " DATETIME ,"
            + KEY_CREATED_AT + " DATETIME" + ");";

    // CLIENT_INFO table create statement
    private static final String CREATE_TABLE_CLIENT_INFO = "CREATE TABLE " + TABLE_CLIENT_INFO
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_U_NAME + " TEXT,"
            + KEY_U_PHONE + " TEXT," + KEY_U_ADDRESS + " TEXT,"
            + KEY_U_LOCATION + " TEXT," + KEY_U_UNAME + " TEXT not null unique,"
            + KEY_UPDATED_AT + " DATETIME ,"
            + KEY_STATUS +" TEXT ,"
            + KEY_CREATED_AT + " DATETIME" + ");";

    // VENDOR_INFO table create statement
    private static final String CREATE_TABLE_VENDOR_INFO = "CREATE TABLE " + TABLE_VENDOR_INFO
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_U_NAME + " TEXT,"
            + KEY_U_PHONE + " TEXT," + KEY_U_ADDRESS + " TEXT,"
            + KEY_U_LOCATION + " TEXT," + KEY_U_UNAME + " TEXT not null unique, "
            + KEY_UPDATED_AT + " DATETIME ," + KEY_STATUS +" TEXT ,"
            + KEY_CREATED_AT + " DATETIME" + ");";


    // ORDERS table create statement
    private static final String CREATE_TABLE_ORDERS = "CREATE TABLE " + TABLE_ORDERS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_ORDER_TYPE + " TEXT,"
            + KEY_ORDER_CLIENTREF + " INTEGER, " + KEY_ORDER_VENDORREF + " INTEGER, "
            + KEY_ORDER_DATA + " TEXT, "
            + KEY_ORDER_CREDIT + " REAL,"
            + KEY_ORDER_VENDORAMT + " REAL,"
            + KEY_ORDER_AMOUNT + " REAL,"
            + KEY_ORDER_PAID + " REAL,"
            + KEY_STATUS +" TEXT ,"
            + KEY_APPUSERREF  + " INTEGER ,"
            + KEY_ORDER_YEAR  + " TEXT,"
            + KEY_ORDER_MONTH + " TEXT,"
            + KEY_ORDER_DAY   + " TEXT,"
            + KEY_UPDATED_AT  + " DATETIME ,"+ KEY_CREATED_AT + " DATETIME" + ");";

    // client_wallet table create statement
    private static final String CREATE_TABLE_CLIENT_WALLET = "CREATE TABLE " + TABLE_CLIENT_WALLET
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_CWALLET_CREF + " INTEGER," + KEY_CWALLET_BAL + " REAL,"
            + KEY_UPDATED_AT + " DATETIME ," + KEY_CREATED_AT + " DATETIME" + ");";

    // client_wallet table create statement
    private static final String CREATE_TABLE_VENDOR_WALLET = "CREATE TABLE " + TABLE_VENDOR_WALLET
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_CWALLET_CREF + " INTEGER," + KEY_CWALLET_BAL + " REAL,"
            + KEY_UPDATED_AT + " DATETIME ," + KEY_CREATED_AT + " DATETIME" + ");";

    // VENDOR_SETTLEMENT table create statement
    private static final String CREATE_TABLE_VENDOR_SETTLEMENT = "CREATE TABLE " + TABLE_VENDOR_SETTLEMENT
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_VS_ORDERREF + " INTEGER," + KEY_ORDER_VENDORREF + " INTEGER,"
            + KEY_VS_AMOUNT + " REAL," + KEY_VS_PAID + " REAL,"
            + KEY_APPUSERREF + " INTEGER ," + KEY_STATUS +" TEXT ,"
            + KEY_UPDATED_AT + " DATETIME ,"
            + KEY_CREATED_AT + " DATETIME" + ");";

    // client_SETTLEMENT table create statement
    private static final String CREATE_TABLE_CLIENT_SETTLEMENT = "CREATE TABLE " + TABLE_CLIENT_SETTLEMENT
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_VS_ORDERREF + " INTEGER," + KEY_ORDER_VENDORREF + " INTEGER,"
            + KEY_VS_AMOUNT + " REAL," + KEY_VS_PAID + " REAL,"
            + KEY_APPUSERREF + " INTEGER ," + KEY_STATUS +" TEXT ,"
            + KEY_UPDATED_AT + " DATETIME ,"
            + KEY_CREATED_AT + " DATETIME" + ");";

    public PocDbhelpher(Context context) {
        super(context, DB_NAME_HD, null, DB_VERSION_HD);
        //Log.d(TAG, "DatabaseHelper: path "+context.getDatabasePath(DB_NAME_HD).getPath());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LOCATION);
        db.execSQL(CREATE_TABLE_CLIENT_INFO);
        db.execSQL(CREATE_TABLE_CLIENT_WALLET);
        db.execSQL(CREATE_TABLE_CLIENT_SETTLEMENT);
        db.execSQL(CREATE_TABLE_VENDOR_INFO);
        db.execSQL(CREATE_TABLE_VENDOR_WALLET);
        db.execSQL(CREATE_TABLE_VENDOR_SETTLEMENT);
        db.execSQL(CREATE_TABLE_ORDERS);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getDateTimedb() {
        /*
        Calendar cal         = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String joiningDate   = sdf.format(cal.getTime());
        */
        /*
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date); */
        Calendar cal         = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String joiningDate   = sdf.format(cal.getTime());
        return joiningDate;
    }

    // Start helper methods

    public long addData(String dbtblname, ContentValues contentValues){
        long result=-1;
        try {
            SQLiteDatabase database= this.getWritableDatabase();
            //SQLiteDatabase db_HD = this.getWritableDatabase();
            String gettimestamp = getDateTime();//getDateTimedb();
            contentValues.put(KEY_UPDATED_AT,gettimestamp);
            contentValues.put(KEY_CREATED_AT,gettimestamp);
            Log.d(TAG, "addData:db "+dbtblname +" ;; " + contentValues.toString());
            result = database.insertOrThrow(dbtblname, null, contentValues);
                if(result>0)
                {
                    String maindb=""; ContentValues wvales = new ContentValues();
                    if(dbtblname.equalsIgnoreCase(TABLE_CLIENT_INFO) | dbtblname.equalsIgnoreCase(TABLE_VENDOR_INFO)) {
                        if (dbtblname.equalsIgnoreCase(TABLE_CLIENT_INFO)) {
                            maindb = TABLE_CLIENT_WALLET;
                        } else if (dbtblname.equalsIgnoreCase(TABLE_VENDOR_INFO)) {
                            maindb = TABLE_VENDOR_WALLET;
                        }
                        wvales.put(KEY_CWALLET_CREF, result);
                        wvales.put(KEY_CWALLET_BAL, 0);
                        wvales.put(KEY_UPDATED_AT, gettimestamp);
                        wvales.put(KEY_CREATED_AT, gettimestamp);
                        database.insertOrThrow(maindb, null, wvales);
                    }
                }
            Log.d(TAG, "addData: res"+result);
            if(database.isOpen())
                database.close();
        } catch (SQLiteException e){
            Log.d(TAG, "addData: "+Log.getStackTraceString(e));
        } catch (Exception e){
            Log.d(TAG, "addData: Exp. "+Log.getStackTraceString(e));
        }
        return result;//==-1?false:true;
    }

    public Boolean updateData(String dbname, ContentValues contentValues, String wherestring , String[] refname ){
        long result=1;
        try {
            Log.d(TAG, "updateData: "+dbname +" "+contentValues.toString());
            SQLiteDatabase database= this.getWritableDatabase();
            String gettimestamp = getDateTime();//getDateTimedb();
            contentValues.put(KEY_UPDATED_AT,gettimestamp);
            result = database.update(dbname, contentValues, wherestring, refname);
            if(database.isOpen())
                database.close();
            Log.d(TAG, "updateData: "+dbname + " : "+result);
        } catch (SQLiteException e){
            Log.d(TAG, "updateData: "+Log.getStackTraceString(e));
        } catch (Exception e){
            Log.d(TAG, "updateData: Exp. "+Log.getStackTraceString(e));
        }
        return result==-1?false:true;
    }

    public Cursor getorderbyuser(String usertype){
        Cursor cursor=null;
        //SELECT foo, count(bar) FROM mytable GROUP BY bar ORDER BY count(bar) DESC;
        //KEY_ID
        String query="SELECT "+usertype +" , count("+KEY_ID+") FROM "+TABLE_ORDERS + " GROUP BY "+KEY_ID + " ORDER BY count("+KEY_ID+" ) DESC;";
        SQLiteDatabase db = this.getWritableDatabase();
        cursor=db.rawQuery(query,null);
        /*if(cursor.moveToFirst()) {
            // cursor.getInt(0);
        }*/
        return cursor;
    }

    public int getTaskCount(String KEY_TASK_TASKLISTID,long tasklist_id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor= db.rawQuery("SELECT COUNT (*) FROM " + TABLE_ORDERS + " WHERE " + KEY_TASK_TASKLISTID + "=?", new String[] { String.valueOf(tasklist_id) });
        cursor.moveToFirst();
        int count= cursor.getInt(0);
        cursor.close();
        return count;
    }

    public Cursor SearchData(String MY_TABLE_NAME,String[] cdata,String [] refname,String wherestring,String orderclause){
        Cursor cursor = null;
        //cdata= new String[] { COLUMN_MODEL_LIST_NAME_HD,COLUMN_MODEL_LIST_DNAME_HD ,COLUMN_MODEL_LIST_IMAGE_NAME_HD,
        // COLUMN_MODEL_LIST_PATTERN_HD,COLUMN_MODEL_LIST_PATTERN_NAME_HD,COLUMN_MODEL_LIST_SYNC_HD,COLUMN_MODEL_LIST_STATUS_HD };
        try {
            // SQLiteDatabase db= this.getWritableDatabase();
            SQLiteDatabase database= this.getWritableDatabase();
            cursor = database.query(MY_TABLE_NAME, cdata, wherestring, refname, null, null, orderclause, null);
        } catch (SQLiteException e){
            Log.d(TAG, "updateData: "+Log.getStackTraceString(e));
        } catch (Exception e){
            Log.d(TAG, "updateData: Exp. "+Log.getStackTraceString(e));
        }
        return cursor;
    }

    public Cursor getAlldata(String qry) {
        Cursor cursor = null;
        try {
            //SQLiteDatabase db= this.getWritableDatabase();
            //db= this.getWritableDatabase();
            SQLiteDatabase database= this.getWritableDatabase();
            cursor = database.rawQuery(qry, null);
            Log.d(TAG, "getAlldata: "+cursor.toString());
        } catch (SQLiteException e){
            Log.d(TAG, "getAlldata: getAlldata "+Log.getStackTraceString(e));
        } catch (Exception e){
            Log.d(TAG, "getAlldata: getAlldata "+Log.getStackTraceString(e));
        }
        return cursor;
    }

    public void deletedata(String tbl,String whereclause,String[] whereargs) {
        //SQLiteDatabase dbb= this.getWritableDatabase();
        SQLiteDatabase database= this.getWritableDatabase();
        try {
           // database.beginTransaction();
            database.delete(tbl, whereclause, whereargs);
           // database.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "deletedata "+Log.getStackTraceString(e));
        } finally {
           // database.endTransaction();
        }
    }

    /*
    public Boolean addData(String dbname, Object obj){
        Boolean wasSuccess = true;
        long result=1;
        SQLiteDatabase db_HD = this.getWritableDatabase();
        modelcolor_pojo modelcolor_pojo;
        seat_model_img modeldata_pojo;
        seat_model_list_pojo modelnames_pojo;
        try {
            if(obj instanceof seat_model_list_pojo) {
                modelnames_pojo = (seat_model_list_pojo) obj;
            } else if(obj instanceof seat_model_img) {
                modeldata_pojo= (seat_model_img) obj;
            } else if(obj instanceof modelcolor_pojo) {
                modelcolor_pojo= (modelcolor_pojo) obj;
            } else {
                return false;
            }
        } catch (ClassCastException e){
            Log.d(TAG, "addData: classcast "+e.getMessage());
            return false;
        }
        ContentValues initialValues = new ContentValues();
        try {
            //db_HD.beginTransaction();
            if (dbname.equalsIgnoreCase(REG_MODELS)) {
                modelnames_pojo = (seat_model_list_pojo) obj;
               // Log.d(TAG, "addData: models "+modelnames_pojo.toString());
                initialValues.put(COLUMN_MODEL_LIST_NAME_HD, modelnames_pojo.getModel_name());
                initialValues.put(COLUMN_MODEL_LIST_DNAME_HD, modelnames_pojo.getModel_dname());
                initialValues.put(COLUMN_MODEL_LIST_PATTERN_HD, modelnames_pojo.getModel_pattern());
                initialValues.put(COLUMN_MODEL_LIST_PATTERN_NAME_HD, modelnames_pojo.getModel_pattern_name());
                initialValues.put(COLUMN_MODEL_LIST_SYNC_HD, modelnames_pojo.getModel_sync());
                initialValues.put(COLUMN_MODEL_LIST_STATUS_HD, modelnames_pojo.getModel_status());
                result =  db_HD.insert(TBL_MODEL_LIST_DB, null, initialValues);
            } else if (dbname.equalsIgnoreCase(REG_MODELIMG)) {
                modeldata_pojo= (seat_model_img) obj;
                initialValues.put(COLUMN_MODEL_IMG_MAIN_HD, modeldata_pojo.getModel_main());
                initialValues.put(COLUMN_MODEL_IMG_DATA_HD, modeldata_pojo.getModel_data());
                initialValues.put(COLUMN_MODEL_IMG_TAG_HD, modeldata_pojo.getModel_tag());
                initialValues.put(COLUMN_MODEL_IMG_ORDER_HD, modeldata_pojo.getModel_order());
                initialValues.put(COLUMN_MODEL_IMG_SYNC_HD, modeldata_pojo.getModel_sync());
                initialValues.put(COLUMN_MODEL_IMG_STATUS_HD, modeldata_pojo.getModel_status());
                result = db_HD.insert(TBL_MODEL_IMG_DB, null, initialValues);
            } else if (dbname.equalsIgnoreCase(REG_COLORS)) {
                modelcolor_pojo= (modelcolor_pojo) obj;
                initialValues.put(COLUMN_MODEL_COLOR_NAME_HD, modelcolor_pojo.getColor_name());
                initialValues.put(COLUMN_MODEL_COLOR_SYNC_HD, modelcolor_pojo.getColor_sync());
                result = db_HD.insert(TBL_MODEL_COLOR_DB, null, initialValues);
            }
            if (result == -1) {
                wasSuccess = false;
            } else {
                //db_HD.setTransactionSuccessful();
            }
            Log.d(TAG, "addData: "+dbname+" resu <-> "+result);
        } catch (SQLiteException e){
            Log.d(TAG, "addData: "+e.getMessage());
        } catch (Exception e){
            Log.d(TAG, "addData: Exp. "+e.getMessage());
        } finally {
            //db_HD.endTransaction();
        }
        db_HD.close();
        return wasSuccess;
    }

    public Boolean UpdateData(String regtype, String refname, String wherestring, ContentValues data){
        Boolean qstat=false;
        try {
            SQLiteDatabase db_HD = this.getWritableDatabase();
            String MY_TABLE_NAME = "";
            if (regtype.equalsIgnoreCase(REG_MODELS)) {
                MY_TABLE_NAME = TBL_MODEL_LIST_DB;
            } else if(regtype.equalsIgnoreCase(REG_MODELIMG)){
                MY_TABLE_NAME = TBL_MODEL_IMG_DB;
            } else if(regtype.equalsIgnoreCase(REG_COLORS)) {
                MY_TABLE_NAME = TBL_MODEL_COLOR_DB;
            }
            int i =db_HD.update(MY_TABLE_NAME, data, wherestring, new String[]{refname});
            //Log.d(TAG, "UpdateData: "+MY_TABLE_NAME + refname+" resupdte "+i);
            db_HD.close();
            if(i>0)
                qstat = true;
        } catch (SQLiteException e){
            Log.d(TAG, "updateImgData: "+ Log.getStackTraceString(e));
        } catch (Exception e){
            //String stackTrace = Log.getStackTraceString(e);
            Log.d(TAG, "BulkImgUpdate372: "+ Log.getStackTraceString(e));
        }
        return qstat;
    }

    public Boolean BulkImgUpdate(List<Object> obj){
        Boolean stat=false;
        try {
            SQLiteDatabase db_HD = this.getWritableDatabase();
            for(Object o1: obj) {
                dbimgupdatehelper_pojo pojo = (dbimgupdatehelper_pojo) o1;
                String encfilename=pojo.getEncfilename();
                String syncdata = pojo.getSyncdata();
                String MY_TABLE_NAME = "";
                String wherestring = "";
                String refname = pojo.getSearchstr();
                String regtype = pojo.getDbtype();
                ContentValues cv = new ContentValues();
                if (regtype.equalsIgnoreCase(REG_MODELS)) {
                    MY_TABLE_NAME = TBL_MODEL_LIST_DB;
                    wherestring = COLUMN_MODEL_LIST_NAME_HD + " = ?";
                    cv.put(COLUMN_MODEL_LIST_SYNC_HD, syncdata);
                    cv.put(COLUMN_MODEL_LIST_IMAGE_NAME_HD, encfilename);
                } else if(regtype.equalsIgnoreCase(REG_MODELIMG)){
                    MY_TABLE_NAME = TBL_MODEL_IMG_DB;
                    wherestring = COLUMN_MODEL_IMG_DATA_HD + " = ?";
                    cv.put(COLUMN_MODEL_IMG_SYNC_HD, syncdata);
                    cv.put(COLUMN_MODEL_IMG_IMAGE_NAME_HD, encfilename);
                } else if(regtype.equalsIgnoreCase(REG_COLORS)){
                    MY_TABLE_NAME = TBL_MODEL_COLOR_DB;
                    wherestring = COLUMN_MODEL_COLOR_NAME_HD + " = ?";
                    cv.put(COLUMN_MODEL_COLOR_SYNC_HD, syncdata);
                    cv.put(COLUMN_MODEL_COLOR_IMAGE_NAME_HD, encfilename);
                }
                    int i =db_HD.update(MY_TABLE_NAME, cv, wherestring, new String[]{refname});
                if(i>0)
                    stat=true;
                //Log.d(TAG, "BulkImgUpdate: "+regtype + " "+refname+" "+encfilename+" status upd "+i);
            }
            db_HD.close();
            //Log.d(TAG, "BulkImgUpdate: 322 trans sucessfull ");
        } catch (SQLiteException e){
            Log.d(TAG, "updateImgData: "+ Log.getStackTraceString(e));
        } catch (Exception e){
            //String stackTrace = Log.getStackTraceString(e);
            Log.d(TAG, "BulkImgUpdate372: "+ Log.getStackTraceString(e));
        }
        return stat;
    }

    public ArrayList<LinkedHashMap<String, String>> getAlldata(String tabl) {
        ArrayList<LinkedHashMap<String, String>> list = new ArrayList<>();
        try {
            SQLiteDatabase db_HD = this.getReadableDatabase();
            String qry = "Select *  from " + tabl + " ;";
            if (tabl.equalsIgnoreCase(REG_MODELIMG)) {
                qry = "Select *  from " + TBL_MODEL_IMG_DB + " ;";
            } else if (tabl.equalsIgnoreCase(REG_COLORS)) {
                qry = "Select *  from " + TBL_MODEL_COLOR_DB + " ;";
            } else if (tabl.equalsIgnoreCase(REG_MODELS)) {
                qry = "Select *  from " + TBL_MODEL_LIST_DB + " ;";
            }
            Cursor cursor = db_HD.rawQuery(qry, null);
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                if (tabl.equalsIgnoreCase(REG_MODELIMG)) {
                    map.put(COLUMN_MODEL_IMG_REF_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_IMG_REF_HD)));
                    map.put(COLUMN_MODEL_IMG_DATA_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_IMG_DATA_HD)));
                    map.put(COLUMN_MODEL_IMG_SYNC_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_IMG_SYNC_HD)));
                    map.put(COLUMN_MODEL_IMG_IMAGE_NAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_IMG_IMAGE_NAME_HD)));
                } else if (tabl.equalsIgnoreCase(REG_COLORS)) {
                    map.put(COLUMN_MODEL_COLOR_REF_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_COLOR_REF_HD)));
                    map.put(COLUMN_MODEL_COLOR_NAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_COLOR_NAME_HD)));
                    map.put(COLUMN_MODEL_COLOR_SYNC_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_COLOR_SYNC_HD)));
                    map.put(COLUMN_MODEL_COLOR_IMAGE_NAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_COLOR_IMAGE_NAME_HD)));
                } else if (tabl.equalsIgnoreCase(REG_MODELS)) {
                    map.put(COLUMN_MODEL_LIST_REF_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_REF_HD)));
                    map.put(COLUMN_MODEL_LIST_NAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_NAME_HD)));
                    map.put(COLUMN_MODEL_LIST_DNAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_DNAME_HD)));
                    map.put(COLUMN_MODEL_LIST_IMAGE_NAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_IMAGE_NAME_HD)));
                    map.put(COLUMN_MODEL_LIST_PATTERN_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_PATTERN_HD)));
                    map.put(COLUMN_MODEL_LIST_PATTERN_NAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_PATTERN_NAME_HD)));
                    map.put(COLUMN_MODEL_LIST_SYNC_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_SYNC_HD)));
                    map.put(COLUMN_MODEL_LIST_STATUS_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_STATUS_HD)));
                }
                String mapdata = "";
            /*for (Map.Entry<String, String> entry : map.entrySet()) {
                mapdata=mapdata+entry.getKey() + " -> " + entry.getValue()+"\n";
            }*/
                //Log.d(TAG, "addseats: 576 "+mapdata);
        /*
                list.add(i, map);
                cursor.moveToNext();
            }
            if (!cursor.isClosed())
                cursor.close();
            db_HD.close();
        } catch (SQLiteException e){
            Log.d(TAG, "getAlldata: sqlexp "+ Log.getStackTraceString(e));
        } catch (Exception e){
            Log.d(TAG, "getAlldata: exp "+ Log.getStackTraceString(e));
        }
        return list;
    }


    public ArrayList<LinkedHashMap<String, String>> getFulldata(String tabl) {
        ArrayList<LinkedHashMap<String, String>> list = new ArrayList<>();
        try {
            SQLiteDatabase db_HD = this.getReadableDatabase();
            String qry = "Select *  from " + tabl + " ;";
            if (tabl.equalsIgnoreCase(REG_MODELIMG)) {
                qry = "Select *  from " + TBL_MODEL_IMG_DB + " ;";
            } else if (tabl.equalsIgnoreCase(REG_COLORS)) {
                qry = "Select *  from " + TBL_MODEL_COLOR_DB + " ;";
            } else if (tabl.equalsIgnoreCase(REG_MODELS)) {
                qry = "Select *  from " + TBL_MODEL_LIST_DB + " ;";
            }
            Cursor cursor = db_HD.rawQuery(qry, null);
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                if (tabl.equalsIgnoreCase(REG_MODELIMG)) {
                    map.put(COLUMN_MODEL_IMG_REF_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_IMG_REF_HD)));
                    map.put(COLUMN_MODEL_IMG_DATA_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_IMG_DATA_HD)));
                    map.put(COLUMN_MODEL_IMG_SYNC_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_IMG_SYNC_HD)));
                    map.put(COLUMN_MODEL_IMG_IMAGE_NAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_IMG_IMAGE_NAME_HD)));
                } else if (tabl.equalsIgnoreCase(REG_COLORS)) {
                    map.put(COLUMN_MODEL_COLOR_REF_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_COLOR_REF_HD)));
                    map.put(COLUMN_MODEL_COLOR_NAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_COLOR_NAME_HD)));
                    map.put(COLUMN_MODEL_COLOR_SYNC_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_COLOR_SYNC_HD)));
                    map.put(COLUMN_MODEL_COLOR_IMAGE_NAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_COLOR_IMAGE_NAME_HD)));

                } else if (tabl.equalsIgnoreCase(REG_MODELS)) {
                    map.put(COLUMN_MODEL_LIST_REF_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_REF_HD)));
                    map.put(COLUMN_MODEL_LIST_NAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_NAME_HD)));
                    map.put(COLUMN_MODEL_LIST_DNAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_DNAME_HD)));
                    map.put(COLUMN_MODEL_LIST_IMAGE_NAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_IMAGE_NAME_HD)));
                    map.put(COLUMN_MODEL_LIST_PATTERN_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_PATTERN_HD)));
                    map.put(COLUMN_MODEL_LIST_PATTERN_NAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_PATTERN_NAME_HD)));
                    map.put(COLUMN_MODEL_LIST_SYNC_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_SYNC_HD)));
                }
                //Log.d(TAG, "getAlldata: "+map.toString());
                //HashMap<String,String> map= databaseHandler.getdataBycolumn(TBL_MODELDATA,seatrefname);
                String mapdata = "";
            /*for (Map.Entry<String, String> entry : map.entrySet()) {
                mapdata=mapdata+entry.getKey() + " -> " + entry.getValue()+"\n";
            }*/
                //Log.d(TAG, "addseats: 576 "+mapdata);
        /*
                list.add(i, map);
                cursor.moveToNext();
            }
            if (!cursor.isClosed())
                cursor.close();
            db_HD.close();
        } catch (SQLiteException e){
            Log.d(TAG, "getAlldata: sqlexp "+ Log.getStackTraceString(e));
        } catch (Exception e){
            Log.d(TAG, "getAlldata: exp "+ Log.getStackTraceString(e));
        }
        return list;
    }


    public ArrayList<LinkedHashMap<String, String>> SearchData(String regtype, String[] refname, String wherestring, String orderclause){
        ArrayList<LinkedHashMap<String, String>> list = new ArrayList<>();
        try {
            SQLiteDatabase db_HD = this.getReadableDatabase();
            String MY_TABLE_NAME = "";
            String[] cdata=null;
            if (regtype.equalsIgnoreCase(REG_MODELS)) {
                MY_TABLE_NAME = TBL_MODEL_LIST_DB;
                cdata= new String[] { COLUMN_MODEL_LIST_NAME_HD,COLUMN_MODEL_LIST_DNAME_HD ,COLUMN_MODEL_LIST_IMAGE_NAME_HD,
                 COLUMN_MODEL_LIST_PATTERN_HD,COLUMN_MODEL_LIST_PATTERN_NAME_HD,COLUMN_MODEL_LIST_SYNC_HD,COLUMN_MODEL_LIST_STATUS_HD };
            } else if(regtype.equalsIgnoreCase(REG_MODELIMG)){
                MY_TABLE_NAME = TBL_MODEL_IMG_DB;
                cdata= new String[] { COLUMN_MODEL_IMG_MAIN_HD,COLUMN_MODEL_IMG_DATA_HD ,
                 COLUMN_MODEL_IMG_IMAGE_NAME_HD,COLUMN_MODEL_IMG_ORDER_HD,COLUMN_MODEL_IMG_TAG_HD ,COLUMN_MODEL_IMG_STATUS_HD};
            } else if(regtype.equalsIgnoreCase(REG_COLORS)) {
                MY_TABLE_NAME = TBL_MODEL_COLOR_DB;
                cdata= new String[] { COLUMN_MODEL_COLOR_NAME_HD,COLUMN_MODEL_COLOR_SYNC_HD ,
                        COLUMN_MODEL_COLOR_IMAGE_NAME_HD };
            }
            //date + " ASC, " + semester  + " ASC"
            Cursor cursor = db_HD.query(MY_TABLE_NAME, cdata, wherestring, refname, null, null, orderclause, null);
            cursor.moveToFirst();
            for(int i = 0; i < cursor.getCount(); i++) {
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                if (regtype.equalsIgnoreCase(REG_MODELIMG)) {
                    map.put(COLUMN_MODEL_IMG_MAIN_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_IMG_MAIN_HD)));
                    map.put(COLUMN_MODEL_IMG_DATA_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_IMG_DATA_HD)));
                    map.put(COLUMN_MODEL_IMG_IMAGE_NAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_IMG_IMAGE_NAME_HD)));
                    map.put(COLUMN_MODEL_IMG_ORDER_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_IMG_ORDER_HD)));
                    map.put(COLUMN_MODEL_IMG_TAG_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_IMG_TAG_HD)));
                    map.put(COLUMN_MODEL_IMG_STATUS_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_IMG_STATUS_HD)));
                } else if (regtype.equalsIgnoreCase(REG_COLORS)) {
                    //map.put(COLUMN_MODEL_COLOR_REF_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_COLOR_REF_HD)));
                    map.put(COLUMN_MODEL_COLOR_NAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_COLOR_NAME_HD)));
                    map.put(COLUMN_MODEL_COLOR_SYNC_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_COLOR_SYNC_HD)));
                    map.put(COLUMN_MODEL_COLOR_IMAGE_NAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_COLOR_IMAGE_NAME_HD)));
                } else if (regtype.equalsIgnoreCase(REG_MODELS)) {
                    //map.put(COLUMN_MODEL_LIST_REF_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_REF_HD)));
                    map.put(COLUMN_MODEL_LIST_NAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_NAME_HD)));
                    map.put(COLUMN_MODEL_LIST_DNAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_DNAME_HD)));
                    map.put(COLUMN_MODEL_LIST_IMAGE_NAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_IMAGE_NAME_HD)));
                    map.put(COLUMN_MODEL_LIST_PATTERN_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_PATTERN_HD)));
                    map.put(COLUMN_MODEL_LIST_PATTERN_NAME_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_PATTERN_NAME_HD)));
                    map.put(COLUMN_MODEL_LIST_SYNC_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_SYNC_HD)));
                    map.put(COLUMN_MODEL_LIST_STATUS_HD, cursor.getString(cursor.getColumnIndex(COLUMN_MODEL_LIST_STATUS_HD)));
                }
                    String mapdata = "";
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    mapdata = mapdata + entry.getKey() + " -> " + entry.getValue() + "\n";
                }
               // Log.d(TAG, "SearchData: 410 " + mapdata);

                list.add(i, map);
                cursor.moveToNext();
            }
                if(!cursor.isClosed())
                    cursor.close();
                if(db_HD.isOpen())
                    db_HD.close();
        } catch (SQLiteException e){
            Log.d(TAG, "getAlldata: sqlexp "+ Log.getStackTraceString(e));
        } catch (Exception e){
            Log.d(TAG, "getAlldata: exp "+ Log.getStackTraceString(e));
        }
        return list;
    }

    */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENT_INFO);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENT_WALLET);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENDOR_INFO);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENDOR_SETTLEMENT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
            onCreate(db);
        }
    }

}
