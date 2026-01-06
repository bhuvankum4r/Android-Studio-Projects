package com.example.coffeeapp

import android.app.Application
import android.content.ClipData
import android.content.ComponentName
import androidx.window.embedding.RuleController
import androidx.window.embedding.SplitAttributes
import androidx.window.embedding.SplitPairFilter
import androidx.window.embedding.SplitPairRule
import com.example.coffeeapp.Activity.CartActivity
import com.example.coffeeapp.Activity.DetailActivity
import com.example.coffeeapp.Activity.ItemsListActivity
import com.example.coffeeapp.Activity.MainActivity
import com.example.coffeeapp.Activity.SplashActivity

class CoffeeAppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val ruleController = RuleController.getInstance(this)

        val internalFilter = setOf(
            SplitPairFilter(
                ComponentName(this, SplashActivity::class.java),
                ComponentName(this, MainActivity::class.java),
                null
            ),

            SplitPairFilter(
                ComponentName(this, MainActivity::class.java),
                ComponentName(this, ItemsListActivity::class.java),
                null
            ),

            SplitPairFilter(
                ComponentName(this, ItemsListActivity::class.java),
                ComponentName(this, DetailActivity::class.java),
                null
            ),

            SplitPairFilter(
                ComponentName(this, DetailActivity::class.java),
                ComponentName(this, CartActivity::class.java),
                null
            ),

            SplitPairFilter(
                ComponentName(this, MainActivity::class.java),
                ComponentName(this, CartActivity::class.java),
                null
            ),

            SplitPairFilter(
                ComponentName(this, ItemsListActivity::class.java),
                ComponentName(this, CartActivity::class.java),
                null
            ),

            SplitPairFilter(
                ComponentName(this, MainActivity::class.java),
                ComponentName(this, DetailActivity::class.java),
                null
            )
        )

        val splitAttributes = SplitAttributes.Builder().setSplitType(SplitAttributes.SplitType.ratio(0.5f))
            .setLayoutDirection(SplitAttributes.LayoutDirection.LOCALE)
            .build()

        val splitPairRules = SplitPairRule.Builder(internalFilter).setDefaultSplitAttributes(splitAttributes).setClearTop(true).build()

        ruleController.setRules(setOf(splitPairRules))
    }


}