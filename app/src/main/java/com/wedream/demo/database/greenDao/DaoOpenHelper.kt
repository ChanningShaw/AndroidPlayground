package com.wedream.demo.database.greenDao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.greenrobot.greendao.database.Database

class DaoOpenHelper : DaoMaster.OpenHelper {
  constructor(context: Context, name: String) : super(context, name) {}

  constructor(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?) : super(context, name, factory) {}

    override fun onUpgrade(db: Database, oldVersion: Int, newVersion: Int) {

    }
}
