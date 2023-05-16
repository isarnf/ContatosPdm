package br.edu.scl.ifsp.ads.contatospdm.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Contact::class], version = 1)
abstract class ContactDaoRoom: RoomDatabase() {

    companion object Constants {
        const val CONTACT_DATABASE_FILE = "contacts_room"
    }
    abstract fun getContactDao(): ContactDao

}