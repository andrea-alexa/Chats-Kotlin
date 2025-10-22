package com.example.chats_kotlin

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chats_kotlin.databinding.ActivityOpcionesLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class OpcionesLoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOpcionesLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    //Indicamos un progres dialog
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mGoogleSignInClient:GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOpcionesLoginBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        comprobarSesion()

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.opcionEmail.setOnClickListener{
            startActivity(Intent(applicationContext, LoginEmailActivity::class.java))
        }

        //evento ingresar con google
        binding.opcionGoogle.setOnClickListener{
            iniciarGoogle()
        }
    }

    private fun iniciarGoogle() {
        val googleSignIntent = mGoogleSignInClient.signInIntent
    }

    private val googleSignInActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK){
                val data = resultado.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)

                try {
                    val cuenta = task.getResult(ApiException::class.java)
                    autenticarCuentaGoogle(cuenta.idToken)
                }
                catch (e:Exception){
                    Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this, "Error al iniciar sesión con Google", Toast.LENGTH_SHORT).show()
            }
        }

    private fun autenticarCuentaGoogle(idToken: String?) {
        val credencial = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credencial)
                .addOnSuccessListener { authResultado ->
                    //Condicion para comprobar el user
                    if (authResultado.additionalUserInfo!!.isNewUser){
                        actualizarUsuario()
                    }
                    else{
                        startActivity(Intent(this, MainActivity::class.java))
                        finishAffinity()
                    }
                }
                .addOnFailureListener{e->
                    Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
                }
        }


    private fun comprobarSesion() {
        if(firebaseAuth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }

    }
}

