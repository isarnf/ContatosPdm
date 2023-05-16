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
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.scl.ifsp.ads.contatospdm.R
import br.edu.scl.ifsp.ads.contatospdm.adapter.ContactAdapter
import br.edu.scl.ifsp.ads.contatospdm.adapter.ContactRvAdapter
import br.edu.scl.ifsp.ads.contatospdm.adapter.OnContactClickListener
import br.edu.scl.ifsp.ads.contatospdm.controller.ContactController
import br.edu.scl.ifsp.ads.contatospdm.databinding.ActivityMainBinding
import br.edu.scl.ifsp.ads.contatospdm.model.Contact

class MainActivity : BaseActivity(), OnContactClickListener {
    private val amb: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Data source
    private val contactList: MutableList<Contact> = mutableListOf()

    // Adapter
    private val contactAdapter: ContactRvAdapter by lazy {
        ContactRvAdapter(contactList, this)
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
        amb.contactRv.layoutManager =
            LinearLayoutManager(this) // informa o listView qual é o adapter que ele irá utilizar
        amb.contactRv.adapter = contactAdapter

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
                        Toast.makeText(this, "Contato adicionado!", Toast.LENGTH_LONG).show()
                    }
                    contactController.getContacts()
                    contactAdapter.notifyDataSetChanged()
                }
            }
        }

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

    fun updateContactList(_contactList: MutableList<Contact>){
        contactList.clear()
        contactList.addAll(_contactList)
        contactAdapter.notifyDataSetChanged()
    }

    // Funções que serão chamadas sempre que uma célula for clicada no RecyclerView
    // A associação entre uma célula e função será feita no ContactRvAdapter
    override fun onTileContactClick(position: Int) {
        val contact = contactList[position]
        val contactIntent = Intent(this@MainActivity, ContactActivity::class.java)
        contactIntent.putExtra(EXTRA_CONTACT, contact)
        contactIntent.putExtra(EXTRA_VIEW_CONTACT, true)
        startActivity(contactIntent)
    }

    override fun onEditMenuIconClick(position: Int) {
        val contact = contactList[position]
        val contactIntent= Intent(this, ContactActivity::class.java)
        contactIntent.putExtra(EXTRA_CONTACT, contact)
        carl.launch(contactIntent)
    }

    override fun onRemoveMenuItemClick(position: Int) {
        val contact = contactList[position]
        contactList.removeAt(position)
        contactController.removeContact(contact)
        contactAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Contato removido!", Toast.LENGTH_LONG).show()
    }
}