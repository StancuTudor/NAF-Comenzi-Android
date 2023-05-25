package com.example.proiectandroid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import kotlin.collections.mutableMapOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proiectandroid.databinding.FragmentListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: AwbAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var awbArrayList: ArrayList<AwbData>
    private lateinit var searchView: SearchView

    private lateinit var databaseReference: DatabaseReference

    private lateinit var awbMap: MutableMap<String, Long>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentListBinding.inflate(layoutInflater)
        var uid : String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().reference.child(uid)

        return inflater.inflate(R.layout.fragment_list, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()
    }

    private fun dataInitialize() {
        awbArrayList = ArrayList()

        awbMap = mutableMapOf()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {

                    val key = childSnapshot.key.toString()
                    println(key)
                    val item = childSnapshot.value as HashMap<*, *>

                    awbMap[key] = item["awb"] as Long
                }
                populateRecycler()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun populateRecycler(){
        for (key in awbMap.keys) {
            val awb = AwbData(awbMap[key]!!.toLong())
            awbArrayList.add(awb)
        }

        afterDataInitialize()
    }

    private fun afterDataInitialize(){
        val layoutManager = LinearLayoutManager(context)

        recyclerView = requireView().findViewById(R.id.recyclerView)
        searchView = requireView().findViewById(R.id.searchView)

        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = AwbAdapter(awbArrayList)
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
// Click pe adapter
//        adapter.setOnItemClickListener(object : AwbAdapter.onItemClickListener {
//            override fun onItemClick(position: Int) {
//                val intent = Intent(activity, GamesActivity::class.java)
//
//                intent.putExtra("image", gamesArrayList[position].image)
//                intent.putExtra("title", gamesArrayList[position].title)
//                intent.putExtra("description", gamesArrayList[position].description)
//                intent.putExtra("price", gamesArrayList[position].price)
//
//                startActivity(intent)
//            }
//        })
    }

    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = ArrayList<AwbData>()

            for (i in awbArrayList) {
                if (i.awb.toString().lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(activity, "No Data found", Toast.LENGTH_SHORT).show()
                adapter.setFilteredList(filteredList)
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }
}