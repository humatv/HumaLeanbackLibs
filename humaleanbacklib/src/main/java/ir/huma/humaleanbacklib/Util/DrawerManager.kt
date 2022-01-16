package ir.huma.humaleanbacklib.Util

import android.graphics.Color
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.mikepenz.crossfader.Crossfader
import com.mikepenz.crossfader.util.UIUtils
import com.mikepenz.crossfader.view.CrossFadeSlidingPaneLayout
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.MiniDrawer
import com.mikepenz.materialdrawer.holder.DimenHolder
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.util.ifNotNull
import com.mikepenz.materialdrawer.util.ifNull
import ir.huma.humaleanbacklib.R
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.math.roundToInt

class DrawerManager(val activity: FragmentActivity, val result: Drawer) {

    private lateinit var miniResult: MiniDrawer
    private lateinit var crossFader: Crossfader<*>
    var frameFragmentRes: Int? = null
    var isRtl = false;
    var miniDrawerBackColor: Int? = null;
    var useMiniDrawer: Boolean = true
    var customHeightMiniDrawerItemInDp = 120
    var fireOnClick = false;
    var miniDrawerWidth =
        activity.resources.getDimension(R.dimen.material_mini_drawer_item).roundToInt()
    var drawerWidth =
        activity.resources.getDimension(R.dimen.material_drawer_width).roundToInt()

    fun build(crossfadeContentResLayout: Int) {
        if (useMiniDrawer) {
            miniResult = result.miniDrawer!!

            //get the widths in px for the first and second panel
            val firstWidth = drawerWidth
            val secondWidth = miniDrawerWidth

            //create and build our crossfader (see the MiniDrawer is also builded in here, as the build method returns the view to be used in the crossfader)
            //the crossfader library can be found here: https://github.com/mikepenz/Crossfader
            crossFader = Crossfader<CrossFadeSlidingPaneLayout>()
                .withContent(activity.findViewById<View>(crossfadeContentResLayout))
                .withFirst(result.slider, firstWidth)
                .withSecond(miniResult.build(activity), secondWidth)
//                    .withSavedInstance(savedInstanceState)
                .build()

            //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
            miniResult.withCrossFader(CrossfadeWrapper(crossFader))
            if (miniDrawerBackColor != null) {
                crossFader.getSecond().setBackgroundColor(miniDrawerBackColor!!)
            }
            //define a shadow (this is only for normal LTR layouts if you have a RTL app you need to define the other one
            crossFader.getCrossFadeSlidingPaneLayout()
                .setShadowResourceLeft(R.drawable.material_drawer_shadow_left)

            miniResult.withIncludeSecondaryDrawerItems(true)

            for (i in 0 until miniResult.itemAdapter?.itemList?.size()!!) {
                (miniResult.itemAdapter.getAdapterItem(i)!! as MiniDrawerItem).mCustomHeight =
                    DimenHolder.fromPixel(customHeightMiniDrawerItemInDp)
            }
        }
    }

