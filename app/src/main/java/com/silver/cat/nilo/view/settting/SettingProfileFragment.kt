package com.silver.cat.nilo.view.settting

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceViewHolder
import android.util.AttributeSet
import com.silver.cat.nilo.R
import com.silver.cat.nilo.config.dagger.Injectable
import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.util.Result
import com.silver.cat.nilo.util.observe
import com.silver.cat.nilo.view.util.crop.CropImageActivity
import com.silver.cat.nilo.view.util.glide.NetworkImageView
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject


class SettingProfileFragment : PreferenceFragmentCompat(), Injectable {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private val compositeDisposable = CompositeDisposable()

  private val viewModel: SettingProfileViewModel by lazy {
    ViewModelProviders.of(this, viewModelFactory).get(SettingProfileViewModel::class.java)
  }

  private lateinit var avatarPreference: SettingProfileAvatarPreference

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    AndroidSupportInjection.inject(this)
    addPreferencesFromResource(R.xml.fragment_setting_profile)
    avatarPreference = findPreference("avatarPreference") as SettingProfileAvatarPreference

    avatarPreference.setOnPreferenceClickListener {
      val activity = activity ?: return@setOnPreferenceClickListener true
      CropImageActivity.start(activity).subscribe { optional ->
        if (optional.isPresent) {
          val result = optional.get()
          result.uri?.let {
            avatarPreference.imageUri = it
            avatarPreference.notifyViewHolder()
          }
        }
      }.addTo(compositeDisposable)
      true
    }

    viewModel.me.observe(this, { result ->
      when (result) {
        is Result.Success -> {
          val accountDto = result.data
          avatarPreference.accountDto = accountDto
        }
      }
    })
  }

  override fun onDestroy() {
    compositeDisposable.clear()
    super.onDestroy()
  }

}

class SettingProfileAvatarPreference @JvmOverloads constructor(context: Context,
                                                               attrs: AttributeSet? = null,
                                                               defStyle: Int = 0)
  : Preference(context, attrs, defStyle) {

  var accountDto: AccountDto? = null

  var imageUri: String? = null

  init {
    layoutResource = R.layout.preference_setting_profile_avatar
  }

  override fun onBindViewHolder(holder: PreferenceViewHolder) {
    super.onBindViewHolder(holder)
    val avatar = holder.findViewById(R.id.avatar) as NetworkImageView
    if (imageUri != null) {
      avatar.setImageUrl(imageUri)
    } else {
      accountDto?.let {
        //        avatar.setImageUrl()
      }
    }
  }

  fun notifyViewHolder() {
    notifyChanged()
  }
}