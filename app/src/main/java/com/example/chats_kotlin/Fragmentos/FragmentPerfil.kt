package com.example.chats_kotlin.Fragmentos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chats_kotlin.OpcionesLoginActivity
import com.example.chats_kotlin.R
import com.example.chats_kotlin.databinding.FragmentPerfilBinding
import com.google.firebase.auth.FirebaseAuth


class FragmentPerfil : Fragment() {
    private lateinit var binding: FragmentPerfilBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mContext: Context

    override  fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPerfilBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        // Boton cerrar sesion
        binding.btnCerrarSesion.setOnClickListener{
            firebaseAuth.signOut()
            startActivity(Intent(context, OpcionesLoginActivity::class.java))
            activity?.finishAffinity()
        }
    }
}