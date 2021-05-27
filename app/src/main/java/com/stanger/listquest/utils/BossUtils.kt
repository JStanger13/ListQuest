package com.stanger.listquest.utils


import com.stanger.listquest.R
import kotlin.random.Random

class BossUtils {
    companion object {
        fun generateBoss(): Int { return Random.nextInt(16)}

        fun changeBossImage(rand: Int): Int {
            return when (rand) {
                0 -> R.drawable.centaur
                1 -> R.drawable.cerberus
                2 -> R.drawable.cthulu
                3 -> R.drawable.dragon
                4 -> R.drawable.minotaur
                5 -> R.drawable.vampire
                6 -> R.drawable.werewolf
                7-> R.drawable.mummy
                8 -> R.drawable.medusa
                9 -> R.drawable.ghost
                10 -> R.drawable.devil
                11 -> R.drawable.goblin
                12 -> R.drawable.kraken
                13 -> R.drawable.knight
                14 -> R.drawable.witch
                15 -> R.drawable.orc
                else -> R.drawable.centaur
            }
        }

        fun getBossHead(rand: Int): Int{
            return when (rand) {
                0 -> R.drawable.centaur_head
                1 -> R.drawable.cerberus_head
                2 -> R.drawable.cthulu_head
                3 -> R.drawable.dragon_head
                4 -> R.drawable.minotaur_head
                5 -> R.drawable.vampire_head
                6 -> R.drawable.werewolf_head
                7 -> R.drawable.mummy_head
                8 -> R.drawable.medusa_head
                9 -> R.drawable.ghost_head
                10 -> R.drawable.devil_head
                11 -> R.drawable.goblin_head
                12 -> R.drawable.kraken_head
                13 -> R.drawable.knight_head
                14 -> R.drawable.witch_head
                15 -> R.drawable.orc_head
                else -> R.drawable.centaur_head
            }
        }


        fun getBossName(rand: Int): String{
            val boss = when (rand) {
                0 -> "Centaur"
                1 -> "Cerberus"
                2 -> "Cthulu"
                3 -> "Dragon"
                4 -> "Minotaur"
                5 -> "Vampire"
                6 -> "Werewolf"
                7-> "Mummy"
                8 -> "Medusa"
                9 -> "Ghost"
                10 -> "Devil"
                11 -> "Goblin"
                12 -> "Kraken"
                13 -> "Knight"
                14 -> "Witch"
                15 -> "Orc"
                else -> ""
            }
            return boss
        }
    }
}