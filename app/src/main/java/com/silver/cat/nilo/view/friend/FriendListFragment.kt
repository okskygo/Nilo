package com.silver.cat.nilo.view.friend

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.silver.cat.nilo.R
import com.silver.cat.nilo.config.dagger.Injectable
import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.util.Result
import com.silver.cat.nilo.util.observe
import com.silver.cat.nilo.view.BaseFragment
import com.silver.cat.nilo.view.util.inflate
import com.silver.cat.nilo.view.widget.decoration.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_friend_list.*
import kotlinx.android.synthetic.main.holder_friend.view.*
import javax.inject.Inject

/**
 * Created by xiezhenyu on 2017/5/11.
 */

class FriendListFragment : BaseFragment(), Injectable {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private val adapter = FriendListAdapter()

  private val friendListViewModel: FriendListViewModel by lazy {
    ViewModelProviders.of(this, viewModelFactory).get(FriendListViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater,
                            container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_friend_list, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    lifecycle.addObserver(friendListViewModel)

    recyclerView.addItemDecoration(DividerItemDecoration(
        context,
        DividerItemDecoration.Type.BOTTOM))
    recyclerView.layoutManager = LinearLayoutManager(context)
    recyclerView.adapter = adapter

    friendListViewModel.friends.observe(this, { result ->
      when (result) {
        is Result.Success -> {
          adapter.update(result.data)
        }
      }
    })

  }

}

class FriendListAdapter : RecyclerView.Adapter<FriendListViewHolder>() {

  private val friends: MutableList<AccountDto> = mutableListOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendListViewHolder {
    return FriendListViewHolder(parent)
  }

  override fun onBindViewHolder(holder: FriendListViewHolder, position: Int) {
    holder.bind(friends[position])
  }

  override fun getItemCount(): Int {
    return friends.count()
  }

  fun update(friends: List<AccountDto>) {
    this.friends.clear()
    this.friends.addAll(friends)
    notifyDataSetChanged()
  }
}

class FriendListViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.holder_friend)) {

  private var accountDto: AccountDto? = null

  init {
    itemView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
      val accountDto = accountDto
      if (accountDto == null) {
        itemView.shimmerLayout.stopShimmerAnimation()
      } else {
        if (accountDto.creator) {
          itemView.shimmerLayout.startShimmerAnimation()
        } else {
          itemView.shimmerLayout.stopShimmerAnimation()
        }
      }
    }
  }

  fun bind(accountDto: AccountDto) {
    this.accountDto = accountDto
    //TODO avatar
    itemView.nickname.text = accountDto.nickname
    itemView.motto.text = accountDto.motto
  }
}