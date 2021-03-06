package com.wedream.demo.category

import android.content.ComponentName
import android.os.Parcel
import android.os.Parcelable
import com.wedream.demo.app.ApplicationHolder

open class Category(val name: String) : Parcelable {
    val children = ArrayList<Category>()
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(children.size)
        if (children.size > 0) {
            for (child in children) {
                parcel.writeParcelable(child, 0)
            }
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            val name = parcel.readString() ?: "Unknown"
            val category = Category(name)
            val size = parcel.readInt()
            if (size > 0) {
                parcel.readParcelable<Category>(Category::class.java.classLoader)?.let {
                    category.children.add(it)
                }
            }
            return category
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }

    fun addCategory(c: Category): Category {
        children.add(c)
        return this
    }

    fun addComponentCategory(c: ComponentName): Category {
        children.add(ComponentCategory(c))
        return this
    }

    fun addComponentCategory(clazz: Class<*>): Category {
        val c = ComponentName(ApplicationHolder.instance, clazz)
        children.add(ComponentCategory(c))
        return this
    }

    fun addComponentCategories(classes: List<Class<*>>): Category {
        for (clazz in classes) {
            addComponentCategory(clazz)
        }
        return this
    }
}