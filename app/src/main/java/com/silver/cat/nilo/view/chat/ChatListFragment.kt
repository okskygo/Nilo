package com.silver.cat.nilo.view.chat

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.silver.cat.nilo.R
import com.silver.cat.nilo.util.view.DividerItemDecoration
import com.silver.cat.nilo.util.view.clickThrottleFirst
import com.silver.cat.nilo.util.view.inflate
import com.silver.cat.nilo.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_chat_list.*

/**
 * Created by xiezhenyu on 2017/5/11.
 */

class ChatListFragment : BaseFragment() {

    private val adapter = ChatListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.Type.BOTTOM))
        recyclerView.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}

class ChatListAdapter : RecyclerView.Adapter<ChatListViewHolder>() {

    override fun getItemCount(): Int = 10

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        return ChatListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        holder.bind()
    }
}

class ChatListViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(parent.inflate(R.layout.holder_chat_list)) {

    init {
        itemView.clickThrottleFirst()
                .subscribe {
                    itemView.context.startActivity(Intent(itemView.context, ChatActivity::class.java))
                }
    }

    fun bind() {}
}