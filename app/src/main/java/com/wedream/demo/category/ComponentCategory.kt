package com.wedream.demo.category

import android.content.ComponentName
import android.os.Parcel
import android.os.Parcelable

class ComponentCategory(val componentName: ComponentName) :
    Category(componentName.shortClassName.split(".").last()), Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(componentName, 0)
        super.writeToParcel(parcel, flags)
    }

    companion object CREATOR : Parcelable.Creator<ComponentCategory> {
        override fun createFromParcel(parcel: Parcel): ComponentCategory {
            val componentName =
                parcel.readParcelable<ComponentName>(ComponentName::class.java.classLoader)!!
            val name = parcel.readString() ?: "Unknown"
            val category = ComponentCategory(componentName)
            val size = parcel.readInt()
            if (size > 0) {
                parcel.readParcelable<Category>(Category::class.java.classLoader)?.let {
                    category.children.add(it)
                }
            }
            return category
        }

        override fun newArray(size: Int): Array<ComponentCategory?> {
            return arrayOfNulls(size)
        }
    }
}