package pmn.dev.deliyou

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.cardview.widget.CardView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

class HomeScreen : AppCompatActivity() {
    private lateinit var restaurantRecyclerView: RecyclerView
    private lateinit var restaurantArrayList: ArrayList<Restaurant>
    private lateinit var myAdapter: RestaurantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        restaurantRecyclerView = findViewById(R.id.restaurantList)
        restaurantRecyclerView.layoutManager = LinearLayoutManager(this)
        restaurantRecyclerView.setHasFixedSize(true)

        restaurantArrayList = arrayListOf<Restaurant>()
        myAdapter = RestaurantAdapter(restaurantArrayList)
        restaurantRecyclerView.adapter = myAdapter
        getRestaurantData()


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getRestaurantData(){
        var lista = arrayListOf<Restaurant>()
        val db = Firebase.firestore
        db.collection("Restaurants")
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    val restaurant = document.toObject(Restaurant::class.java)
                    Log.i("MyActivity", "${restaurant.name}")
                    restaurantArrayList.add(restaurant)
                }
                myAdapter.notifyDataSetChanged()
            }.addOnFailureListener{
                Log.i("MyActivity", "Algo fue mal")
            }
        for(restaurant in lista){
            Log.i("mika", "$restaurant")
            Log.i("mika", "${restaurant.name} and ${restaurant.address} and ${restaurant.menu}")
        }

    }
}
