package com.example.proiectandroid

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.core.Context
import java.time.LocalDate
import kotlin.random.Random
@RequiresApi(Build.VERSION_CODES.O)
class AddFragment : Fragment() {

    private lateinit var database : DatabaseReference
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationId = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        val btn : Button = view.findViewById(R.id.button)
        val textAwb : TextView = view.findViewById(R.id.awb)
        val textAdresaDest : TextView = view.findViewById(R.id.adresaDest)
        val textAdresaExp : TextView = view.findViewById(R.id.adresaExp)
        val textDetalii : TextView = view.findViewById(R.id.detalii)
        val textRamburs : TextView = view.findViewById(R.id.ramburs)

        setAwbText(view)

        btn.setOnClickListener{
            if(!verificaCampuri(view)){
                Toast.makeText(getActivity(),"Campuri incorecte", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val awb : Long = textAwb.text.toString().toLong()
            val adresaExp = textAdresaExp.text.toString()
            val adresaDest = textAdresaDest.text.toString()
            val detalii = textDetalii.text.toString()
            val ramburs = textRamburs.text.toString().toDouble()
            val uid : String = FirebaseAuth.getInstance().currentUser?.uid.toString()
            val pic = "photo1.png"


            database = FirebaseDatabase.getInstance().getReference(uid)

            val Awb = AwbData(awb, adresaExp, adresaDest, detalii, ramburs, uid, pic)
            database.child(awb.toString()).setValue(Awb)
                .addOnCompleteListener {
                textAdresaExp.text = ""
                textAdresaDest.text = ""
                textDetalii.text = ""
                textRamburs.text = ""
                setAwbText(view)

                // notificare(awb.toString())

                Toast.makeText(getActivity(),"Succes", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(getActivity(),"Eroare", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAwbText(view : View){
        val today = LocalDate.now()
        var awb = "1"
        var day = today.dayOfYear.toString()
        while(day.length < 3)
            day = "0" + day
        awb = awb + day
        var rnd = Random.nextInt(0, 9999).toString()
        while(rnd.length < 4)
            rnd = "0" + day
        awb = awb + rnd

        view.findViewById<TextView>(R.id.awb).text = awb
    }

    private fun verificaCampuri(view: View) : Boolean{
        val textAwb : TextView = view.findViewById(R.id.awb)
        val textAdresaDest : TextView = view.findViewById(R.id.adresaDest)
        val textAdresaExp : TextView = view.findViewById(R.id.adresaExp)
        val textDetalii : TextView = view.findViewById(R.id.detalii)
        val textRamburs : TextView = view.findViewById(R.id.ramburs)

        if(textAdresaDest.text.isEmpty())
            return false
        if(textAdresaExp.text.isEmpty())
            return false
        if(textDetalii.text.isEmpty())
            return false
        try{
            textRamburs.text.toString().toDouble()
        }
        catch (e: NumberFormatException){
            if(!textRamburs.text.isEmpty())
                return false
        }

        return true
    }

}