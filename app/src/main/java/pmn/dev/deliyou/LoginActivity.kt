package pmn.dev.deliyou

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
            if(editTextPass.text.isNullOrBlank() || editTextMail.text.isNullOrBlank()){
                Toast.makeText(this,"Porfavor rellena los datos necesarios",Toast.LENGTH_SHORT).show()
            } else {

                FirebaseAuth.getInstance().signInWithEmailAndPassword(editTextMail.text.toString()
                    ,editTextPass.text.toString()).addOnCompleteListener{
                        if(it.isSuccessful){
                            Toast.makeText(this,"Logueado :D",Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this,"Error en el login",Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }

}