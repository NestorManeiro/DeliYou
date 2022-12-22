package pmn.dev.deliyou

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ProductAdapter (private val restaurantFoodList: ArrayList<Product>) : RecyclerView.Adapter<ProductViewHolder>(){
    private var cartDB: CartDB? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        cartDB = CartDB(parent.context)
        val layoutInflater = LayoutInflater.from(parent.context)
        //var foodId = 1
        Log.i("tsuneo", "No entra no entra 1")
        /*Log.i("sisuka", "No entra no entra")
        for (food in foodList) {
            val foodArr = food.split(",")
            val foodType = Product(foodArr[0], foodArr[1], foodArr[2], foodId.toString())
            Log.i("luca","$foodArr")
            foodId++
            restaurantFoodList.add(foodType)
        }*/
        val prefs = parent.context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)

        if (firstStart) {
            createTableOnFirstStart(parent.context)
        }

        return ProductViewHolder(layoutInflater.inflate(R.layout.product_menu_item, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        Log.i("tsuneo", "No entra no entra 2")
        val item = restaurantFoodList[position]
        holder.render(item)
        holder.setOnClickListeners(restaurantFoodList, cartDB!!)
    }

    private fun createTableOnFirstStart(context: Context) {
        Log.i("tsuneo", "No entra no entra 3")
        cartDB!!.insertEmpty()
        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("firstStart", false)
        editor.apply()
    }

    override fun getItemCount(): Int {
        Log.i("tsuneo", "${restaurantFoodList.size}")

        return restaurantFoodList.size
    }
}

class ProductViewHolder (view: View): RecyclerView.ViewHolder(view), View.OnClickListener {

    val context = view.context
    val productName = view.findViewById<TextView>(R.id.productTextView)
    val productPrice = view.findViewById<TextView>(R.id.priceTextView)
    val quantity = view.findViewById<TextView>(R.id.quantityTextView)
    val addToCartBtn = view.findViewById<Button>(R.id.addToCartBtn)
    var amount = 0
    val productImg = view.findViewById<ImageView>(R.id.productImageView)

    fun render(item: Product) {
        Log.i("tsuneo", "No entra no entra 5")
        productName.text = item.name
        productPrice.text = item.price + "€"
        quantity.text = amount.toString()

        val imageUri = item.imageSrc
        Picasso.get().load(imageUri)
            .error(R.mipmap.ic_launcher_round)
            .into(productImg)
    }

    fun setOnClickListeners(foodList: ArrayList<Product>, cartDB: CartDB){
        Log.i("tsuneo", "No entra no entra 6")
        addToCartBtn.setOnClickListener {
            val position = adapterPosition
            val foodItem = foodList[position]
            if (amount == 0) {
                cartDB.insertIntoDatabase(
                    foodItem.id.toString(),
                    foodItem.name.toString(), foodItem.imageSrc, foodItem.price.toString(), "3"
                )
            } else {
                cartDB.updateDatabase(foodItem.id.toString(), "3")
            }

            amount += 1
            quantity.text = "x" + amount.toString()
        }
    }

    override fun onClick(p0: View?) {
        Log.i("tsuneo", "No entra no entra 7")
        amount++
        quantity.text = amount.toString()
    }

}