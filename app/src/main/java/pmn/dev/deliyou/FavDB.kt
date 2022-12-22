package pmn.dev.deliyou

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log

class FavDB(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DB_VERSION) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {}
    fun insertEmpty() {
        val db = this.writableDatabase
        val cv = ContentValues()
        for (x in 1..10) {
            cv.put(KEY_ID, x)
            cv.put(FAVORITE_STATUS, "0")
            db.insert(TABLE_NAME, null, cv)
        }
    }

    fun insertIntoDatabase(item_name: String, item_image: String, id: String?, fav_status: String) {
        val db: SQLiteDatabase
        db = this.writableDatabase
        val cv = ContentValues()
        cv.put(ITEM_NAME, item_name)
        cv.put(ITEM_IMAGE, item_image)
        cv.put(KEY_ID, id)
        cv.put(FAVORITE_STATUS, fav_status)
        db.insert(TABLE_NAME, null, cv)
        Log.d("FavDB Status", "$item_name, favstatus - $fav_status - . $cv")
    }

    fun read_all_data(id: String): Cursor {
        val db = this.readableDatabase
        val sql = "select * from " + TABLE_NAME + " where " + KEY_ID + "=" + id + ""
        return db.rawQuery(sql, null, null)
    }

    fun remove_fav(id: String) {
        val db = this.writableDatabase
        val sql =
            "DELETE FROM $TABLE_NAME WHERE $KEY_ID=$id"
        db.execSQL(sql)
    }

    fun select_all_favorite_list(): Cursor {
        val db = this.readableDatabase
        val sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + FAVORITE_STATUS + " =" + "'1'"
        return db.rawQuery(sql, null, null)
    }

    fun check_if_exists(name: String): Boolean {
        name.replace("'", "''")
        val db = this.readableDatabase
        val sql = "SELECT * FROM $TABLE_NAME WHERE $ITEM_NAME = '$name'"
        db.rawQuery(sql, null, null).use {
            if (it.moveToFirst()) {
                return true
            }
        }


        return false
    }



    companion object {
        private const val DB_VERSION = 1
        private const val DATABASE_NAME = "FavRestsDB"
        private const val TABLE_NAME = "favoriteTable"
        public const val KEY_ID = "id"
        public const val ITEM_NAME = "itemName"
        public const val ITEM_IMAGE = "itemImage"
        private const val FAVORITE_STATUS = "fStatus"
        internal const val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " TEXT PRIMARY KEY," + ITEM_NAME + " TEXT,"
                + ITEM_IMAGE + " TEXT," + FAVORITE_STATUS + " TEXT)")
    }
}