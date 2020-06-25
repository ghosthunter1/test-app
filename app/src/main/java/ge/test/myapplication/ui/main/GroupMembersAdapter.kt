package ge.test.myapplication.ui.main

import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl
import com.daimajia.swipe.interfaces.SwipeAdapterInterface
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface
import com.daimajia.swipe.util.Attributes
import ge.test.myapplication.R
import ge.test.myapplication.loadCircleImage
import ge.test.myapplication.loadCircleImageFromDrawable
import ge.test.myapplication.network.models.Member
import ge.test.myapplication.setNumberDifferentSizeText
import kotlinx.android.synthetic.main.group_member_item.view.*


class GroupMembersAdapter(val onItemClicked: (type: Int) -> Unit) :
    RecyclerView.Adapter<GroupMembersAdapter.GroupMemberViewHolder>()
    , SwipeItemMangerInterface, SwipeAdapterInterface {

    private var swipeItemRecyclerMangerImpl = SwipeItemRecyclerMangerImpl(this).apply {
        mode = Attributes.Mode.Multiple
    }
    private var members: ArrayList<Member> = ArrayList<Member>()
    private var myosition = -1
    private var userImage: Bitmap? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMemberViewHolder {
        return GroupMemberViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.group_member_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return members.size
    }

    override fun onBindViewHolder(holder: GroupMemberViewHolder, position: Int) {
        swipeItemRecyclerMangerImpl.bindView(holder.swipeLayout, position)
        holder.swipeLayout.isClickToClose = true
        holder.bind(members.get(position), position)


    }

    fun addMembers(members: List<Member>) {
        this.members.addAll(members)
        notifyDataSetChanged()
    }


    inner class GroupMemberViewHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {


        val swipeLayout = view.swiper_layout
        val leftDragable = view.left_dragable
        val rightDragable = view.right_dragable_view
        val mainContentLayout = view.group_item_main_content_layout
        var memberName = view.member_name
        var memberNumber = view.member_number
        var memberTime = view.member_time
        var memberImage = view.member_image

        init {
            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown)
            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, leftDragable)
            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, rightDragable)
            leftDragable.setOnClickListener(this)
            rightDragable.setOnClickListener(this)
        }


        fun bind(member: Member, pos: Int) {
            if (myosition == member.position) {
                swipeLayout.isSwipeEnabled = false
                mainContentLayout.background = swipeLayout.context.getDrawable(R.drawable.group_member_item_selected_bg)
            } else {
                swipeLayout.isSwipeEnabled = true
                mainContentLayout.setBackgroundColor(Color.WHITE)
            }
            memberName.text = member.name
            memberNumber.text = member.position.toString()
            memberTime.setNumberDifferentSizeText(member.hours)

            if (myosition == member.position) {
                if (userImage != null)
                    memberImage.loadCircleImageFromDrawable(userImage!!)
            } else {
                if (member.image == null) {
                    memberImage.loadCircleImage(member.imageUrl, member)
                } else {
                    memberImage.loadCircleImageFromDrawable(member.image!!)

                }
            }

        }

        override fun onClick(v: View?) {
            when (v?.id) {
                leftDragable.id -> onItemClicked.invoke(1)
                rightDragable.id -> onItemClicked.invoke(2)
            }
        }


    }

    fun setUserImage(bitmap: Bitmap?) {
        userImage = bitmap
    }

    fun setUserPosition(position: Int) {
        myosition = position
        notifyDataSetChanged()
    }

    override fun closeAllExcept(layout: SwipeLayout?) {
        swipeItemRecyclerMangerImpl.closeAllExcept(layout)
    }

    override fun setMode(mode: Attributes.Mode?) {
        swipeItemRecyclerMangerImpl.setMode(mode);
    }

    override fun closeAllItems() {
        swipeItemRecyclerMangerImpl.closeAllItems()
    }

    override fun removeShownLayouts(layout: SwipeLayout?) {
        swipeItemRecyclerMangerImpl.removeShownLayouts(layout);
    }

    override fun getOpenItems(): MutableList<Int> {
        return swipeItemRecyclerMangerImpl.getOpenItems();
    }

    override fun isOpen(position: Int): Boolean {
        return swipeItemRecyclerMangerImpl.isOpen(position);
    }

    override fun openItem(position: Int) {
        swipeItemRecyclerMangerImpl.isOpen(position);
    }

    override fun getMode(): Attributes.Mode {
        return swipeItemRecyclerMangerImpl.getMode();
    }

    override fun getOpenLayouts(): MutableList<SwipeLayout> {
        return swipeItemRecyclerMangerImpl.getOpenLayouts();
    }

    override fun closeItem(position: Int) {
        swipeItemRecyclerMangerImpl.closeItem(position);
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swiper_layout;
    }


}