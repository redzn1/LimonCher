package com.limone.limoncher.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.res.painterResource
import com.limone.limoncher.R
import com.limone.limoncher.game.account.Account
import com.limone.limoncher.game.account.AccountsManager
import com.limone.limoncher.ui.screens.content.elements.PlayerFace
import com.limone.limoncher.game.account.getAccountTypeName

private val McChromeBg = Color(0xFF211F1D)
private val McSidebar = Color(0xFF2B2928)
private val McPanel = Color(0xFF171615)
private val McText = Color(0xFFF1F1F1)
private val McMuted = Color(0xFFBDBDBD)
private val McGreen = Color(0xFF58A52C)
private val McLine = Color(0xFF474442)

enum class MinecraftTab {
    PLAY, INSTALLATIONS, REALMS, MODS, SKINS, PATCH_NOTES
}

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

    Row(
        modifier = Modifier.fillMaxSize().background(McPanel)
    ) {
        MinecraftSidebar(
            account = account,
            selectedTab = selectedTab,
            onHome = onHome,
            onTabSelected = onTabSelected,
            onSettings = onSettings,
            onAccounts = onAccounts
        )
        Column(
            modifier = Modifier.fillMaxSize().background(McPanel)
        ) {
            MinecraftTopNav(
                selectedTab = selectedTab,
                account = account,
                onTabSelected = onTabSelected,
                onAccounts = onAccounts
            )
            Box(modifier = Modifier.fillMaxSize().background(McPanel)) {
                content()
            }
        }
    }
}

@Composable
private fun MinecraftSidebar(
    account: Account?,
    selectedTab: MinecraftTab,
    onHome: () -> Unit,
    onTabSelected: (MinecraftTab) -> Unit,
    onSettings: () -> Unit,
    onAccounts: () -> Unit
) {
    Column(
        modifier = Modifier.width(184.dp).fillMaxHeight().background(McSidebar)
    ) {
        ProfileBlock(account, onAccounts)

        SidebarItem("HOME", R.drawable.ic_home_filled, selectedTab == MinecraftTab.PLAY, onHome)
        SidebarItem(
            "MINECRAFT:\nJAVA EDITION",
            R.drawable.ic_box,
            selectedTab != MinecraftTab.PLAY,
            { onTabSelected(MinecraftTab.INSTALLATIONS) }
        )

        Spacer(modifier = Modifier.weight(1f))

        SidebarItem("WHAT'S NEW", R.drawable.ic_assignment_filled, selectedTab == MinecraftTab.PATCH_NOTES) {
            onTabSelected(MinecraftTab.PATCH_NOTES)
        }
        SidebarItem("SETTINGS", R.drawable.ic_settings_filled, false, onSettings)

        Text(
            text = "LimonCher 0.5.6",
            color = McMuted,
            fontSize = 10.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun ProfileBlock(account: Account?, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(72.dp).clickable(onClick = onClick).padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (account != null) {
            PlayerFace(account = account, avatarSize = 38.dp)
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = "Account",
                tint = McGreen,
                modifier = Modifier.size(36.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = account?.username ?: "Add account",
                color = McText,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                fontSize = 13.sp,
                fontFamily = FontFamily.SansSerif
            )
            Text(
                text = account?.let { getAccountTypeName(it) } ?: "Offline / Microsoft / Ely.by",
                color = McMuted,
                maxLines = 1,
                fontSize = 10.sp
            )
        }
    }
    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(McLine))
}

@Composable
private fun SidebarItem(
    label: String,
    icon: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(58.dp).clickable(onClick = onClick).background(
            if (selected) Color(0xFF242321) else Color.Transparent
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.width(4.dp).fillMaxHeight().background(
                if (selected) McGreen else Color.Transparent
            )
        )
        Icon(
            painter = painterResource(icon),
            contentDescription = label,
            tint = if (selected) McText else McMuted,
            modifier = Modifier.padding(start = 12.dp, end = 10.dp).size(23.dp)
        )
        Text(
            text = label,
            color = if (selected) McText else McMuted,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 13.sp,
            fontFamily = FontFamily.SansSerif
        )
    }
    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(McLine.copy(alpha = 0.8f)))
}

@Composable
private fun MinecraftTopNav(
    selectedTab: MinecraftTab,
    account: Account?,
    onTabSelected: (MinecraftTab) -> Unit,
    onAccounts: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(78.dp).background(McChromeBg).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = "MINECRAFT: JAVA EDITION",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 18.dp, end = 20.dp),
            fontFamily = FontFamily.SansSerif
        )

        listOf(
            MinecraftTab.PLAY to "Play",
            MinecraftTab.INSTALLATIONS to "Installations",
            MinecraftTab.REALMS to "Realms",
            MinecraftTab.MODS to "Mods",
            MinecraftTab.SKINS to "Skins",
            MinecraftTab.PATCH_NOTES to "Patch Notes"
        ).forEach { (tab, label) ->
            Column(
                modifier = Modifier.height(56.dp).clickable { onTabSelected(tab) }.padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = label,
                    color = if (selectedTab == tab) Color.White else McMuted,
                    fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Box(
                    modifier = Modifier.width(34.dp).height(3.dp).background(
                        if (selectedTab == tab) McGreen else Color.Transparent
                    )
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (account != null) {
            Row(
                modifier = Modifier.clickable(onClick = onAccounts).padding(vertical = 9.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PlayerFace(account = account, avatarSize = 32.dp)
                Column {
                    Text(
                        text = account.username,
                        color = McText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(
                        text = getAccountTypeName(account),
                        color = McMuted,
                        fontSize = 9.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
