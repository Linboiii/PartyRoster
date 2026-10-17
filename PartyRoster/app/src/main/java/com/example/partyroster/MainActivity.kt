package com.example.partyroster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                PartyRosterApp()
            }
        }
    }
}

@Composable
fun PartyRosterApp() {
    var heroList by remember { mutableStateOf(mockHeroList) }
    var selectedFilter by remember { mutableStateOf("All") }
    var expandedHeroId by remember { mutableStateOf<String?>(null) }

    val filterCategories = listOf("All", "Paladin", "Berserker", "Cleric", "Rogue", "Archmage")

    val squadHeroes = remember(heroList) { heroList.filter { it.isInSquad } }
    val squadCount = squadHeroes.size
    val totalSquadCp = remember(squadHeroes) { squadHeroes.sumOf { it.cp } }

    val filteredHeroes = remember(selectedFilter, heroList) {
        if (selectedFilter == "All") {
            heroList
        } else {
            heroList.filter { it.heroClass.equals(selectedFilter, ignoreCase = true) }
        }
    }

    val onHealHero: (String) -> Unit = { id ->
        heroList = heroList.map { hero ->
            if (hero.id == id) {
                val newHp = (hero.currentHp + 250).coerceAtMost(hero.maxHp)
                hero.copy(currentHp = newHp)
            } else {
                hero
            }
        }
    }

    val onToggleSquad: (String) -> Unit = { id ->
        heroList = heroList.map { hero ->
            if (hero.id == id) {
                if (hero.isInSquad) {
                    hero.copy(isInSquad = false)
                } else {
                    if (squadCount < 4) {
                        hero.copy(isInSquad = true)
                    } else {
                        hero
                    }
                }
            } else {
                hero
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFF12141A)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            HeaderSection(totalHeroes = heroList.size)

            Spacer(modifier = Modifier.height(12.dp))

            SquadOverviewCard(
                squadHeroes = squadHeroes,
                squadCount = squadCount,
                totalCp = totalSquadCp,
                onRemoveHero = { onToggleSquad(it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SearchBar()

            Spacer(modifier = Modifier.height(12.dp))

            FilterChipRow(
                filters = filterCategories,
                heroList = heroList,
                selectedFilter = selectedFilter,
                onFilterSelect = { selectedFilter = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredHeroes, key = { it.id }) { hero ->
                    HeroCard(
                        hero = hero,
                        isExpanded = expandedHeroId == hero.id,
                        isSquadFull = squadCount >= 4,
                        onCardClick = {
                            expandedHeroId = if (expandedHeroId == hero.id) null else hero.id
                        },
                        onHealClick = { onHealHero(hero.id) },
                        onToggleSquadClick = { onToggleSquad(hero.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun HeaderSection(totalHeroes: Int) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "★ Expedition", fontSize = 12.sp, color = Color(0xFFFFB74D))
        }
        Text(text = "Party Roster", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(text = "Guild Vanguard • $totalHeroes Heroes Available", fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun SquadOverviewCard(
    squadHeroes: List<Hero>,
    squadCount: Int,
    totalCp: Int,
    onRemoveHero: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E222D)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "EXPEDITION SQUAD ($squadCount/4)", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "⚔ $totalCp CP", fontSize = 14.sp, color = Color(0xFFFFB74D), fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = {},
                    enabled = squadCount in 1..4,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF5252),
                        disabledContainerColor = Color.DarkGray
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Deploy",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (squadCount in 1..4) Color.White else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (squadHeroes.isEmpty()) {
                Text(
                    text = "No heroes deployed to squad yet.",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    squadHeroes.forEach { hero ->
                        SquadMemberAvatar(hero = hero, onClick = { onRemoveHero(hero.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun SquadMemberAvatar(hero: Hero, onClick: () -> Unit) {
    val context = LocalContext.current
    val bitmap = remember(hero.imageRes) {
        loadBitmapFromResource(context, hero.imageRes)
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF141720),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray)
            ) {
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = hero.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(text = hero.name, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = "✕", fontSize = 8.sp, color = Color.Red)
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
fun SearchBar() {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF1E222D),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "🔍", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Search by hero name, class, or rarity...", fontSize = 13.sp, color = Color.Gray)
        }
    }
}

@Composable
fun FilterChipRow(
    filters: List<String>,
    heroList: List<Hero>,
    selectedFilter: String,
    onFilterSelect: (String) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(filters) { filter ->
            val count = if (filter == "All") heroList.size else heroList.count { it.heroClass.equals(filter, ignoreCase = true) }
            val label = "$filter ($count)"
            val isSelected = filter == selectedFilter

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) Color(0xFFFF5252) else Color(0xFF1E222D),
                modifier = Modifier.clickable { onFilterSelect(filter) }
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = if (isSelected) Color.White else Color.Gray,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun HeroCard(
    hero: Hero,
    isExpanded: Boolean,
    isSquadFull: Boolean,
    onCardClick: () -> Unit,
    onHealClick: () -> Unit,
    onToggleSquadClick: () -> Unit
) {
    val context = LocalContext.current
    val bitmap = remember(hero.imageRes) {
        loadBitmapFromResource(context, hero.imageRes)
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E222D)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .animateContentSize()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.DarkGray)
                ) {
                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = hero.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFFB74D).copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = hero.rarity,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFB74D),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = hero.heroClass, fontSize = 11.sp, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(text = hero.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)

                    Spacer(modifier = Modifier.height(4.dp))

                    StatProgressBar(label = "HP", current = hero.currentHp, max = hero.maxHp, barColor = Color(0xFF4CAF50))
                    Spacer(modifier = Modifier.height(3.dp))
                    StatProgressBar(label = "MP", current = hero.currentMp, max = hero.maxMp, barColor = Color(0xFF2196F3))
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Lvl ${hero.level}", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "⚔ ${hero.cp} CP", fontSize = 12.sp, color = Color(0xFFFFB74D), fontWeight = FontWeight.Bold)

                    if (hero.isInSquad) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF4CAF50).copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "IN SQUAD",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4CAF50),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isExpanded) "▲" else "▼",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MiniStatBox("ATK", hero.atk.toString())
                        MiniStatBox("DEF", hero.def.toString())
                        MiniStatBox("CRIT", hero.crit)
                        MiniStatBox("SPD", hero.spd.toString())
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF141720),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = hero.skillName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFB74D))
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = hero.skillDescription, fontSize = 11.sp, color = Color.LightGray, lineHeight = 15.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onHealClick,
                            enabled = hero.currentHp < hero.maxHp,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = if (hero.currentHp >= hero.maxHp) "MAX HP" else "Heal (+250 HP)",
                                fontSize = 11.sp,
                                color = if (hero.currentHp >= hero.maxHp) Color.Gray else Color.White
                            )
                        }

                        Button(
                            onClick = onToggleSquadClick,
                            enabled = hero.isInSquad || !isSquadFull,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (hero.isInSquad) Color(0xFF424242) else Color(0xFFFF5252),
                                disabledContainerColor = Color.DarkGray
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = when {
                                    hero.isInSquad -> "Remove"
                                    isSquadFull -> "Squad Full"
                                    else -> "+ Add to Squad"
                                },
                                fontSize = 11.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatProgressBar(label: String, current: Int, max: Int, barColor: Color) {
    val progress = (current.toFloat() / max.toFloat()).coerceIn(0f, 1f)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.Gray, modifier = Modifier.width(20.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color.Black.copy(alpha = 0.4f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(barColor)
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = "$current/$max", fontSize = 9.sp, color = Color.Gray)
    }
}

@Composable
fun MiniStatBox(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = Color(0xFF141720)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}