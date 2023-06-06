package br.edu.scl.ifsp.ads.contatospdm.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.edu.scl.ifsp.ads.contatospdm.databinding.ActivityCreateAccountBinding
import com.google.firebase.auth.FirebaseAuth

class CreateAccountActivity : BaseActivity() {
    private val acab: ActivityCreateAccountBinding by lazy {
        ActivityCreateAccountBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acab.root)

        acab.createAccountBt.setOnClickListener {
            val email = acab.emailEt.text.toString()
            val password = acab.passwordEt.text.toString()
            val password2 = acab.repeatPasswordEt.text.toString()

            if (password.equals(password2)) {
                //Cria a conta no firebase
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@CreateAccountActivity,
                            "Usuário $email criado com sucesso!",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(
                            this@CreateAccountActivity,
                            "Erro na criação do usuário!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            } else {
                //Senhas não batem
                Toast.makeText(
                    this@CreateAccountActivity,
                    "Senhas não coincidem!",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }
}