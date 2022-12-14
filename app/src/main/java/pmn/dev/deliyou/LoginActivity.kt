package pmn.dev.deliyou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login.setOnClickListener{
            if(restaurantAddressForm.text.isNullOrBlank() || restaurantNameForm.text.isNullOrBlank()){
                Toast.makeText(this,"Porfavor rellena los datos necesarios",Toast.LENGTH_SHORT).show()
            } else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(restaurantNameForm.text.toString()
                    ,restaurantAddressForm.text.toString()).addOnCompleteListener{
                        if(it.isSuccessful){
                            Toast.makeText(this,"Logueado :D",Toast.LENGTH_SHORT).show()
                            //Si se pone MainPage se verá el menú hamburguesa si se pone HomeScreen se verá la home.
                            val intent = Intent(this@LoginActivity, MainPage::class.java)
                            startActivity(intent)
                            finish();
                        } else {
                            Toast.makeText(this,"Error en el login",Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }

}