package pmn.dev.deliyou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RestaurantAdapter(private val RestaurantList : ArrayList<Restaurant>) : RecyclerView.Adapter<RestaurantViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        for(res in RestaurantList){
            Log.i("mika", "${res.name} and ${res.address} and ${res.menu} and ${res.imageSrc} and ${res.precio}'")
        }
        return RestaurantViewHolder(layoutInflater.inflate(R.layout.restaurant_item, parent, false))
    }

    override fun getItemCount(): Int = RestaurantList.size

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val item = RestaurantList[position]
        holder.setOnClickListeners();
        holder.render(item)
    }
}

class RestaurantViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
    val context = view.context
    val restaurantName = view.findViewById<TextView>(R.id.restaurantName)
    val menu = view.findViewById<TextView>(R.id.restaurantMenu)
    val precio = view.findViewById<TextView>(R.id.restaurantPrice)
    val imageSrc = view.findViewById<ImageView>(R.id.restaurantImgSrc)
    val menuButton = view.findViewById<Button>(R.id.menubutton)

    fun render(restaurant: Restaurant){
        Log.i("mika", "${restaurant.name} and ${restaurant.address} and ${restaurant.menu} and ${restaurant.imageSrc} and ${restaurant.precio}")
        restaurantName.text = restaurant.name
        menu.text = restaurant.menu
        precio.text = restaurant.precio
        val imageUri = restaurant.imageSrc
        Picasso.get().load(imageUri)
            .error(R.mipmap.ic_launcher_round)
            .into(imageSrc)
    }
    fun setOnClickListeners(){
        menuButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val intent = Intent(context, RestaurantDetail::class.java);
        intent.putExtra("name", restaurantName.text)
        context.startActivity(intent);
    }
}