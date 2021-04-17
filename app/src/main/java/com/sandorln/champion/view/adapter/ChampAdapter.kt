package com.sandorln.champion.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sandorln.champion.R
import com.sandorln.champion.network.LolApiClient
import com.sandorln.champion.model.CharacterData
import com.sandorln.champion.databinding.ItemChampionIconBinding

class ChampAdapter(var championList: List<CharacterData>, var onClickItem: (selectChampion: CharacterData) -> Unit) :
    RecyclerView.Adapter<ChampAdapter.MainChampionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainChampionViewHolder =
        MainChampionViewHolder(ItemChampionIconBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            .apply {
                val layoutParams = itemView.layoutParams
                layoutParams.height = (parent.width / 6.0).toInt()
                itemView.layoutParams = layoutParams
            }

    override fun getItemCount(): Int = championList.size

    override fun onBindViewHolder(holder: MainChampionViewHolder, position: Int) {
        holder.itemView.setOnClickListener { onClickItem(championList[position]) }
        holder.binding.character = championList[position]

        /* 챔피언 버전에 맞게 URL 수정 후 이미지 불러오기 */
        val champVersion = LolApiClient.lolVersion!!.lvCategory.cvChampion

        Glide.with(holder.itemView.context)
            .load("http://ddragon.leagueoflegends.com/cdn/$champVersion/img/champion/${championList[position].cId}.png")
            .placeholder(R.drawable.ic_launcher_foreground)
            .fitCenter()
            .into(holder.binding.imgChampionIcon)

        holder.itemView.requestLayout()
    }

    class MainChampionViewHolder(val binding: ItemChampionIconBinding) : RecyclerView.ViewHolder(binding.root)
}