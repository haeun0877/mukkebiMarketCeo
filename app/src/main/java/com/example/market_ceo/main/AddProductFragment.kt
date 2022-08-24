package com.example.market_ceo.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.market_ceo.R
import com.example.market_ceo.databinding.FragmentAddProductBinding

class AddProductFragment : Fragment() {
    lateinit var binding: FragmentAddProductBinding

    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(inflater, container, false)

        binding.ivAddImage.setOnClickListener {
            navigatePhotos()
        }

        val editFocusListener = EditFocus()
        binding.etOrigin.onFocusChangeListener = editFocusListener
        binding.etProductName.onFocusChangeListener = editFocusListener
        binding.etProductPrice.onFocusChangeListener = editFocusListener

        return binding.root
    }

    private fun navigatePhotos() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,2000)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AddProductFragment().apply {
            }
    }

    inner class EditFocus : View.OnFocusChangeListener {
        override fun onFocusChange(view: View?, hasFocus: Boolean) {
            if(hasFocus){
                view?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.box_round_5_white_0537c8) }
            }else{
                view?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.box_round_5_whie_black) }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK) {
            Toast.makeText(context,"잘못된 접근입니다",Toast.LENGTH_SHORT).show()
            return
        }

        when(requestCode) {
            2000 -> {
                val selectedImageURI : Uri? = data?.data

                if( selectedImageURI != null) {
                    imageUri = selectedImageURI
                    binding.ivAddImage.setImageURI(imageUri)
                }else {
                    Toast.makeText(context,"사진을 가져오지 못했습니다",Toast.LENGTH_SHORT).show()
                }
            } else -> {
            Toast.makeText(context,"잘못된 접근입니다",Toast.LENGTH_SHORT).show()
        }
        }
    }
}