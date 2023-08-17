package com.example.restaurante.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurante.R
import com.example.restaurante.data.preference.SharedPreferences
import com.example.restaurante.data.room.entity.Categoria
import com.example.restaurante.data.room.entity.Producto
import com.example.restaurante.domain.viewmodel.CategoriaViewModel
import com.example.restaurante.domain.viewmodel.ProductoViewModel
import com.example.restaurante.presentation.catalogo.Details.DetalleProductoActivity
import com.example.restaurante.presentation.catalogo.ListProductosAdapter
import com.example.restaurante.presentation.perfil.PerfilUsuarioActivity
import kotlinx.android.synthetic.main.activity_list_productos.view.btnMenu
import kotlinx.android.synthetic.main.activity_list_productos.view.btnPerfil
import kotlinx.android.synthetic.main.activity_list_productos.view.rvCategoria
import kotlinx.android.synthetic.main.activity_list_productos.view.rvProducto
import kotlinx.android.synthetic.main.activity_list_productos.view.tvUsuario

class ListProductosFragment : Fragment(), ListProductosAdapter.ICard, ListProductosCategoriaAdapter.ICard {
    private lateinit var productoAdapter : ListProductosAdapter
    private lateinit var categoriaAdapter: ListProductosCategoriaAdapter
    private lateinit var viewModelProducto: ProductoViewModel
    private lateinit var viewModelCategoria: CategoriaViewModel
    private var lstProductos : MutableList<Producto> = ArrayList()
    private var lstCategoria : MutableList<Categoria> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_list_productos,container,false)
        initValues(view)
        initObservers()
        return view
    }

    private fun initValues(view: View) {
        viewModelCategoria = ViewModelProvider(this)[CategoriaViewModel::class.java]
        viewModelProducto = ViewModelProvider(this)[ProductoViewModel::class.java]
        setNombreUsuario(view)

        categoriaAdapter = ListProductosCategoriaAdapter(lstCategoria, this)
        view.rvCategoria.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        view.rvCategoria.adapter = categoriaAdapter

        productoAdapter = ListProductosAdapter(lstProductos, this)
        view.rvProducto.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        view.rvProducto.adapter=productoAdapter

        view.btnPerfil.setOnClickListener {
            startActivity(Intent(activity, PerfilUsuarioActivity::class.java))
        }
        view.btnMenu.setOnClickListener{
            // Accion temporal para mostrar Detalle Producto
            startActivity(Intent(requireContext(), DetalleProductoActivity::class.java))
        }

//        view.btn_opc_promociones.setOnClickListener {
//            // Accion temporal para Cerrar sesion
//            SharedPreferences.deletePrefUsuario(requireContext())
//            val intent = Intent(requireContext(), LoginActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//        }
//
//        view.btn_opc_parrillas.setOnClickListener {
//            startActivity(Intent(requireContext(), ConfirmacionActivity::class.java))
//        }
    }

    private fun initObservers(){
        viewModelCategoria.getCategorias.observe(viewLifecycleOwner){
            categoriaAdapter.update(it)
        }
        viewModelProducto.getProductos.observe(viewLifecycleOwner){
            productoAdapter.update(it)
        }
        viewModelCategoria.getCategorias()
        viewModelProducto.obtenerProductos()
    }

    private fun setNombreUsuario(view : View){
        val nombre = SharedPreferences.getPrefUsuario(requireContext())!!.nom_usuario
        view.tvUsuario.text = nombre
    }

    override fun onCardClick(item: Producto) {
        startActivity(Intent(activity, DetalleProductoActivity::class.java).apply {
            putExtra("id_producto", item.id_producto)
        })
    }

    override fun onCardClick(item: Categoria) {
        TODO("Not yet implemented")
    }
}
