package br.edu.scl.ifsp.ads.contatospdm.model

import androidx.room.*

@Dao
interface ContactDao {

    @Insert
    fun createContact(contact: Contact)

    @Query("SELECT * FROM Contact WHERE id = :id")
    fun retrieveContact(id: Int): Contact?

    @Query("SELECT * FROM Contact")
    fun retrieveContacts(): MutableList<Contact>

    @Update
    fun updateContact(contact: Contact): Int

    @Delete
    fun deleteContact(contact: Contact): Int
}