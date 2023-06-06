package br.edu.scl.ifsp.ads.contatospdm.controller

import android.os.Message
import androidx.room.Room
import br.edu.scl.ifsp.ads.contatospdm.model.*
import br.edu.scl.ifsp.ads.contatospdm.model.ContactDaoRoom.Constants.CONTACT_DATABASE_FILE
import br.edu.scl.ifsp.ads.contatospdm.view.MainActivity

class ContactController(private val mainActivity: MainActivity) { //controller específico da Main Activity
    //private val contactDaoImpl: ContactDao = ContactDaoSqlite(mainActivity)
//    private val contactDaoImpl: ContactDao = Room.databaseBuilder(
//        mainActivity, ContactDaoRoom::class.java, CONTACT_DATABASE_FILE
//    ).build().getContactDao()
    private val contactDaoImpl: ContactDao = ContactDaoRtDbFb()

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
    fun editContact(contact: Contact) {
        Thread{
            contactDaoImpl.updateContact(contact)
        }.start()
    }
    fun removeContact(contact: Contact) {
        Thread{
            contactDaoImpl.deleteContact(contact)
        }.start()
    }


}