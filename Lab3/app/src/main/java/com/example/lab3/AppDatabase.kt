package com.example.lab3

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabase(context: Context) : SQLiteOpenHelper(context, "products.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE entries (id INTEGER PRIMARY KEY AUTOINCREMENT, product TEXT, brand TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS entries")
        onCreate(db)
    }

    fun insertEntry(product: String, brand: String): Boolean {
        val db = writableDatabase
        val query = "INSERT INTO entries (product, brand) VALUES (?, ?)"
        return try {
            db.execSQL(query, arrayOf(product, brand))
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getAllEntries(): List<Pair<String, String>> {
        val result = mutableListOf<Pair<String, String>>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT product, brand FROM entries", null)
        if (cursor.moveToFirst()) {
            do {
                val product = cursor.getString(0)
                val brand = cursor.getString(1)
                result.add(product to brand)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return result
    }
}
