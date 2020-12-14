package com.cookiejarapps.notes.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.cookiejarapps.notes.Note
import java.util.*


class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    private val hp: HashMap<*, *>? = null
    override fun onCreate(db: SQLiteDatabase) {
        // TODO Auto-generated method stub
        db.execSQL(
            "create table notes " +
                    "(id integer primary key, title text, content text, color integer)"
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS notes")
        onCreate(db)
    }

    fun insertNote(
        title: String?,
        content: String?,
        color: Int?
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("title", title)
        contentValues.put("content", content)
        contentValues.put("color", color)
        db.insert("notes", null, contentValues)
        return true
    }

    fun getData(id: Int): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("select * from notes where id=$id", null)
    }

    fun numberOfRows(): Int {
        val db = this.readableDatabase
        return DatabaseUtils.queryNumEntries(db, NOTES_TABLE_NAME).toInt()
    }

    fun updateNote(
        id: Int?,
        title: String?,
        content: String?,
        color: Int?
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        if(title != null){
            contentValues.put("title", title)
        }
        if(content != null){
            contentValues.put("content", content)
        }
        if(color != null){
            contentValues.put("color", color)
        }
        db.update(
            "notes",
            contentValues,
            "id = ? ",
            arrayOf(Integer.toString(id!!))
        )
        return true
    }

    fun deleteNote(id: Int?): Int {
        val db = this.writableDatabase
        return db.delete(
            "notes",
            "id = ? ", arrayOf(Integer.toString(id!!))
        )
    }

    val allNotes: ArrayList<Note>
        get() {
            val array_list = ArrayList<Note>()

            //hp = new HashMap();
            val db = this.readableDatabase
            val res = db.rawQuery("select * from notes", null)
            res.moveToFirst()
            while (res.isAfterLast == false) {
                array_list.add(Note(res.getInt(res.getColumnIndex(NOTES_COLUMN_ID)), res.getString(res.getColumnIndex(NOTES_COLUMN_TITLE)), res.getString(res.getColumnIndex(
                    NOTES_COLUMN_CONTENT)), res.getInt(res.getColumnIndex(NOTES_COLUMN_COLOR))))
                res.moveToNext()
            }
            return array_list
        }

    companion object {
        const val DATABASE_NAME = "notes.db"
        const val NOTES_TABLE_NAME = "notes"
        const val NOTES_COLUMN_ID = "id"
        const val NOTES_COLUMN_TITLE = "title"
        const val NOTES_COLUMN_CONTENT = "content"
        const val NOTES_COLUMN_COLOR = "color"
    }
}