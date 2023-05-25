package com.example.proiectandroid

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.proiectandroid.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var user: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    )   : View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val btn : Button = view.findViewById(R.id.button)
        val textUser : TextView = view.findViewById(R.id.user)


        user = FirebaseAuth.getInstance()
        if(user.currentUser != null){
            textUser.text = user.currentUser?.email
        }

        btn.setOnClickListener{
            user.signOut()
            val intent = Intent (getActivity(), SignInActivity::class.java)
            getActivity()?.startActivity(intent)
        }
        return view
    }

}