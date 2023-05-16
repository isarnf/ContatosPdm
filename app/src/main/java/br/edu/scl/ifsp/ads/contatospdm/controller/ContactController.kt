package br.edu.scl.ifsp.ads.contatospdm.controller

import androidx.room.Room
import br.edu.scl.ifsp.ads.contatospdm.model.Contact
import br.edu.scl.ifsp.ads.contatospdm.model.ContactDao
import br.edu.scl.ifsp.ads.contatospdm.model.ContactDaoRoom
import br.edu.scl.ifsp.ads.contatospdm.model.ContactDaoRoom.Constants.CONTACT_DATABASE_FILE
import br.edu.scl.ifsp.ads.contatospdm.model.ContactDaoSqlite
import br.edu.scl.ifsp.ads.contatospdm.view.MainActivity

class ContactController(private val mainActivity: MainActivity) { //controller específico da Main Activity
    //private val contactDaoImpl: ContactDao = ContactDaoSqlite(mainActivity)
    private val contactDaoImpl: ContactDao = Room.databaseBuilder(
        mainActivity, ContactDaoRoom::class.java, CONTACT_DATABASE_FILE
    ).build().getContactDao()

    fun insertContact(contact: Contact) {
        Thread {
            contactDaoImpl.createContact(contact)
            val list = contactDaoImpl.retrieveContacts()
            mainActivity.runOnUiThread {
                mainActivity.updateContactList(list)
            }
        }.start()

    }
    fun getContact(id:Int) = contactDaoImpl.retrieveContact(id)
    fun getContacts() {
        Thread {
            val list = contactDaoImpl.retrieveContacts()
            mainActivity.runOnUiThread {
                mainActivity.updateContactList(list)
            }
        }.start()
    }
    fun editContact(contact: Contact) = contactDaoImpl.updateContact(contact)
    fun removeContact(contact: Contact) = contactDaoImpl.deleteContact(contact)


}