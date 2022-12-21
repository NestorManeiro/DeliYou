package pmn.dev.deliyou

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
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
import java.util.ArrayList

class Favoritos2 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var recyclerView: RecyclerView
    private var favDB: FavDB? = null
    private val favItemList: ArrayList<FavItem>? = ArrayList()
    private var favAdapter: FavAdapter? = null

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favs_list)
        favDB = FavDB(applicationContext)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        
        recyclerView = findViewById(R.id.restaurantFavList)
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        loadData()
    }

    @SuppressLint("Range")
    private fun loadData() {
        favItemList!!.clear()
        val db = favDB!!.readableDatabase
        val cursor = favDB!!.select_all_favorite_list()
        try {
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex(FavDB.ITEM_NAME))
                val image = cursor.getString(cursor.getColumnIndex(FavDB.ITEM_IMAGE))
                val id = cursor.getString(cursor.getColumnIndex(FavDB.KEY_ID))
                val favItem = FavItem(name, image, id)
                favItemList!!.add(favItem)
            }
        } finally {
            if (cursor != null && cursor.isClosed) cursor.close()
            db.close()
        }
        favAdapter = FavAdapter(favItemList!!)
        recyclerView!!.adapter = favAdapter
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