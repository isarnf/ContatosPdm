package br.edu.scl.ifsp.ads.contatospdm.view

import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.ads.contatospdm.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

sealed class BaseActivity: AppCompatActivity(){
    protected val EXTRA_CONTACT = "Contact"
    protected val EXTRA_VIEW_CONTACT = "ViewContact"

    protected val googleSignInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    //mantem as informacoes de login uma vez que ele é realizado
    protected val googleSignInClient: GoogleSignInClient by lazy{
        GoogleSignIn.getClient(this, googleSignInOptions)
    }
}
