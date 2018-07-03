package com.example.beavi5.tochka.ui.main.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.beavi5.tochka.R
import com.example.beavi5.tochka.ui.main.UserPM
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_user.view.*

class RVGitHubAdapter : RecyclerView.Adapter<RVGitHubAdapter.GitHubHolder>() {
    var users: ArrayList<UserPM> = ArrayList()

    fun clear() = users.clear()

    fun addItems(newItems: List<UserPM>) = users.plusAssign(newItems)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_user, parent, false)
        return GitHubHolder(view)
    }

    override fun onBindViewHolder(holder: GitHubHolder, position: Int) = holder.bindItems(users[position])

    override fun getItemCount() = users.size

    inner class GitHubHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(user: UserPM) {
            itemView.tvName.setText(user.userName)
            if (user.photoUrl.isNotEmpty()) {
                Picasso.get().load(user.photoUrl).into(itemView.ivAvatar)
            }

        }

    }
}