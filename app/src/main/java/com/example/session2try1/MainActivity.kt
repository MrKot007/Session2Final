package com.example.session2try1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.session2try1.Connection.api
import com.example.session2try1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signOut.setOnClickListener {
            api.signOut("Bearer ${SharedPref.getToken(this@MainActivity)}").push(object: OnGetData<Boolean>{
                override fun onGet(data: Boolean) {
                    SharedPref.saveMail("", this@MainActivity)
                    SharedPref.savePassword("", this@MainActivity)
                    SharedPref.saveToken("", this@MainActivity)
                    finishAffinity()
                }

                override fun onError(error: String) {
                    showAlertDialog(error, this@MainActivity)
                }
            }, this)
        }

    }
    private fun showAlertDialog(reason: String, context: Context) {
        AlertDialog.Builder(context)
            .setTitle(reason)
            .setPositiveButton("Попробовать снова") {
                    dialog, id -> dialog.cancel()
            }.create().show()
    }
}