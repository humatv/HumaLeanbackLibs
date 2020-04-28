package ir.huma.humaleanbacksample

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.mikepenz.materialdrawer.*
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import com.mikepenz.crossfader.Crossfader
import com.mikepenz.crossfader.util.UIUtils
import com.mikepenz.crossfader.view.CrossFadeSlidingPaneLayout
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener
import ir.huma.humaleanbacklib.Util.CrossfadeWrapper

class MenuActivity : FragmentActivity() {

    //save our header or result
    private lateinit var headerResult: AccountHeader
    private lateinit var result: Drawer
    private lateinit var miniResult: MiniDrawer
    private lateinit var crossFader: Crossfader<*>

    companion object {
        private const val PROFILE_SETTING = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        result = DrawerBuilder()
                .withActivity(this)
                .withDrawerGravity(Gravity.RIGHT)
                .withTranslucentStatusBar(false)
//                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        PrimaryDrawerItem().withName("helllo").withIdentifier(1),
                        PrimaryDrawerItem().withName("ali").withBadge("22").withBadgeStyle(BadgeStyle(Color.RED, Color.RED)).withIdentifier(2).withSelectable(false),
                        PrimaryDrawerItem().withName("dskfs").withIdentifier(3),
                        DividerDrawerItem(),
                        SwitchDrawerItem().withName("Switch").withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener),
                        ToggleDrawerItem().withName("Toggle").withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener),
                                ExpandableDrawerItem().withName("Collapsable").withIdentifier(19).withSelectable(false).withSubItems(
                                SecondaryDrawerItem().withName("CollapsableItem").withLevel(2).withIdentifier(2002),
                                SecondaryDrawerItem().withName("CollapsableItem 2").withLevel(2).withIdentifier(2003)
                        )
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                    override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                        if (drawerItem is Nameable<*>) {
                            crossFader?.crossFade()
                            Toast.makeText(this@MenuActivity, drawerItem.name?.getText(this@MenuActivity), Toast.LENGTH_SHORT).show()
                        }
                        return false
                    }
                })
                .withGenerateMiniDrawer(true)
                .withSavedInstance(savedInstanceState)
                // build only the view of the Drawer (don't inflate it automatically in our layout which is done with .build())
                .buildView()


        //the MiniDrawer is managed by the Drawer and we just get it to hook it into the Crossfader
        miniResult = result.miniDrawer!!

        //get the widths in px for the first and second panel
        val firstWidth = UIUtils.convertDpToPixel(300f, this).toInt()
        val secondWidth = UIUtils.convertDpToPixel(72f, this).toInt()

        //create and build our crossfader (see the MiniDrawer is also builded in here, as the build method returns the view to be used in the crossfader)
        //the crossfader library can be found here: https://github.com/mikepenz/Crossfader
        crossFader = Crossfader<CrossFadeSlidingPaneLayout>()
                .withContent(findViewById<View>(R.id.crossfade_content))
                .withFirst(result.slider, firstWidth)
                .withSecond(miniResult.build(this), secondWidth)
                .withSavedInstance(savedInstanceState)
                .build()

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniResult.withCrossFader(CrossfadeWrapper(crossFader))

        //define a shadow (this is only for normal LTR layouts if you have a RTL app you need to define the other one
        crossFader.getCrossFadeSlidingPaneLayout().setShadowResourceLeft(R.drawable.material_drawer_shadow_left)

    }


    private val onCheckedChangeListener = object : OnCheckedChangeListener {
        override fun onCheckedChanged(drawerItem: IDrawerItem<*>, buttonView: CompoundButton, isChecked: Boolean) {
            if (drawerItem is Nameable<*>) {
                Log.i("material-drawer", "DrawerItem: " + (drawerItem as Nameable<*>).name + " - toggleChecked: " + isChecked)
            } else {
                Log.i("material-drawer", "toggleChecked: $isChecked")
            }
        }
    }

    override fun onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (crossFader.isCrossFaded()) {
            crossFader.crossFade()
        } else {
            super.onBackPressed()
        }
    }
}
