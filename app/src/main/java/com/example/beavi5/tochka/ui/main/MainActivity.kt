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
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.beavi5.tochka.R
import com.example.beavi5.tochka.ui.base.BaseActivity
import com.example.beavi5.tochka.ui.base.RecyclerViewDivider
import com.example.beavi5.tochka.ui.login.LoginActivity
import com.example.beavi5.tochka.ui.main.adapters.RVGitHubAdapter
import com.example.beavi5.tochka.utils.Prefs
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : BaseActivity(), IMainView, NavigationView.OnNavigationItemSelectedListener {

    override fun clearRVAdapter() {
        rvAdapter.clear()
    }

    private var isLoading = false

    private lateinit var presenter: IMainPresenter
    private lateinit var prefs: Prefs
    private lateinit var rvAdapter: RVGitHubAdapter
    private lateinit var searchSubscription: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = Prefs(this)
        presenter = MainPresenter(this)
        setContentView(R.layout.activity_main)
        initActionBar()
        initRecyclerView()
        initNavigationView()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miExit -> {
                presenter.onSignOut()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initNavigationView() {
        val navigationView = findViewById(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val ivAvatar = headerView.findViewById(R.id.ivAvatar) as ImageView

        if (prefs.photoUrl.isNotEmpty())
            Picasso.get().load(prefs.photoUrl).into(ivAvatar)
        else Picasso.get().load(R.mipmap.avatariconbig).into(ivAvatar);


        val tvLogin = headerView.findViewById(R.id.tvLogin) as TextView
        tvLogin.text = prefs.userName
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun initRecyclerView() {
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        rvAdapter = RVGitHubAdapter()
        recyclerView.adapter = rvAdapter
        recyclerView.addItemDecoration(RecyclerViewDivider(this))
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleThreshold = 5
                val visibleItemCount = recyclerView.layoutManager.getChildCount()
                val totalItemCount = recyclerView.layoutManager.getItemCount()
                val firstVisibleItems = (recyclerView.layoutManager as (LinearLayoutManager)).findFirstVisibleItemPosition()

                if (!isLoading) {
                    if (visibleItemCount + firstVisibleItems >= totalItemCount - visibleThreshold) {
                        isLoading = true
                        presenter.onLoadMore()
                        showProgress()
                    }
                }
            }

        })


    }

    override fun onDestroy() {
        searchSubscription.dispose()
        presenter.onDestroy()
        super.onDestroy()
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
       searchViewItem.setOnMenuItemClickListener { ivSearchArrow.visibility = View.GONE
       true}
        val searchView = searchViewItem.actionView as SearchView
        searchSubscription = RxSearchView.queryTextChanges(searchView).debounce(500, TimeUnit.MILLISECONDS)
                .map(CharSequence::toString)
                .filter { it.isNotEmpty() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ presenter.onSearchQuery(it) }, { onError(it) })

        return super.onCreateOptionsMenu(menu)
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

    override fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    override fun onError(e: Throwable) {
        hideProgress()
        showError(e)
    }


}
