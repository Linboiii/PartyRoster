package com.example.partyroster

import androidx.annotation.DrawableRes

data class Hero(
    val id: String,
    val name: String,
    val heroClass: String,
    val rarity: String,
    val level: Int,
    val cp: Int,
    val currentHp: Int,
    val maxHp: Int,
    val currentMp: Int,
    val maxMp: Int,
    val atk: Int,
    val def: Int,
    val crit: String,
    val spd: Int,
    val skillName: String,
    val skillDescription: String,
    val isInSquad: Boolean = false,
    @DrawableRes val imageRes: Int
)

val mockHeroList = listOf(
    Hero("1", "Alistair", "Paladin", "MYTHIC", 50, 5600, 1650, 1800, 650, 800, 380, 540, "22%", 105, "Wrath of Heavens", "Stuns all enemies for 3 seconds.", false, R.drawable.paladin),
    Hero("2", "Grommash", "Berserker", "LEGENDARY", 50, 5520, 1800, 2400, 300, 500, 710, 310, "35%", 98, "Blood Frenzy", "Consumes 15% HP for massive AoE damage.", false, R.drawable.berserker),
    Hero("3", "Kaelen", "Berserker", "EPIC", 45, 4800, 1950, 2200, 250, 450, 680, 290, "30%", 95, "Whirlwind", "Spins rapidly hitting nearby targets.", false, R.drawable.berserker),
    Hero("4", "Theresa", "Cleric", "MYTHIC", 49, 5480, 1100, 1200, 2100, 2400, 210, 320, "15%", 112, "Divine Grace", "Heals all allies and shields them.", false, R.drawable.cleric),
    Hero("5", "Selene", "Cleric", "RARE", 42, 3900, 950, 1100, 1800, 2000, 190, 280, "10%", 108, "Light Blessing", "Restores MP to party over time.", false, R.drawable.cleric),
    Hero("6", "Vespera", "Rogue", "MYTHIC", 48, 5350, 890, 950, 410, 600, 620, 210, "45%", 135, "Shadow Strike", "Teleports behind target for crit damage.", false, R.drawable.rogue),
    Hero("7", "Kaeleth", "Rogue", "LEGENDARY", 46, 4950, 820, 900, 380, 550, 590, 200, "40%", 130, "Poison Dash", "Inflicts severe damage over time.", false, R.drawable.rogue),
    Hero("8", "Elaria", "Archmage", "MYTHIC", 50, 5120, 850, 900, 2500, 2500, 580, 250, "30%", 118, "Arcane Burst", "Unleashes raw elemental force.", true, R.drawable.archmage),
    Hero("9", "Ignis", "Archmage", "LEGENDARY", 47, 4850, 800, 880, 2200, 2300, 610, 220, "32%", 115, "Inferno Pillar", "Burns enemies in a pillar of fire.", false, R.drawable.archmage),
    Hero("10", "Zephyr", "Archmage", "EPIC", 44, 4300, 780, 850, 2000, 2100, 540, 210, "25%", 120, "Chain Lightning", "Bounces lightning to 4 targets.", false, R.drawable.archmage),
    Hero("11", "Darius", "Paladin", "LEGENDARY", 48, 5050, 1600, 1750, 620, 780, 370, 520, "18%", 100, "Bastion Guard", "Shields adjacent party members.", true, R.drawable.paladin),
    Hero("12", "Torvald", "Berserker", "EPIC", 43, 4200, 1850, 2100, 280, 480, 660, 280, "28%", 92, "Ground Slam", "Stuns enemies in a frontal cone.", false, R.drawable.berserker),
    Hero("13", "Balthazar", "Paladin", "LEGENDARY", 49, 5200, 1550, 1750, 600, 750, 360, 510, "20%", 102, "Holy Aegis", "Gives 50% defense boost to party.", true, R.drawable.paladin),
    Hero("14", "Valerius", "Rogue", "RARE", 40, 3700, 810, 880, 350, 500, 550, 190, "38%", 128, "Evasive Slash", "Strikes target while retreating.", false, R.drawable.rogue)
)