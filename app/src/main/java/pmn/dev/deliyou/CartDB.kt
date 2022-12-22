package pmn.dev.deliyou

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class CartDB(context: Context?) : SQLiteOpenHelper(context,
    CartDB.DATABASE_NAME, null,
    CartDB.DB_VERSION) {

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(CartDB.CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}
    fun insertEmpty() {
        val db = this.writableDatabase
        val cv = ContentValues()
        for (x in 1..10) {
            cv.put(CartDB.KEY_ID, x)
            db.insert(CartDB.TABLE_NAME, null, cv)
        }
    }


    fun insertIntoDatabase(item_name: String, item_image: String, id: String?, item_price: String, item_amount: String) {
        val db: SQLiteDatabase
        db = this.writableDatabase
        val cv = ContentValues()
        cv.put(CartDB.ITEM_NAME, item_name)
        cv.put(CartDB.ITEM_IMAGE, item_image)
        cv.put(CartDB.KEY_ID, id)
        cv.put(CartDB.ITEM_PRICE, item_price)
        cv.put(CartDB.ITEM_AMOUNT, item_amount)
        db.insert(CartDB.TABLE_NAME, null, cv)
    }

    fun updateDatabase(id: String?, item_amount: String) {
        val db = this.writableDatabase
        val sql =
            "UPDATE ${CartDB.TABLE_NAME} SET $ITEM_AMOUNT=$item_amount WHERE $KEY_ID=$id"
        db.execSQL(sql)
    }

    fun getData(): Cursor {
        val db = this.readableDatabase
        val sql = "SELECT * FROM $TABLE_NAME"
        return db.rawQuery(sql, null, null)
    }

    companion object {
        private const val DB_VERSION = 1
        private const val DATABASE_NAME = "ShoppingCartDB"
        private const val TABLE_NAME = "ShoppingCartTable"
        public const val KEY_ID = "id"
        public const val ITEM_NAME = "itemName"
        public const val ITEM_IMAGE = "itemImage"
        private const val ITEM_PRICE = "itemPrice"
        private const val ITEM_AMOUNT = "itemAmount"
        private const val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " TEXT PRIMARY KEY," + ITEM_NAME + " TEXT,"
                + ITEM_IMAGE + " TEXT," + ITEM_PRICE + " TEXT, " + ITEM_AMOUNT + " TEXT)")
    }
}