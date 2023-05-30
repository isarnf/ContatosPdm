package br.edu.scl.ifsp.ads.contatospdm.adapter

// Interface que será implementada pela MainActivity para tratar eventos de clique nas células de
// RecyclerView e que será usada pelo ContactRvAdapter para tratar os eventos de clique individualmente

interface OnContactClickListener {

    fun onTileContactClick(position : Int)

    fun onEditMenuIconClick(position : Int)

    fun onRemoveMenuItemClick(position : Int)

}