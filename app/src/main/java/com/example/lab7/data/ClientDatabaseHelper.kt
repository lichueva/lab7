package com.example.lab7.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ClientDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "clientDatabase.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_CLIENTS = "clients"
        private const val COLUMN_ID = "id"
        private const val COLUMN_FIRST_NAME = "firstName"
        private const val COLUMN_LAST_NAME = "lastName"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_ORDERS = "orders"
        private const val COLUMN_DISCOUNTS = "discounts"
        private const val COLUMN_CONTACT_INFO = "contactInfo"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_CLIENTS (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_FIRST_NAME TEXT,
                $COLUMN_LAST_NAME TEXT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_PRICE INTEGER,
                $COLUMN_ORDERS INTEGER,
                $COLUMN_DISCOUNTS TEXT,
                $COLUMN_CONTACT_INFO TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS clients")
        onCreate(db)
    }
}

