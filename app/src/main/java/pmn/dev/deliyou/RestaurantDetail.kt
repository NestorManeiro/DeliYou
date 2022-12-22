package pmn.dev.deliyou

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
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
import kotlinx.android.synthetic.main.activity_restaurant_detail.*
import kotlin.collections.ArrayList

class RestaurantDetail : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var recyclerView: RecyclerView
    private var cartDB: CartDB? = null
    private lateinit var detailAdapter: ProductAdapter
    val products = arrayListOf<Product>()


    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)

        val extras = intent.extras
        val value = extras?.getString("name")

        val food = extras?.getStringArrayList("food")


        if (food != null) {
            var foodId = 1
            for (f in food) {
                val fArr = f.split(",")
                val fProduct = Product(fArr[0], fArr[1], fArr[2], foodId.toString())
                foodId++
                products.add(fProduct)
            }
        }

        Log.i("peluca", "$food")


        val name = findViewById<TextView>(R.id.textView2)
        name.text = value

        //------------------------------------------------------------------------------------------


        cartDB = CartDB(applicationContext)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        recyclerView = findViewById(R.id.product_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        detailAdapter = ProductAdapter(products)
        recyclerView.adapter = detailAdapter

        Log.i("gigante", "$products")
        val db = Firebase.firestore
        buyBtn.setOnClickListener() {

            val cursor = cartDB!!.getData()
            while (cursor.moveToNext()) {
                Log.i("paputo", "${cursor.getString(4)}")
                val data = hashMapOf(

                    "name" to cursor.getString(2),
                    "precio" to cursor.getString(3),
                    "amount" to cursor.getString(4)
                )
                db.collection("Pedidos")
                    .add(data)
                    .addOnSuccessListener { documentReference ->
                        Log.d("TAG", "DocumentSnapshot written with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("TAG", "Error adding document", e)
                    }
            }
            Toast.makeText(this,"Compra realizada con exito",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
            finish();
        }

        detailAdapter.notifyDataSetChanged()
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
            R.id.nav_item_favoritos -> Toast.makeText(this, "Favoritos", Toast.LENGTH_SHORT).show()
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
                        Log.d(ContentValues.TAG, "OK! Works fine!")
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Log.w(ContentValues.TAG, "Something is wrong!")
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