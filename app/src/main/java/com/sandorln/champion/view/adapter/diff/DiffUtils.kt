package com.sandorln.champion.view.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import com.sandorln.champion.model.ChampionData
import com.sandorln.champion.model.ChampionData.ChampionSkin
import com.sandorln.champion.model.ChampionData.ChampionSpell

object DiffUtils {
    val DIFF_CHAMPION_DATA = object : DiffUtil.ItemCallback<ChampionData>() {
        override fun areItemsTheSame(oldItem: ChampionData, newItem: ChampionData): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ChampionData, newItem: ChampionData): Boolean =
            oldItem == newItem
    }

    val DIFF_CHAMPION_SKIN = object : DiffUtil.ItemCallback<ChampionSkin>() {
        override fun areItemsTheSame(oldItem: ChampionSkin, newItem: ChampionSkin): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ChampionSkin, newItem: ChampionSkin): Boolean = oldItem == newItem
    }

    val DIFF_CHAMPION_SKILL = object : DiffUtil.ItemCallback<ChampionSpell>() {
        override fun areItemsTheSame(oldItem: ChampionSpell, newItem: ChampionSpell): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ChampionSpell, newItem: ChampionSpell): Boolean = oldItem == newItem
    }
}