package ir.huma.humaleanbacksample

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.LayoutDirection
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import ir.huma.humaleanbacklib.Util.DrawerManager
import ir.huma.humaleanbacklib.Util.SpaceDrawerItem

class TestDrawerActivity : FragmentActivity() {

    private lateinit var result: Drawer
    private lateinit var drawerManager: DrawerManager;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL

        result = DrawerBuilder()
                .withActivity(this)
//                .withHeader(R.layout.detail_view_content)
                .withDrawerGravity(Gravity.START)
                .withTranslucentStatusBar(false)
//                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        PrimaryDrawerItem().withName("تست").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(1),
                        SpaceDrawerItem().withIdentifier(332),
                        DividerDrawerItem(),
                        PrimaryDrawerItem().withName("تست").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(1),
                        PrimaryDrawerItem().withName("ali").withIcon(FontAwesome.Icon.faw_home).withBadge("22").withBadgeStyle(BadgeStyle(Color.RED, Color.RED)).withIdentifier(2),
                        PrimaryDrawerItem().withName("dskfs").withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(3),
                        SwitchDrawerItem().withName("Switch").withIcon(GoogleMaterial.Icon.gmd_pan_tool).withChecked(true),
                        ToggleDrawerItem().withName("Toggle").withIcon(GoogleMaterial.Icon.gmd_pan_tool).withChecked(true),
                        ExpandableDrawerItem().withName("Collapsable").withIcon(GoogleMaterial.Icon.gmd_filter_list).withSelectable(false).withIdentifier(19).withSubItems(
                                SecondaryDrawerItem().withName("CollapsableItem").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_filter_list).withIdentifier(2002),
                                SecondaryDrawerItem().withName("CollapsableItem 2").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_filter_list).withIdentifier(2003)
                        )
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                    override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                        if(position == 1){
//                            if(supportFragmentManager.findFragmentByTag((TestBaseBrowseFragment::class).simpleName) != null ){
//
//                            }
                            drawerManager.replaceFragment(TestBaseBrowseFragment())
                        } else if(position == 0){
                            drawerManager.replaceFragment(TestBaseRowsFragment())
                        } else if(position == 2){
                            drawerManager.replaceFragment(TestDetailFragment())
                        }
                        //(drawerItem is Nameable<*>) {
                        //  crossFader?.crossFade()
                        Toast.makeText(this@TestDrawerActivity, "hello", Toast.LENGTH_SHORT).show()
                        //}
                        return false
                    }
                })
                .withGenerateMiniDrawer(true)
                .withSavedInstance(savedInstanceState)
                // build only the view of the Drawer (don't inflate it automatically in our layout which is done with .build())
                .buildView()

//        result.miniDrawer?.withIncludeSecondaryDrawerItems(true)
        drawerManager = DrawerManager(this, result);
        drawerManager.isRtl =true;
        drawerManager.miniDrawerBackColor = Color.DKGRAY
        drawerManager.build()
        drawerManager.frameFragmentRes = ir.huma.humaleanbacklib.R.id.topLayout
        drawerManager.replaceFragment(TestBaseRowsFragment())

//
//            var f = TestBaseBrowseFragment()
//            val tx = supportFragmentManager.beginTransaction()
//            tx.replace(ir.huma.humaleanbacklib.R.id.topLayout, f)
//            tx.commit()

    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        return drawerManager.keyEvent(event) || super.dispatchKeyEvent(event);
    }
}
