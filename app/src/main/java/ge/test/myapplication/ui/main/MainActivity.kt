package ge.test.myapplication.ui.main

import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ge.test.myapplication.R
import ge.test.myapplication.loadCircleImage
import kotlinx.android.synthetic.main.bottom_sheet.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import ge.test.myapplication.network.models.InfoResponse
import ge.test.myapplication.network.models.Member
import ge.test.myapplication.network.models.Resource
import ge.test.myapplication.setNumberDifferentSizeText


class MainActivity : AppCompatActivity(), GroupOptionsBottomSheet.OnItemClickListener {


    val mAdapter = GroupMembersAdapter({ type -> onAdapterItemClicked(type) })
    var menuItem: MenuItem? = null
    lateinit var dialog: GroupOptionsBottomSheet
    lateinit var linearLayoutManager: LinearLayoutManager
    var isAddMenuVisible = false
    lateinit var mainViewModel: MainViewModel
    var errorDialog: AlertDialog? = null
    var loading = true
    var userPosition = 100
    var pastVisiblesItems: Int = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iniModelView()
        observe()
        iniUI()
        setListeners()
    }

    fun onAdapterItemClicked(type: Int) {
        if (type == 1) {
            showSnackBar("უფლებების მინიჭება")
            return
        }
        showSnackBar("ჯგუფიდან წაშლა")

    }

    fun showSnackBar(text: String) {
        val snackbar = Snackbar.make(main_coordinatlayout, text, Snackbar.LENGTH_LONG)
        snackbar.setAction("CLOSE") {
            snackbar.dismiss()
        }
        snackbar.setActionTextColor(Color.RED).show()
    }

    fun iniUI() {
        iniToolbar()
        linearLayoutManager = LinearLayoutManager(this).apply {
            orientation = RecyclerView.VERTICAL
        }
        members_recyclerview.layoutManager = linearLayoutManager
        members_recyclerview.setHasFixedSize(true);
        members_recyclerview.setItemViewCacheSize(20);
        members_recyclerview.setDrawingCacheEnabled(true);
        members_recyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        members_recyclerview.adapter = mAdapter
    }

    fun iniModelView() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    fun observe() {
        mainViewModel.info.observe(this, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> fillInfo(it.data!!)
                Resource.Status.ERROR -> showConnectionError(it.message!!)
                Resource.Status.LOADING -> lottie.visibility = View.VISIBLE
            }

        })

        mainViewModel.memberlist.observe(this, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> updateMemberAdapter(it.data!!)
                Resource.Status.ERROR -> showConnectionError(it.message!!)
                Resource.Status.LOADING -> lottie.visibility = View.VISIBLE
            }
        })


        mainViewModel.getInfo()
    }

    fun updateMemberAdapter(members: List<Member>) {
        errorDialog?.dismiss()
        lottie.visibility = View.GONE
        mAdapter.addMembers(members)
    }

    fun fillInfo(response: InfoResponse) {
        errorDialog?.dismiss()
        userPosition = response.me.position
        lottie.visibility = View.GONE
        iniAnimation()
        member_label.text = response.info[0].key
        member_count_label.setNumberDifferentSizeText(response.info[0].value)
        average_time.text = response.info[1].key
        average_time_label.setNumberDifferentSizeText(response.info[1].value)
        full_time.text = response.info[2].key
        full_time_label.setNumberDifferentSizeText(response.info[2].value)
        fitness_club_icon.loadCircleImage(response.imageUrl)

        user_number.text = response.me.position.toString()
        user_image.loadCircleImage(response.imageUrl, response.me, { bitmap -> mAdapter.setUserImage(bitmap) })
        user_name.text = response.me.name
        user_time.setNumberDifferentSizeText(response.me.hours)

        mAdapter.setUserPosition(response.me.position)
    }

    fun showConnectionError(errorText: String) {
        lottie.visibility = View.GONE
        errorDialog = AlertDialog.Builder(this)
            .setTitle(errorText)
            .setPositiveButton("Try Again", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    mainViewModel.getInfo()
                }

            })
            .create()

        errorDialog?.show()
    }

    fun iniToolbar() {
        setSupportActionBar(toolabar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false);
    }


    fun setListeners() {
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (Math.abs(verticalOffset) == appBarLayout.totalScrollRange)
                showAddMenuItem(true)
            else
                showAddMenuItem(false)
        })

        bottom_sheet_options_menu.setOnClickListener {
            appBarLayout.setExpanded(true)
            showGroupOptionsDialog()
        }


        members_recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (linearLayoutManager.findLastVisibleItemPosition() < userPosition) {
                    user_layout.visibility = View.VISIBLE
                } else {
                    user_layout.visibility = View.GONE
                }

                if (dy > 0) {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            if (mainViewModel.hasmore.value!!) {
                                mainViewModel.currentPage.value = mainViewModel.currentPage.value!! + 1
                                mainViewModel.getMembers()
                            }
                        }
                    }
                }
            }
        })

    }

    fun showAddMenuItem(visible: Boolean) {
        if (isAddMenuVisible != visible) {
            isAddMenuVisible = visible
            menuItem?.setVisible(visible)
            if (visible)
                main_coordinatlayout.setBackgroundColor(Color.WHITE)
            else
                main_coordinatlayout.setBackgroundColor(Color.parseColor("#e4f7fb"))
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu_items, menu)
        menuItem = menu.findItem(R.id.add)
        return true
    }


    fun showGroupOptionsDialog() {
        dialog = GroupOptionsBottomSheet.newInstance()
        dialog.show(supportFragmentManager, "dialog")
    }

    fun iniAnimation() {
        animateView(fitness_club_icon, R.anim.scale_and_alpha, 0)
        animateView(fitness_club_label, R.anim.enter_from_bottom, 0)

        animateView(add_group_icon, R.anim.enter_from_bottom, 100)
        animateView(edit_picture_icon, R.anim.enter_from_bottom, 100)
        animateView(edit_icon, R.anim.enter_from_bottom, 200)

        animateView(add_group_label, R.anim.enter_from_bottom, 200)
        animateView(chage_picture_label, R.anim.enter_from_bottom, 300)
        animateView(edit_label, R.anim.enter_from_bottom, 300)

        animateView(member_count_label, R.anim.enter_from_bottom, 400)
        animateView(average_time_label, R.anim.enter_from_bottom, 400)
        animateView(full_time_label, R.anim.enter_from_bottom, 500)

        animateView(member_label, R.anim.enter_from_bottom, 500)
        animateView(average_time, R.anim.enter_from_bottom, 600)
        animateView(full_time, R.anim.enter_from_bottom, 600)


    }

    fun animateView(view: View, animationId: Int, offset: Long) {
        var anim = AnimationUtils.loadAnimation(this, animationId)
        anim.startOffset = offset
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
            }

            override fun onAnimationStart(p0: Animation?) {
                view.visibility = View.VISIBLE
            }

        })
        view.startAnimation(anim)
    }

    override fun onItemClicked(text: String) {
        try {
            dialog.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        showSnackBar(text)
    }

    override fun onDestroy() {
        mainViewModel.clearDisposables()
        super.onDestroy()
    }
}
