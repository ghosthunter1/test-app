package ge.test.myapplication.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ge.test.myapplication.R
import kotlinx.android.synthetic.main.group_options_dialog.view.*

class GroupOptionsBottomSheet : BottomSheetDialogFragment() {

    private lateinit var layout: View
    private lateinit var listener: OnItemClickListener
    private val anim1: Int = R.anim.group_option_item_load_animation

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        layout = inflater.inflate(R.layout.group_options_dialog, container, false)

        startAnimations()
        setListeners()


        return layout
    }


    fun loadAnimation(offset: Long, view: View, animResId: Int) {
        var anim = AnimationUtils.loadAnimation(context, animResId)
        anim.startOffset = offset
        view.startAnimation(anim)
    }


    fun startAnimations() {
        loadAnimation(0, layout.group_options_label, anim1)
        loadAnimation(0, layout.group_options_line, anim1)


        loadAnimation(100, layout.permission_assign_icon, anim1)
        loadAnimation(100, layout.permission_assign_text, anim1)

        loadAnimation(200, layout.remove_member_icon, anim1)
        loadAnimation(200, layout.remove_member_text, anim1)


        loadAnimation(300, layout.leave_group_icon, anim1)
        loadAnimation(300, layout.leave_group_text, anim1)
    }

    fun setListeners() {
        layout.bottomsheet_permission.setOnClickListener {
            listener.onItemClicked("უფლებების მინიჭება")
        }

        layout.bottomsheet_remove_member.setOnClickListener {
            listener.onItemClicked("წევრების წაშლა")
        }

        layout.bottomsheet_leave_group.setOnClickListener {
            listener.onItemClicked("ჯგუფის დატოვება")
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnItemClickListener
    }

    companion object {
        fun newInstance(): GroupOptionsBottomSheet {
            return GroupOptionsBottomSheet()
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(text: String)
    }
}