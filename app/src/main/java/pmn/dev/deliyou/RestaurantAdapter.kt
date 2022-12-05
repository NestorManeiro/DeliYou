package pmn.dev.deliyou

import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RestaurantAdapter(private val RestaurantList : ArrayList<Restaurant>) : RecyclerView.Adapter<RestaurantViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        for(res in RestaurantList){
            Log.i("mika", "${res.name} and ${res.address} and ${res.menu}")
        }
        return RestaurantViewHolder(layoutInflater.inflate(R.layout.restaurant_item, parent, false))
    }

    override fun getItemCount(): Int = RestaurantList.size

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val item = RestaurantList[position]
        holder.render(item)
    }

}

class RestaurantViewHolder(view: View): RecyclerView.ViewHolder(view){
    val restaurantName = view.findViewById<TextView>(R.id.restaurantName)
    val address = view.findViewById<TextView>(R.id.restaurantAddress)
    fun render(restaurant: Restaurant){
        Log.i("mika", "${restaurant.name} and ${restaurant.address} and ${restaurant.menu}")
        restaurantName.text = restaurant.name
        address.text = restaurant.address
    }
}