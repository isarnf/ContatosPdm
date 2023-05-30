package br.edu.scl.ifsp.ads.contatospdm.adapter

import android.content.Context
import android.view.*
import android.view.View.OnCreateContextMenuListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.scl.ifsp.ads.contatospdm.databinding.TileContactBinding
import br.edu.scl.ifsp.ads.contatospdm.model.Contact

class ContactRvAdapter(
    private val contactList: MutableList<Contact>, // data service
    private val onContactClickListener: OnContactClickListener //objeto que implementa as funções de clique
) : RecyclerView.Adapter<ContactRvAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(tileContactBinding: TileContactBinding) :
        RecyclerView.ViewHolder(tileContactBinding.root), OnCreateContextMenuListener {
        val nameTv: TextView = tileContactBinding.nameTv
        val emailTv: TextView = tileContactBinding.emailTv
        var contactPosition = -1 // Para saber qual célula foi clicada

        init { // serve para dizer que o criador do menu de contexto da minha célula é o próprio view holder
            tileContactBinding.root.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu?.add(Menu.NONE, 0, 0, "Editar")?.setOnMenuItemClickListener {
                if (contactPosition != -1) {
                    onContactClickListener.onEditMenuIconClick(contactPosition)

                }
                true
            }
            menu?.add(Menu.NONE, 1, 1, "Remover")?.setOnMenuItemClickListener {
                if (contactPosition != -1) {
                    onContactClickListener.onRemoveMenuItemClick(contactPosition)

                }
                true
            }
        }
    }

    // Chamada pelo LayoutManager para buscar a quantidade de dados e preparar a quantidade de células necessárias
    // Retirna a quantidade de dados que tenho no data service
    // Chamada inicialmente quando a tela estiver vazia e quando modificamos a quantidade de itens no data service
    override fun getItemCount(): Int = contactList.size

    // Chamada pelo LayoutManager para criar uma nova célula (E consequentemente um novo ViewHolder)
    // Retorna o ViewHolder e não a view
    // Se a célula já existe, essa função não é chamada (neste caso quem é chamada é a onBindViewHolder)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        // Criar a célula;
        val tileContactBinding = TileContactBinding.inflate(LayoutInflater.from(parent.context))

        // Criar um ViewHolder usando a célula
        val contactViewHolder = ContactViewHolder(tileContactBinding)

        // Retorna o ViewHolder
        return contactViewHolder
    }

    // Chamada pelo LayoutManager para alterar os valores de uma célula
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        //Busca o contato pela posição no data service
        val contact = contactList[position]

        // Altera os valores da célula usando o ViewHolder
        holder.nameTv.text = contact.name
        holder.emailTv.text = contact.email
        holder.contactPosition = position

        // Tratamento da visualização das informações de cada célula ao clicar sobre a célula
        holder.itemView.setOnClickListener{
            onContactClickListener.onTileContactClick(position)
        }
    }

}












