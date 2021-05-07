package com.billyindrai.aplikasigithubuser

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.billyindrai.aplikasigithubuser.databinding.ActivityDetailUserBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity(){
    companion object {
        const val EXTRA_USER = "EXTRA_USER"
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var user : User
    private val checked: Int = R.drawable.ic_baseline_favorite_border_24
    private val unChecked: Int = R.drawable.ic_baseline_favorite_24

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f

        user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        val PagerAdapter = PagerAdapter(this@DetailUserActivity, user)
        binding.viewPager.adapter = PagerAdapter

        val viewModel : ViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(ViewModel::class.java)
        var statusFav = false

        showLoading(true)
        viewModel.findUser(user.username)!!.observe(this, { findDB ->
            user.name?.let { setActionBarTittle(it) }
            if (findDB != null && findDB.username == user.username) {
                userDetail(findDB)
                statusFav = true
                binding.favorite.setImageResource(checked)
            } else {
                showLoading(true)
                viewModel.getUserDetail(user)
                viewModel.loadUserDetail().observe(this, { userDetail ->
                    if (userDetail != null) {
                        user = userDetail
                        userDetail(user)
                        showLoading(false)
                    }
                })
            }
        })
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(MainActivity.TAB_TITLES[position])
        }.attach()

        binding.favorite.setOnClickListener { view ->

            if (!statusFav) {
                viewModel.insert(user)
                binding.favorite.setImageResource(checked)
                Snackbar.make(view, "Added to Favorite", Snackbar.LENGTH_SHORT).show()
            } else {
                viewModel.delete(user)
                binding.favorite.setImageResource(unChecked)
                Snackbar.make(view, "Removed from Favorite", Snackbar.LENGTH_SHORT).show()
            }
            statusFav = !statusFav
        }

    }

   private fun userDetail(user : User){
       binding.dtTvUsername.text = user.username
       binding.dtTvName.text = user.name
       Glide.with(this@DetailUserActivity)
               .load(user.avatar)
               .apply(RequestOptions().override(350, 350))
               .into(binding.dtPhoto)
       if(user.location == null){
           binding.dtTvLocation.text = getString(R.string.location, "-")
       }else {
           binding.dtTvLocation.text = getString(R.string.location, user.location)
       }
       if(user.company == null){
           binding.dtTvCompany.text = getString(R.string.company, "-")

       }else {
           binding.dtTvCompany.text = getString(R.string.company, user.company)
       }
       binding.dtTvRepository.text = getString(R.string.repository,user.repository)
       binding.dtTvFollowing.text = getString(R.string.following,user.following)
       binding.dtTvFollowers.text = getString(R.string.followers,user.followers)

    }

    private fun setActionBarTittle(title: String) {
        if (supportActionBar != null) {
            this.title = title
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_setting, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.favorite -> startActivity(Intent(this, FavoriteActivity::class.java))

            R.id.setting -> startActivity(Intent(this, SettingActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }


}