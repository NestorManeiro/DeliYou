package pmn.dev.deliyou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registro.setOnClickListener{
            if(restaurantAddressForm.text.isNullOrBlank() || restaurantNameForm.text.isNullOrBlank()){
                Toast.makeText(this,"Porfavor rellena los datos necesarios",Toast.LENGTH_SHORT).show()
            } else {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(restaurantNameForm.text.toString()
                    ,restaurantAddressForm.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this,"Registrado con exito",Toast.LENGTH_SHORT).show()
                        val nextpage = Intent( this, LoginActivity::class.java);
                        startActivity(nextpage);
                        finish();
                    } else {
                        Toast.makeText(this,"E-mail ya registrado o error en el registro",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}