
package com.example.contacts

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Contact::class], version = 2, exportSchema = true)
abstract class ContactDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    companion object {
        /**
         * Migration from v1 (columns: image, phoneNumber) -> v2 (columns: imageUri, phone)
         * We recreate the table and copy data across with column mapping.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Create the new table with desired schema
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS contacts_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        imageUri TEXT,
                        name TEXT NOT NULL,
                        phone TEXT,
                        email TEXT
                    )
                    """.trimIndent()
                )

                // Copy data from old columns to new columns
                // NOTE: old columns assumed: image, phoneNumber
                db.execSQL(
                    """
                    INSERT INTO contacts_new (id, imageUri, name, phone, email)
                    SELECT id, image, name, phoneNumber, email
                    FROM contacts
                    """.trimIndent()
                )

                // Drop old table and rename new table
                db.execSQL("DROP TABLE contacts")
                db.execSQL("ALTER TABLE contacts_new RENAME TO contacts")
            }
        }

        /**
         * Centralized builder to ensure both app & provider use the SAME configuration.
         */
        fun build(context: Context): ContactDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ContactDatabase::class.java,
                "contacts.db"
            )
                .addMigrations(MIGRATION_1_2)     // <-- Proper migration
                // .fallbackToDestructiveMigration()  // <-- Uncomment during dev ONLY if you want to wipe DB
                .build()
        }
    }
}
