package br.edu.scl.ifsp.ads.contatospdm.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.scl.ifsp.ads.contatospdm.R
import br.edu.scl.ifsp.ads.contatospdm.adapter.ContactAdapter
import br.edu.scl.ifsp.ads.contatospdm.controller.ContactController
import br.edu.scl.ifsp.ads.contatospdm.databinding.ActivityMainBinding
import br.edu.scl.ifsp.ads.contatospdm.model.Contact

class MainActivity : BaseActivity() {
    private val amb: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Data source
    private val contactList: MutableList<Contact> = mutableListOf()

    // Adapter
    private val contactAdapter: ContactAdapter by lazy {
        ContactAdapter(
            this, contactList
        )
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    //Controller
    private val contactController: ContactController by lazy{
        ContactController(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        supportActionBar?.subtitle = getString(R.string.contact_list)
        contactController.getContacts()
        //fillContactList()
        amb.contactsLv.adapter =
            contactAdapter // informa o listView qual é o adapter que ele irá utilizar

        carl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val contact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(EXTRA_CONTACT, Contact::class.java)
                } else {
                    result.data?.getParcelableExtra(EXTRA_CONTACT)
                }
                contact?.let { _contact ->
                    //if(contactList.any{it.id == _contact.id}){ //any retorna verdaeiro se pelo menos um elemento casa com o predicado (comparacao)
                    val position = contactList.indexOfFirst { it.id == _contact.id } //retorna o indice(index) caso o predicado seja satisfeito, ou seja, caso algum contato da minha lista tenha um id igual
                    if (position != -1) {
                        contactList[position] = _contact
                        contactController.editContact(_contact)
                        Toast.makeText(this, "Contato editado!", Toast.LENGTH_LONG).show()
                    } else {
                        contactController.insertContact(_contact)
                        contactController.getContacts()
                        Toast.makeText(this, "Contato adicionado!", Toast.LENGTH_LONG).show()
                    }
                    contactAdapter.notifyDataSetChanged()
                }
            }
        }


        registerForContextMenu(amb.contactsLv)

        //listener para clique rápido nos itens de lista
        amb.contactsLv.setOnItemClickListener(object: AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ){
                val contact = contactList[position]
                val contactIntent = Intent(this@MainActivity, ContactActivity::class.java)
                contactIntent.putExtra(EXTRA_CONTACT, contact)
                contactIntent.putExtra(EXTRA_VIEW_CONTACT, true)
                startActivity(contactIntent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        return when(item.itemId){
            R.id.addContactMi -> {
                carl.launch(Intent(this, ContactActivity::class.java))
                true
            }
            else -> false
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterContextMenuInfo).position
        val contact = contactList[position]

        return when (item.itemId){
            R.id.removeContactMi -> {
                contactList.removeAt(position)
                contactController.removeContact(contact)
                contactAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Contato removido!", Toast.LENGTH_LONG).show()
                true
            }
            R.id.editContactMi -> {

                val contactIntent= Intent(this, ContactActivity::class.java)
                contactIntent.putExtra(EXTRA_CONTACT, contact)
                carl.launch(contactIntent)

                true
            }
            else -> { false }
        }
    }

    // Função para preencher nosso data source; será apagada no futuro
//    private fun fillContactList(){
//        for(index in 1..50){
//            contactList.add(
//                Contact(
//                    index,
//                    "Nome $index",
//                    "Endereço $index",
//                    "Telefone $index",
//                    "Email $index"
//
//                )
//            )
//        }
//    }

    fun updateContactList(_contactList: MutableList<Contact>){
        contactList.clear()
        contactList.addAll(_contactList)
        contactAdapter.notifyDataSetChanged()
    }

}