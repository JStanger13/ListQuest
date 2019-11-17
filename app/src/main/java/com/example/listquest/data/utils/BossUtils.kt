package com.example.listquest.data.utils

import com.example.listquest.R
import kotlin.random.Random

class BossUtils {
    companion object {
        fun generateBoss(): Int{
            return Random.nextInt(11)
        }

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

                else -> ""
            }
            return boss
        }
    }
}