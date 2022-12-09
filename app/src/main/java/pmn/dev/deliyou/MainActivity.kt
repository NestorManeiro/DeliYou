package pmn.dev.deliyou

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

enum class ProviderType{
    BASIC,
    GOOGLE,
    APPLE,
}
@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private val GOOGLE_SIGN_IN = 100;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setup(email ?:"", provider ?: "")

        //Guardar datos
        val prefs: SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email",email)
        prefs.putString("provider",provider)
        prefs.apply()
    }


    private fun setup(email:String, provider:String){

        logintxt.setOnClickListener{
            val nextpage = Intent( this, LoginActivity::class.java);
            startActivity(nextpage);
            finish();
        }

        registertxt.setOnClickListener{
            val nextpage = Intent( this, RegisterActivity::class.java);
            startActivity(nextpage);
            finish();
        }

        googleSign.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleClient = GoogleSignIn.getClient(this,googleConf)

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode,resultCode,data)

        if(requestCode==GOOGLE_SIGN_IN){
            val task= GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account=task.getResult(ApiException::class.java);

                if(account !=null){
                    val credential= GoogleAuthProvider.getCredential(account.idToken,null)

                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {

                        if (it.isSuccessful) {
                            Toast.makeText(this,"Logueado", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainPage::class.java)
                            startActivity(intent)
                            finish();
                        } else {
                            Toast.makeText(this, "Error en el login", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            } catch (e: ApiException){
                Toast.makeText(this, "Error en el login", Toast.LENGTH_SHORT).show()
            }
        }
    }
}