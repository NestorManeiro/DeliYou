package pmn.dev.deliyou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class RestaurantDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)

        val extras = intent.extras
        val value = extras?.getString("name")

        val name = findViewById<TextView>(R.id.textView2)
        name.text = value
    }
}