package pmn.dev.deliyou

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RestaurantAdapter(private val RestaurantList : ArrayList<Restaurant>) : RecyclerView.Adapter<RestaurantViewHolder>(){
    private var favDB: FavDB? = null
    private var restaurantFavList: ArrayList<RestaurantItem> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        favDB = FavDB(parent.context)
        val layoutInflater = LayoutInflater.from(parent.context)
        var restId=1
        for(res in RestaurantList){
            Log.i("mika", "${res.name} and ${res.address} and ${res.menu} and ${res.imageSrc} and ${res.precio}'")

            val check = favDB!!.check_if_exists(res.name.toString())
            var statusAux = "0"
            if (check) {
                statusAux = "1"
            }
            var resFavItem = RestaurantItem(res.imageSrc.toString(), res.name.toString(), statusAux, restId.toString())
            restId++
            restaurantFavList.add(resFavItem)
        }

        val prefs = parent.context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)
        if (firstStart) {
            createTableOnFirstStart(parent.context)
        }
        return RestaurantViewHolder(layoutInflater.inflate(R.layout.restaurant_item, parent, false))
    }

    override fun getItemCount(): Int = RestaurantList.size

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val item = RestaurantList[position]
        val favItem = restaurantFavList[position]
        val check = favDB!!.check_if_exists(item.name.toString())
        holder.setOnClickListeners(restaurantFavList, favDB!!, check);
        holder.render(item, check)
    }

    private fun createTableOnFirstStart(context: Context) {
        favDB!!.insertEmpty()
        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("firstStart", false)
        editor.apply()
    }
}

class RestaurantViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
    val context = view.context
    val restaurantName = view.findViewById<TextView>(R.id.restaurantName)
    val menu = view.findViewById<TextView>(R.id.restaurantMenu)
    val precio = view.findViewById<TextView>(R.id.restaurantPrice)
    val imageSrc = view.findViewById<ImageView>(R.id.restaurantImgSrc)
    val menuButton = view.findViewById<Button>(R.id.menubutton)
    val favBtn = view.findViewById<Button>(R.id.favBtn)
    fun render(restaurant: Restaurant, check: Boolean){
        Log.i("mika", "${restaurant.name} and ${restaurant.address} and ${restaurant.menu} and ${restaurant.imageSrc} and ${restaurant.precio}")
        restaurantName.text = restaurant.name
        menu.text = restaurant.menu
        precio.text = restaurant.precio
        val imageUri = restaurant.imageSrc
        Picasso.get().load(imageUri)
            .error(R.mipmap.ic_launcher_round)
            .into(imageSrc)
        if (check) {
            favBtn.setBackgroundResource(R.drawable.ic_favorite_red_24dp)
        } else {
            favBtn.setBackgroundResource(R.drawable.ic_favorite_shadow_24dp)
        }
    }
    fun setOnClickListeners(restaurantFavRests: ArrayList<RestaurantItem>, favDB:FavDB, check: Boolean){
        menuButton.setOnClickListener(this)
        favBtn.setOnClickListener(View.OnClickListener {
            val position = adapterPosition
            val restaurantItem = restaurantFavRests[position]
            if ((restaurantItem.favStatus == "0")) {
                restaurantItem.favStatus = "1"
                favDB!!.insertIntoDatabase(
                    restaurantItem.name.toString().trim(), restaurantItem.imageSrc.toString(),
                    restaurantItem.id, restaurantItem.favStatus.toString()
                )
                favBtn.setBackgroundResource(R.drawable.ic_favorite_red_24dp)
            } else {
                restaurantItem.favStatus = "0"
                favDB!!.remove_fav(restaurantItem.id.toString())
                favBtn.setBackgroundResource(R.drawable.ic_favorite_shadow_24dp)
            }
        })
    }

    override fun onClick(p0: View?) {
        val intent = Intent(context, RestaurantDetail::class.java);
        intent.putExtra("name", restaurantName.text)
        context.startActivity(intent);
    }
}

/*package pmn.dev.deliyou


import pmn.dev.deliyou.RestaurantItem
import androidx.recyclerview.widget.RecyclerView
import pmn.dev.deliyou.FavDB
import android.view.ViewGroup
import android.content.SharedPreferences
import android.view.LayoutInflater
import pmn.dev.deliyou.R
import android.widget.TextView
import android.database.sqlite.SQLiteDatabase
import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.view.View
import android.widget.Button
import android.widget.ImageView
import java.util.ArrayList

class RestaurantFavAdapter(
    private val restaurantItems: ArrayList<RestaurantItem>,
    private val context: Context
) : RecyclerView.Adapter<RestaurantFavAdapter.ViewHolder>() {
    private var favDB: FavDB? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        favDB = FavDB(context)
        //create table on first
        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)
        if (firstStart) {
            createTableOnFirstStart()
        }
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.restaurant_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurantItem = restaurantItems[position]
        readCursorData(restaurantItem, holder)
        holder.imageView.setImageResource(restaurantItem.imageSrc)
        holder.nameTextView.text = restaurantItem.name
    }

    override fun getItemCount(): Int {
        return 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var nameTextView: TextView
        var favBtn: Button

        init {
            imageView = itemView.findViewById(R.id.favImageView)
            nameTextView = itemView.findViewById(R.id.favTextView)
            favBtn = itemView.findViewById(R.id.favBtn)

            //add to fav btn
            favBtn.setOnClickListener(View.OnClickListener {
                val position = adapterPosition
                val restaurantItem = restaurantItems[position]
                if ((restaurantItem.favStatus == "0")) {
                    restaurantItem.favStatus = "1"
                    favDB!!.insertIntoTheDatabase(
                        restaurantItem.name.toString(), restaurantItem.imageSrc,
                        restaurantItem.favStatus.toString()
                    )
                    favBtn.setBackgroundResource(R.drawable.ic_favorite_red_24dp)
                } else {
                    restaurantItem.favStatus = "0"
                    favDB!!.remove_fav(restaurantItem.name.toString())
                    favBtn.setBackgroundResource(R.drawable.ic_favorite_shadow_24dp)
                }
            })
        }
    }

    private fun createTableOnFirstStart() {
        favDB!!.insertEmpty()
        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("firstStart", false)
        editor.apply()
    }

    private fun readCursorData(restaurantItem: RestaurantItem, viewHolder: ViewHolder) {
        val cursor: Cursor = favDB!!.read_all_data(restaurantItem.name.toString())
        val db = favDB!!.readableDatabase
        try {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") val item_fav_status = cursor.getString(
                    cursor.getColumnIndex(FavDB.FAVORITE_STATUS)
                )
                restaurantItem.favStatus = item_fav_status

                //check fav status
                if (item_fav_status != null && (item_fav_status == "1")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite_red_24dp)
                } else if (item_fav_status != null && (item_fav_status == "0")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite_shadow_24dp)
                }
            }
        } finally {
            if (cursor != null && cursor.isClosed) cursor.close()
            db.close()
        }
    }
}*/