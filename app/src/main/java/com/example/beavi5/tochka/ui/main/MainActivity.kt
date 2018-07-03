package com.example.beavi5.tochka.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.example.beavi5.tochka.ui.login.LoginActivity
import com.example.beavi5.tochka.R
import com.example.beavi5.tochka.utils.Prefs
import com.example.beavi5.tochka.ui.BaseActivity
import com.example.beavi5.tochka.ui.main.adapters.RVGitHubAdapter
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : BaseActivity(), IMainView, NavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miExit -> {
                presenter.onSignOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onError(e: Throwable) {
        hideProgress()
        showError(e)
    }

    private var isLoading: Boolean = false
    private var currentPage = 1
    private var searchQuery = ""

    private lateinit var presenter: IMainPresenter
    private lateinit var prefs: Prefs
    private lateinit var rvAdapter: RVGitHubAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = MainPresenter(this)
        setContentView(R.layout.activity_main)
        initActionBar()
        initRecyclerView()
        prefs = Prefs(this)
        initNavigationView()

    }

    private fun initNavigationView() {
        val navigationView = findViewById(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val ivAvatar = headerView.findViewById(R.id.ivAvatar) as ImageView
        Picasso.get().load(prefs.photoUrl).into(ivAvatar)
        val tvLogin = headerView.findViewById(R.id.tvLogin) as TextView
        tvLogin.text = prefs.userName
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun initRecyclerView() {
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        rvAdapter = RVGitHubAdapter()
        recyclerView.adapter = rvAdapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            //            var currentPage = 1
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleThreshold = 5
                val visibleItemCount = recyclerView.layoutManager.getChildCount()
                val totalItemCount = recyclerView.layoutManager.getItemCount()
                val firstVisibleItems = (recyclerView.layoutManager as (LinearLayoutManager)).findFirstVisibleItemPosition()

                if (!isLoading) {
                    if (visibleItemCount + firstVisibleItems >= totalItemCount - visibleThreshold) {
                        isLoading = true
                        currentPage++
                        onLoadMore(currentPage, searchQuery)
                        showProgress()
                    }
                }
            }

        })


    }

    private fun initActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_actions, menu)
        val searchViewItem = menu!!.findItem(R.id.action_search)
        val searchView = searchViewItem.actionView as SearchView
        RxSearchView.queryTextChanges(searchView).debounce(500, TimeUnit.MILLISECONDS)
                .map(CharSequence::toString)
                .filter { it.isNotEmpty() }
               // .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (

                        {
                            onSearchQuery(it)
                        }   , {
                    onError(it)
                })


        return super.onCreateOptionsMenu(menu)
    }

    private fun onSearchQuery(searchText: String) {
        searchQuery = searchText
        currentPage = 1
        rvAdapter.clear()

        onLoadMore(currentPage, searchQuery)

    }

    private fun onLoadMore(currentPage: Int, searchQuery: String) {
        presenter.onLoadMore(currentPage, searchQuery)
    }

    override fun onMoreLoaded(data: List<UserPM>) {
        isLoading = false
        rvAdapter.addItems(data)
        rvAdapter.notifyDataSetChanged()
        hideProgress()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}
