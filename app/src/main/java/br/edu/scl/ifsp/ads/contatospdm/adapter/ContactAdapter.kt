package br.edu.scl.ifsp.ads.contatospdm.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.scl.ifsp.ads.contatospdm.R
import br.edu.scl.ifsp.ads.contatospdm.databinding.TileContactBinding
import br.edu.scl.ifsp.ads.contatospdm.model.Contact

class ContactAdapter(
    context: Context,
    private val contactList: MutableList<Contact>
): ArrayAdapter<Contact> (context, R.layout.tile_contact, contactList){
    private lateinit var tcb: TileContactBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contact: Contact = contactList[position]
        var tileContactView = convertView
        if(tileContactView == null){
            //infla uma nova célula
            tcb = TileContactBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false

            )
            tileContactView = tcb.root

            //criando um holder e guardando referência para os TextViews
            val tcvh: TileContactViewHolder = TileContactViewHolder(
//                tileContactView.findViewById(R.id.nameTv),
//                tileContactView.findViewById(R.id.emailTv)
                tcb.nameTv,
                tcb.emailTv

            )

            //armazenando um ViewHolder na célula
            tileContactView.tag = tcvh
        }

        // substituir os valores
        with(tileContactView.tag as TileContactViewHolder){
            nomeTv.text = contact.name
            emailTv.text = contact.email
        }

        return tileContactView
    }

    private data class TileContactViewHolder(
        val nomeTv: TextView,
        val emailTv: TextView
    )

}