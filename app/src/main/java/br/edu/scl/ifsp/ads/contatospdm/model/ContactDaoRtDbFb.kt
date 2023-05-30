package br.edu.scl.ifsp.ads.contatospdm.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.getValue;

class ContactDaoRtDbFb : ContactDao {
    private val CONTACT_LIST_ROOT_NODE = "contactList"
    private val contactRtDbFbReference = Firebase.database.getReference(CONTACT_LIST_ROOT_NODE)

    //Simula uma consulta no banco de dados
    private val contactList:MutableList<Contact> = mutableListOf()
    init{
        contactRtDbFbReference.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val contact: Contact? = snapshot.getValue<Contact>()
                contact?.let{_contact ->
                    if(!contactList.any{_contact.name == it.name}){
                        contactList.add(_contact)
                    }
                }

            }


            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val contact: Contact? = snapshot.getValue<Contact>()
                contact?.let{_contact ->
                        contactList[contactList.indexOfFirst { _contact.name == it.name }] = _contact
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val contact: Contact? = snapshot.getValue<Contact>()
                contact?.let{_contact ->
                    contactList.remove(_contact)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //Não se aplica
            }

            override fun onCancelled(error: DatabaseError) {
                //Não se aplica
            }
        })
        contactRtDbFbReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val  contactHashMap = snapshot.getValue<HashMap<String, Contact>>()
                contactList.clear()
                contactHashMap?.values?.forEach {
                    contactList.add(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Não se aplica
            }

        })
    }

    override fun createContact(contact: Contact) {
        createOrUpdateContact(contact)
    }

    override fun retrieveContact(id: Int): Contact? {
        //Função com código genérico porque nunca é usada, só serviu de exemplo
        return contactList[contactList.indexOfFirst{id == it.id}]
    }

    override fun retrieveContacts(): MutableList<Contact> {
        return contactList
    }

    override fun updateContact(contact: Contact): Int {
        createOrUpdateContact(contact)
        return 1 //retorna quantas linhas foram modificadas
    }

    override fun deleteContact(contact: Contact): Int {
        contactRtDbFbReference.child(contact.name).removeValue()
        return 1
    }

    private fun createOrUpdateContact(contact:Contact) = contactRtDbFbReference.child(contact.name).setValue(contact)
}