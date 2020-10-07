package co.id.bismalabs.mvvm_architecture_android_beginners.ui.main.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import co.id.bismalabs.mvvm_architecture_android_beginners.R
import co.id.bismalabs.mvvm_architecture_android_beginners.data.api.ApiHelper
import co.id.bismalabs.mvvm_architecture_android_beginners.data.api.ApiServiceImpl
import co.id.bismalabs.mvvm_architecture_android_beginners.data.model.User
import co.id.bismalabs.mvvm_architecture_android_beginners.ui.base.ViewModelFactory
import co.id.bismalabs.mvvm_architecture_android_beginners.ui.main.adapter.MainAdapter
import co.id.bismalabs.mvvm_architecture_android_beginners.ui.main.viewmodel.MainViewModel
import co.id.bismalabs.mvvm_architecture_android_beginners.utils.Status
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUi()
        setupViewModel()
        setupObserver()
    }

    private fun setupUi() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(MainViewModel::class.java)
    }

    private fun setupObserver() {
        mainViewModel.getUsers().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    it.data?.let { users -> renderList(users) }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun renderList(users: List<User>) {
        adapter.addData(users)
        adapter.notifyDataSetChanged()
    }
}