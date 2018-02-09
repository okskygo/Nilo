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
import com.silver.cat.nilo.view.util.input.InputActivity
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
  private lateinit var nicknamePreference: Preference
  private lateinit var mottoPreference: Preference
  private lateinit var nidPreference: Preference

  private var accountDto: AccountDto? = null

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    AndroidSupportInjection.inject(this)
    addPreferencesFromResource(R.xml.fragment_setting_profile)
    avatarPreference = findPreference("avatarPreference") as SettingProfileAvatarPreference
    nicknamePreference = findPreference("nickname")
    mottoPreference = findPreference("motto")
    nidPreference = findPreference("nid")

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

    nicknamePreference.setOnPreferenceClickListener {
      val activity = activity ?: return@setOnPreferenceClickListener true
      val accountDto = accountDto ?: return@setOnPreferenceClickListener true

      InputActivity.Builder(30,
          getString(R.string.nickname_hint),
          getString(R.string.nickname),
          accountDto.nickname)
          .startActivityForResult(activity).subscribe {
        if (it.isPresent) {
          nicknamePreference.summary = it.get()
        }
      }.addTo(compositeDisposable)
      true
    }

    mottoPreference.setOnPreferenceClickListener {
      val activity = activity ?: return@setOnPreferenceClickListener true
      val accountDto = accountDto ?: return@setOnPreferenceClickListener true

      InputActivity.Builder(100,
          getString(R.string.motto_hint),
          getString(R.string.motto),
          accountDto.motto)
          .startActivityForResult(activity).subscribe {
        if (it.isPresent) {
          mottoPreference.summary = it.get()
        }
      }.addTo(compositeDisposable)
      true
    }

    nidPreference.setOnPreferenceClickListener {
      val activity = activity ?: return@setOnPreferenceClickListener true
      val accountDto = accountDto ?: return@setOnPreferenceClickListener true
      if (accountDto.nid != null) return@setOnPreferenceClickListener true

      InputActivity.Builder(10, getString(R.string.id_hint), getString(R.string.id))
          .startActivityForResult(activity).subscribe {
        if (it.isPresent) {
          nidPreference.summary = it.get()
        }
      }.addTo(compositeDisposable)
      true
    }

    viewModel.me.observe(this, { result ->
      when (result) {
        is Result.Success -> {
          val accountDto = result.data
          this.accountDto = accountDto
          avatarPreference.accountDto = accountDto
          nicknamePreference.summary = accountDto.nickname
          mottoPreference.summary = accountDto.motto ?: " "
          nidPreference.summary = accountDto.nid ?: getString(R.string.not_set_yet)
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