package com.crystal.ineriorshop

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.crystal.ineriorshop.data.ArticleModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class HomeFragment: Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.firestore
        Log.d("homeFragment", "dddsfdsf")

        db.collection("articles").document("5h5aDLdZZ98tGh2yRTyG")
            .get()
            .addOnSuccessListener { result ->
                val article = result.toObject<ArticleModel>()
                Log.e("homeFragment", article.toString())
            }
            .addOnFailureListener {
                Log.d("homeFragment", "error: $it")

            }
    }
}