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
import com.silver.cat.nilo.util.Result
import com.silver.cat.nilo.util.observe
import com.silver.cat.nilo.view.BaseFragment
import com.silver.cat.nilo.view.util.inflate
import com.silver.cat.nilo.view.widget.decoration.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_friend_list.*
import kotlinx.android.synthetic.main.holder_friend_wireframe.view.*
import javax.inject.Inject

/**
 * Created by xiezhenyu on 2017/5/11.
 */

class FriendListFragment : BaseFragment(), Injectable {

  private val adapter = FriendWireframeAdapter()
  private lateinit var friendListViewModel: FriendListViewModel
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  override fun onCreateView(inflater: LayoutInflater,
                            container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_friend_list, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    friendListViewModel = ViewModelProviders.of(this,
        viewModelFactory).get(FriendListViewModel::class.java)

    friendListViewModel.creators.observe(this, { result ->
      when (result) {
        is Result.Success -> {
          println(">>>>>>>>>" + "result.data = ${result.data}")
        }
        is Result.InProgress -> {
          println(">>>>>>>>>" + "Result.InProgress")
        }
      }
    })

    recyclerView.addItemDecoration(DividerItemDecoration(
        context,
        DividerItemDecoration.Type.BOTTOM))
    recyclerView.layoutManager = LinearLayoutManager(context)
    recyclerView.adapter = adapter
  }

}

class FriendWireframeAdapter : RecyclerView.Adapter<FriendListViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendListViewHolder {
    return FriendListViewHolder(parent)
  }

  override fun onBindViewHolder(holder: FriendListViewHolder, position: Int) {
    holder.bind()
  }

  override fun getItemCount(): Int {
    return 8
  }
}

class FriendListViewHolder(parent: ViewGroup)
  : RecyclerView.ViewHolder(parent.inflate(R.layout.holder_friend_wireframe)) {

  init {
    itemView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
      itemView.shimmerLayout.startShimmerAnimation()
    }
  }

  fun bind() {}
}