package com.limone.limoncher.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.limone.limoncher.R
import com.limone.limoncher.game.account.Account
import com.limone.limoncher.game.account.AccountsManager
import com.limone.limoncher.game.account.getAccountTypeName
import com.limone.limoncher.ui.screens.content.elements.PlayerFace

private val ChromeBg = Color(0xFF171615)
private val SidebarBg = Color(0xFF242321)
private val HeaderBg = Color(0xFF211F1D)
private val TextPrimary = Color(0xFFF4F4F4)
private val TextSecondary = Color(0xFFBDBAB7)
private val Accent = Color(0xFF5EAC32)
private val Divider = Color(0xFF403E3B)

/** Compact Minecraft Launcher-style shell. The content/navigation below it stays native to LimonCher. */
enum class MinecraftTab { PLAY, INSTALLATIONS, MODS, PATCH_NOTES }

@Composable
fun LimonCherChrome(
    selectedTab: MinecraftTab,
    onTabSelected: (MinecraftTab) -> Unit,
    onHome: () -> Unit,
    onSettings: () -> Unit,
    onAccounts: () -> Unit,
    content: @Composable () -> Unit
) {
    val account by AccountsManager.currentAccountFlow.collectAsStateWithLifecycle()
    BoxWithConstraints(Modifier.fillMaxSize().background(ChromeBg)) {
        val compact = maxWidth < 700.dp
        if (compact) {
            Column(Modifier.fillMaxSize()) {
                MobileHeader(account, selectedTab, onAccounts, onHome, onSettings, onTabSelected)
                Box(Modifier.fillMaxSize()) { content() }
            }
        } else {
            Row(Modifier.fillMaxSize()) {
                Sidebar(account, selectedTab, onHome, onTabSelected, onSettings, onAccounts)
                Column(Modifier.fillMaxSize()) {
                    TopHeader(account, selectedTab, onAccounts, onTabSelected)
                    Box(Modifier.fillMaxSize()) { content() }
                }
            }
        }
    }
}

@Composable
private fun Sidebar(
    account: Account?, selectedTab: MinecraftTab, onHome: () -> Unit,
    onTabSelected: (MinecraftTab) -> Unit, onSettings: () -> Unit, onAccounts: () -> Unit
) {
    Column(Modifier.width(190.dp).fillMaxHeight().background(SidebarBg)) {
        Profile(account, onAccounts, Modifier.fillMaxWidth().height(76.dp))
        DividerLine()
        SidebarItem("HOME", R.drawable.ic_home_filled, selectedTab == MinecraftTab.PLAY, onHome)
        SidebarItem("MINECRAFT:\nJAVA EDITION", R.drawable.ic_box,
            selectedTab != MinecraftTab.PLAY, { onTabSelected(MinecraftTab.INSTALLATIONS) })
        Spacer(Modifier.weight(1f))
        SidebarItem("SETTINGS", R.drawable.ic_settings_filled, false, onSettings)
        Text("LimonCher 0.6.3", color = TextSecondary, fontSize = 10.sp,
            modifier = Modifier.padding(14.dp))
    }
}

@Composable
private fun TopHeader(account: Account?, selectedTab: MinecraftTab, onAccounts: () -> Unit,
                      onTabSelected: (MinecraftTab) -> Unit) {
    Row(Modifier.fillMaxWidth().height(78.dp).background(HeaderBg).padding(horizontal = 18.dp),
        verticalAlignment = Alignment.Bottom) {
        Text("MINECRAFT: JAVA EDITION", color = TextPrimary, fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp, modifier = Modifier.padding(bottom = 18.dp, end = 22.dp))
        listOf(
            MinecraftTab.PLAY to "Play",
            MinecraftTab.INSTALLATIONS to "Installations",
            MinecraftTab.MODS to "Mods",
            MinecraftTab.PATCH_NOTES to "Patch Notes"
        ).forEach { (tab, label) ->
            Column(Modifier.height(58.dp).clickable { onTabSelected(tab) }.padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(label, color = if (selectedTab == tab) TextPrimary else TextSecondary,
                    fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 13.sp, modifier = Modifier.padding(bottom = 9.dp))
                Box(Modifier.width(34.dp).height(3.dp).background(if (selectedTab == tab) Accent else Color.Transparent))
            }
        }
        Spacer(Modifier.weight(1f))
        Profile(account, onAccounts, Modifier.widthIn(min = 130.dp, max = 190.dp).height(68.dp))
    }
}

@Composable
private fun MobileHeader(account: Account?, selectedTab: MinecraftTab, onAccounts: () -> Unit,
                         onHome: () -> Unit, onSettings: () -> Unit,
                         onTabSelected: (MinecraftTab) -> Unit) {
    Column(Modifier.fillMaxWidth().background(HeaderBg)) {
        Row(Modifier.fillMaxWidth().height(62.dp).padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("LimonCher", color = TextPrimary, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
            Spacer(Modifier.weight(1f))
            Profile(account, onAccounts, Modifier.widthIn(min = 120.dp, max = 170.dp).height(56.dp))
        }
        Row(Modifier.fillMaxWidth().horizontalScroll(androidx.compose.foundation.rememberScrollState())) {
            listOf(MinecraftTab.PLAY to "Home", MinecraftTab.INSTALLATIONS to "Java Edition",
                MinecraftTab.MODS to "Mods", MinecraftTab.PATCH_NOTES to "Patch Notes").forEach { (tab, label) ->
                Text(label, color = if (selectedTab == tab) TextPrimary else TextSecondary,
                    fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 12.sp, modifier = Modifier.clickable {
                        if (tab == MinecraftTab.PLAY) onHome() else onTabSelected(tab)
                    }.padding(horizontal = 14.dp, vertical = 12.dp))
            }
        }
    }
}

@Composable
private fun Profile(account: Account?, onClick: () -> Unit, modifier: Modifier) {
    Row(modifier.clickable(onClick = onClick).padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        if (account != null) PlayerFace(account = account, avatarSize = 34.dp)
        else Icon(painterResource(R.drawable.ic_add), "Account", tint = Accent, modifier = Modifier.size(30.dp))
        Column(Modifier.weight(1f)) {
            Text(account?.username ?: "Add account", color = TextPrimary, fontSize = 12.sp,
                fontWeight = FontWeight.Bold, maxLines = 1)
            Text(account?.let { getAccountTypeName(it) } ?: "Microsoft / Ely.by / Offline",
                color = TextSecondary, fontSize = 9.sp, maxLines = 1)
        }
    }
}

@Composable
private fun SidebarItem(label: String, icon: Int, selected: Boolean, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().height(56.dp).clickable(onClick = onClick)
        .background(if (selected) Color(0xFF2D2B29) else Color.Transparent),
        verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.width(4.dp).fillMaxHeight().background(if (selected) Accent else Color.Transparent))
        Icon(painterResource(icon), label, tint = if (selected) TextPrimary else TextSecondary,
            modifier = Modifier.padding(start = 12.dp, end = 10.dp).size(22.dp))
        Text(label, color = if (selected) TextPrimary else TextSecondary,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 11.sp, lineHeight = 13.sp)
    }
    DividerLine()
}

@Composable private fun DividerLine() {
    Box(Modifier.fillMaxWidth().height(1.dp).background(Divider))
}
