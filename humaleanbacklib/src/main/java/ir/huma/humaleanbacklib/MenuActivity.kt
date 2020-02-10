package ir.huma.humaleanbacklib

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
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
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

//        val profile = ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460")


        // Create the AccountHeader
//        headerResult = AccountHeaderBuilder()
//                .withActivity(this)
//                .withTranslucentStatusBar(false)
//                .addProfiles(
//                        profile,
//                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
//                        ProfileSettingDrawerItem().withName("Add Account").withDescription("Add new GitHub Account").withIcon(GoogleMaterial.Icon.gmd_add).withIdentifier(PROFILE_SETTING.toLong()),
//                        ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings)
//                )
//                .withOnAccountHeaderListener(object : AccountHeader.OnAccountHeaderListener {
//                    override fun onProfileChanged(view: View?, profile: IProfile<*>, current: Boolean): Boolean {
//                        //sample usage of the onProfileChanged listener
//                        //if the clicked item has the identifier 1 add a new profile ;)
//                        if (profile is IDrawerItem<*> && (profile as IDrawerItem<*>).identifier == PROFILE_SETTING.toLong()) {
//                            val newProfile = ProfileDrawerItem().withNameShown(true).withName("Batman").withEmail("batman@gmail.com").withIcon(resources.getDrawable(R.drawable.profile))
//                            headerResult.profiles?.let {
//                                //we know that there are 2 setting elements. set the new profile above them ;)
//                                headerResult.addProfile(newProfile, it.size - 2)
//                            } ?: headerResult.addProfiles(newProfile)
//                        }
//
//                        //false if you have not consumed the event and it should close the drawer
//                        return false
//                    }
//                })
//                .withSavedInstance(savedInstanceState)
//                .build()

        result = DrawerBuilder()
                .withActivity(this)
                .withDrawerGravity(Gravity.RIGHT)
                .withTranslucentStatusBar(false)
//                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        PrimaryDrawerItem().withName("helllo").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(1),
                        PrimaryDrawerItem().withName("ali").withIcon(FontAwesome.Icon.faw_home).withBadge("22").withBadgeStyle(BadgeStyle(Color.RED, Color.RED)).withIdentifier(2).withSelectable(false),
                        PrimaryDrawerItem().withName("dskfs").withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(3),
                        DividerDrawerItem(),
                        SwitchDrawerItem().withName("Switch").withIcon(GoogleMaterial.Icon.gmd_pan_tool).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener),
                        ToggleDrawerItem().withName("Toggle").withIcon(GoogleMaterial.Icon.gmd_pan_tool).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener),
                                ExpandableDrawerItem().withName("Collapsable").withIcon(GoogleMaterial.Icon.gmd_filter_list).withIdentifier(19).withSelectable(false).withSubItems(
                                SecondaryDrawerItem().withName("CollapsableItem").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_filter_list).withIdentifier(2002),
                                SecondaryDrawerItem().withName("CollapsableItem 2").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_filter_list).withIdentifier(2003)
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
