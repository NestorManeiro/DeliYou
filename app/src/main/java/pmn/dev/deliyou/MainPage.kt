package pmn.dev.deliyou

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainPage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var restaurantRecyclerView: RecyclerView
    private lateinit var restaurantArrayList: ArrayList<Restaurant>
    private lateinit var myAdapter: RestaurantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page2)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)


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
                    Log.i("MyActivity", "${restaurant.Comidas}")
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.inicio ->{
                val nextpage = Intent( this, MainPage::class.java);
                startActivity(nextpage);
                finish();
            }
            R.id.nav_item_pedidos -> {
                val nextpage = Intent( this, pedidos::class.java);
                startActivity(nextpage);
                finish();
            }
            R.id.nav_item_favoritos -> {
                Toast.makeText(this, "Favoritos", Toast.LENGTH_SHORT).show()
                val nextpage = Intent(this, Favoritos2::class.java);
                startActivity(nextpage)
                finish()
            }

            R.id.log_out -> {
                val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                val googleConf= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

                val googleClient= GoogleSignIn.getClient(this,googleConf);
                googleClient.signOut()


                val nextpage = Intent( this, MainActivity::class.java);
                startActivity(nextpage);
                finish();
            }
            R.id.deleteaccount -> {
                val firebaseAuth = FirebaseAuth.getInstance()
                val currentUser = firebaseAuth.currentUser
                currentUser!!.delete().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "OK! Works fine!")
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Log.w(TAG, "Something is wrong!")
                    }
                }
            }
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}