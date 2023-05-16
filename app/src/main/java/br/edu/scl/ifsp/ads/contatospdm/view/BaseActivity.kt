package br.edu.scl.ifsp.ads.contatospdm.view

import androidx.appcompat.app.AppCompatActivity

sealed class BaseActivity: AppCompatActivity(){
    protected val EXTRA_CONTACT = "Contact"
    protected val EXTRA_VIEW_CONTACT = "ViewContact"
}
