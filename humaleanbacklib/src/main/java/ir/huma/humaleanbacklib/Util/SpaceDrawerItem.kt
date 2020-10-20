package ir.huma.humaleanbacklib.Util

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.model.AbstractDrawerItem
import ir.huma.humaleanbacklib.R


/**
 * Created by mikepenz on 03.02.15.
 */
open class SpaceDrawerItem : AbstractDrawerItem<SpaceDrawerItem, SpaceDrawerItem.SpaceViewHolder> {
    var size : Int = 108
    override val type: Int
        get() = R.id.material_drawer_item_space

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_space

    constructor(size: Int) : super() {
        this.size = size
    }

    constructor() : super()


    override fun bindView(holder: SpaceViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        val ctx = holder.itemView.context

        //set the identifier from the drawerItem here. It can be used to run tests
        holder.itemView.id = hashCode()

        //define how the divider should look like
        holder.view.isClickable = false
        holder.view.isEnabled = false
        holder.view.minimumHeight = size
//        ViewCompat.setImportantForAccessibility(holder.view, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO)

        //set the color for the divider
//        holder.divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_divider, R.color.material_drawer_divider))

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun getViewHolder(v: View): SpaceViewHolder {
        return SpaceViewHolder(v)
    }

    class SpaceViewHolder internal constructor(internal val view: View) : RecyclerView.ViewHolder(view) {
        internal val divider: View = view.findViewById(R.id.material_drawer_space)
    }
}
