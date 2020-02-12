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
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.util.ifNotNull
import com.mikepenz.materialdrawer.util.ifNull
import ir.huma.humaleanbacklib.R
import java.lang.Exception
import java.lang.RuntimeException

class DrawerManager(val activity: FragmentActivity, val result: Drawer) {

    private lateinit var miniResult: MiniDrawer
    private lateinit var crossFader: Crossfader<*>
    public var frameFragmentRes: Int? = null
    public var isRtl = false;
    public var miniDrawerBackColor : Int? = null;
    public var useMiniDrawer: Boolean = true


    fun build() {
        if (useMiniDrawer) {
            miniResult = result.miniDrawer!!

            //get the widths in px for the first and second panel
            val firstWidth = UIUtils.convertDpToPixel(300f, activity).toInt()
            val secondWidth = UIUtils.convertDpToPixel(72f, activity).toInt()

            //create and build our crossfader (see the MiniDrawer is also builded in here, as the build method returns the view to be used in the crossfader)
            //the crossfader library can be found here: https://github.com/mikepenz/Crossfader
            crossFader = Crossfader<CrossFadeSlidingPaneLayout>()
                    .withContent(activity.findViewById<View>(R.id.crossfade_content))
                    .withFirst(result.slider, firstWidth)
                    .withSecond(miniResult.build(activity), secondWidth)
//                    .withSavedInstance(savedInstanceState)
                    .build()

            //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
            miniResult.withCrossFader(CrossfadeWrapper(crossFader))
            if(miniDrawerBackColor != null) {
                crossFader.getSecond().setBackgroundColor(miniDrawerBackColor!!)
            }
            //define a shadow (this is only for normal LTR layouts if you have a RTL app you need to define the other one
            crossFader.getCrossFadeSlidingPaneLayout() .setShadowResourceLeft(R.drawable.material_drawer_shadow_left)
        }
    }
    var lastFocus : View? = null;
    fun keyEvent(event: KeyEvent?): Boolean {

        var foc = activity.currentFocus;

        var right = KeyEvent.KEYCODE_DPAD_RIGHT;
        var left = KeyEvent.KEYCODE_DPAD_LEFT;

        if(isRtl){
            val temp = right;
            right = left;
            left = temp;
        }

        if (event?.action == KeyEvent.ACTION_DOWN) {
            if(crossFader.isCrossFaded() && event?.keyCode != right) {
                return true;
            }
            return false;
        }
        if(foc != lastFocus && result.miniDrawer?.recyclerView != foc && result.miniDrawer?.recyclerView?.parent != foc && !crossFader.isCrossFaded()){
            lastFocus = foc;
            return false;
        }
        lastFocus = foc;

        val position = result.currentSelectedPosition;
        var item = result.adapter.getItem(position);

        if (event?.keyCode == left) {
            if (!crossFader.isCrossFaded()) {
                crossFader.crossFade()
                return true;
            }
        } else if (event?.keyCode == right) {
            if (crossFader.isCrossFaded()) {
                crossFader.crossFade()
                return true;
            }
        } else if (event?.keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (position+2 < result.drawerItems.size && result.drawerItems[result.currentSelectedPosition + 1] is DividerDrawerItem) {
                result.setSelectionAtPosition(result.currentSelectedPosition + 2, false)
            } else if(position+1 < result.drawerItems.size){
                result.setSelectionAtPosition(result.currentSelectedPosition + 1, false)
            }
            return true;

        } else if (event?.keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if ( position-2 >=0 && result.drawerItems[result.currentSelectedPosition - 1] is DividerDrawerItem) {
                result.setSelectionAtPosition(result.currentSelectedPosition - 2, false)
            } else if(position-1 >=0){
                result.setSelectionAtPosition(result.currentSelectedPosition - 1, false)
            }
            return true;
        } else if (event?.keyCode == KeyEvent.KEYCODE_ENTER || event?.keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER || event?.keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            if (item is SwitchDrawerItem) {
                item.withChecked(!item.isChecked)
                result.adapter.notifyAdapterItemChanged(position);
            } else if (item is ToggleDrawerItem) {
                item.withChecked(!item.isChecked)
                result.adapter.notifyAdapterItemChanged(position);
            } else {

                result.adapter.viewClickListener?.onClick(result.recyclerView.get(position), position, result.adapter, item!!)
            }

            return true;

        } else if(event?.keyCode == KeyEvent.KEYCODE_BACK ){
            if(crossFader.isCrossFaded()){
                crossFader.crossFade()
                return true
            }
            return false;

        }
        return false;
    }

    fun replaceFragment(fragment : Fragment) {
        if(frameFragmentRes != null){

            if(activity.supportFragmentManager.fragments.size > 0 ) {
                val tx = activity.supportFragmentManager.beginTransaction()
                tx.remove(activity.supportFragmentManager.fragments[0])
                tx.commit()
            }
            val tx = activity.supportFragmentManager.beginTransaction()
            tx.replace(frameFragmentRes!!, fragment,fragment.javaClass.simpleName)
            tx.commitNowAllowingStateLoss()
        } else {
            throw RuntimeException("You must fill frameFragmentRes for replace fragment in it")
        }
    }

}