    var lastFocus: View? = null;
    fun keyEvent(event: KeyEvent?): Boolean {

        var foc = activity.currentFocus;

        var right = KeyEvent.KEYCODE_DPAD_RIGHT;
        var left = KeyEvent.KEYCODE_DPAD_LEFT;

        if (isRtl) {
            val temp = right;
            right = left;
            left = temp;
        }

        if (event?.action == KeyEvent.ACTION_DOWN) {
            if (crossFader.isCrossFaded() && (event?.keyCode != right)) {
                return true;
            }
            return false;
        }

        if (foc != null && foc != lastFocus && result.miniDrawer?.recyclerView != foc && result.miniDrawer?.recyclerView?.parent != foc && !crossFader.isCrossFaded()) {
            if (lastFocus != null && foc::class.java == lastFocus!!::class.java) {
                lastFocus = foc;
                return false;
            }
        }
        lastFocus = foc;

        var position = result.currentSelectedPosition;
        var item = result.adapter.getItem(position);

        if (event?.keyCode == left) {
            if (!crossFader.isCrossFaded()) {


                crossFader.crossFade()
                return true;
            }
        } else if (event?.keyCode == right) {
            if (crossFader.isCrossFaded()) {
                if (item is SwitchDrawerItem) {
                    item.withChecked(!item.isChecked)
                    result.adapter.notifyAdapterItemChanged(position);
                } else if (item is ToggleDrawerItem) {
                    item.withChecked(!item.isChecked)
                    result.adapter.notifyAdapterItemChanged(position);
                } else {
                    result.adapter.viewClickListener?.onClick(
                        if (result.recyclerView.getChildAt(
                                position
                            ) == null
                        ) foc else result.recyclerView.getChildAt(position),
                        position,
                        result.adapter,
                        item!!
                    )
                }

                crossFader.crossFade()
                return true;
            }
        } else if (crossFader.isCrossFaded() && event?.keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            while (position + 1 < result.adapter.itemCount) {
                if (result.adapter.getItem(position + 1) is DividerDrawerItem || result.adapter.getItem(
                        position + 1
                    ) is SpaceDrawerItem
                ) {
                    position++;
                } else {
                    val fire: Boolean =
                        if (result.adapter.getItem(position + 1)?.tag != null && result.adapter.getItem(
                                position + 1
                            )?.tag is Boolean
                        ) (result.adapter.getItem(position + 1)?.tag as Boolean) else fireOnClick
                    result.setSelectionAtPosition(position + 1, fire)
                    result.recyclerView.scrollToPosition(position + 1)
                    break
                }
            }
            return true;

        } else if (crossFader.isCrossFaded() && event?.keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            while (position - 1 >= 0) {
                if (result.adapter.getItem(position - 1) is DividerDrawerItem || result.adapter.getItem(
                        position - 1
                    ) is SpaceDrawerItem
                ) {
                    position--;
                } else {
                    val fire: Boolean =
                        if (result.adapter.getItem(position - 1)?.tag != null && result.adapter.getItem(
                                position - 1
                            )?.tag is Boolean
                        ) (result.adapter.getItem(position - 1)?.tag as Boolean) else fireOnClick
                    result.setSelectionAtPosition(position - 1, fire)
                    result.recyclerView.scrollToPosition(position - 1)
                    break
                }
            }

            return true;
        } else if (crossFader.isCrossFaded() && (event?.keyCode == KeyEvent.KEYCODE_ENTER || event?.keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER || event?.keyCode == KeyEvent.KEYCODE_DPAD_CENTER)) {
            if (item is SwitchDrawerItem) {
                item.withChecked(!item.isChecked)
                result.adapter.notifyAdapterItemChanged(position);
            } else if (item is ToggleDrawerItem) {
                item.withChecked(!item.isChecked)
                result.adapter.notifyAdapterItemChanged(position);
            } else {

                result.adapter.viewClickListener?.onClick(
                    if (result.recyclerView.getChildAt(
                            position
                        ) == null
                    ) foc else result.recyclerView.getChildAt(position),
                    position,
                    result.adapter,
                    item!!
                )
            }

            return true;

        } else if (event?.keyCode == KeyEvent.KEYCODE_BACK) {
            if (crossFader.isCrossFaded()) {
                crossFader.crossFade()
                return true
            }
            return false;

        }
        return false;
    }

    fun replaceFragment(fragment: Fragment, tag: String = fragment.javaClass.simpleName) {
        if (frameFragmentRes != null) {

            if (activity.supportFragmentManager.fragments.size > 0) {
                val tx = activity.supportFragmentManager.beginTransaction()
                tx.remove(activity.supportFragmentManager.fragments[0])
                tx.commit()
            }
            val tx = activity.supportFragmentManager.beginTransaction()
            tx.replace(frameFragmentRes!!, fragment, tag)
            tx.commitNowAllowingStateLoss()
        } else {
            throw RuntimeException("You must fill frameFragmentRes for replace fragment in it")
        }
    }

}