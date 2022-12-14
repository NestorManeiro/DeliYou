package pmn.dev.deliyou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.form_restaurant.*

class form_restaurantdb : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form_restaurant)

        val db = FirebaseFirestore.getInstance()

        enviarformulario.setOnClickListener{
            db.collection("Peticiones").document(restaurantNameForm.text.toString()).set(
                hashMapOf(
                "name" to restaurantNameForm.text.toString(),
                "address" to restaurantAddressForm.text.toString(),
                "imageSrc" to restaurantImageSrcForm.text.toString(),
                "menu" to restaurantMenuForm.text.toString(),
                "precio" to restaurantPriceForm.text.toString()),
            )
        }
    }



}