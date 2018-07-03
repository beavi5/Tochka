package com.example.beavi5.tochka.ui.main.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.beavi5.tochka.R
import com.example.beavi5.tochka.ui.main.UserPM
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_user.view.*
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri


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
        var url = ""
        init {
            itemView?.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                itemView.context.startActivity(intent)
            }
        }
        fun bindItems(user: UserPM) {
            url = user.url
            itemView.tvName.setText(user.userName)
            itemView.tvScore.setText(user.score.toString())
            if (user.photoUrl.isNotEmpty())
                Picasso.get().load(user.photoUrl).into(itemView.ivAvatar)
            else Picasso.get().load(R.mipmap.avataricon).into(itemView.ivAvatar)

        }

    }
}