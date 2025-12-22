package top.writerpass.micromessage.client.navigation.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.micromessage.client.navigation.pages.base.IPage

@Composable
private fun SettingBaseItem(
    label: String,
    description: String? = null,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    endContent: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .let {
                if (onClick != null) {
                    it.clickable(enabled = enabled, onClick = onClick)
                } else it
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge
                )
                if (description != null) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (endContent != null) {
                Spacer(Modifier.width(12.dp))
                endContent()
            }
        }
    }
}

fun LazyListScope.settingItem(
    label: String,
    description: String? = null,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    item {
        SettingBaseItem(
            label = label,
            description = description,
            enabled = enabled,
            onClick = onClick
        )
    }
}

fun LazyListScope.settingSwitch(
    label: String,
    checked: Boolean,
    description: String? = null,
    enabled: Boolean = true,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    item {
        SettingBaseItem(
            label = label,
            description = description,
            enabled = enabled,
            onClick = {
                onClick?.invoke() ?: onCheckedChange?.invoke(!checked)
            },
            endContent = {
                Switch(
                    checked = checked,
                    enabled = enabled,
                    onCheckedChange = onCheckedChange
                )
            }
        )
    }
}

fun LazyListScope.settingToggle(
    label: String,
    checked: Boolean,
    description: String? = null,
    enabled: Boolean = true,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    item {
        SettingBaseItem(
            label = label,
            description = description,
            enabled = enabled,
            onClick =  {
                onClick?.invoke() ?: onCheckedChange?.invoke(!checked)
            },
            endContent = {
                Checkbox(
                    checked = checked,
                    enabled = enabled,
                    onCheckedChange = onCheckedChange
                )
            }
        )
    }
}

fun <T> LazyListScope.settingSingleSelect(
    label: String,
    options: List<T>,
    selected: T,
    optionLabel: (T) -> String = { it.toString() },
    description: String? = null,
    enabled: Boolean = true,
    onSelect: (T) -> Unit
) {
    item {
        var expanded by remember { mutableStateOf(false) }

        SettingBaseItem(
            label = label,
            description = description ?: optionLabel(selected),
            enabled = enabled,
            onClick = { expanded = true },
            endContent = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(optionLabel(option)) },
                            onClick = {
                                expanded = false
                                onSelect(option)
                            }
                        )
                    }
                }
            }
        )
    }
}

fun LazyListScope.settingSectionHeader(
    title: String
) {
    item {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

fun LazyListScope.settingDangerItem(
    label: String,
    description: String? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    item {
        SettingBaseItem(
            label = label,
            description = description,
            enabled = enabled,
            onClick = onClick,
            endContent = null
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
        )
    }
}

fun LazyListScope.settingSlider(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    description: ((Float) -> String)? = null,
    enabled: Boolean = true,
    onValueChange: (Float) -> Unit
) {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge
            )

            if (description != null) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = description(value),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(8.dp))

            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                steps = steps,
                enabled = enabled
            )
        }
    }
}


fun <T> LazyListScope.settingMultiSelect(
    label: String,
    options: List<T>,
    selected: Set<T>,
    optionLabel: (T) -> String = { it.toString() },
    description: String? = null,
    enabled: Boolean = true,
    onChange: (Set<T>) -> Unit
) {
    item {
        var expanded by remember { mutableStateOf(false) }

        SettingBaseItem(
            label = label,
            description = description
                ?: selected.joinToString { optionLabel(it) },
            enabled = enabled,
            onClick = { expanded = true },
            endContent = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        val checked = option in selected
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = checked,
                                        onCheckedChange = null
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(optionLabel(option))
                                }
                            },
                            onClick = {
                                val newSet = selected.toMutableSet()
                                if (checked) newSet.remove(option)
                                else newSet.add(option)
                                onChange(newSet)
                            }
                        )
                    }
                }
            }
        )
    }
}





object SettingsPage : IPage {
    override val routeBase: String
        get() = "settings"
    override val label: String
        get() = "Settings"
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {
            FullSizeBox {
                var notifyEnabled by remember { mutableStateOf(false) }
                var devMode by remember { mutableStateOf(false) }
                var autoLogin by remember { mutableStateOf(false) }
                LazyColumn {
                    settingSectionHeader("General")
                    settingSwitch("Auto Login",autoLogin){
                        autoLogin = !autoLogin
                    }
                    settingItem(
                        label = "账号安全",
                        description = "修改密码、设备管理"
                    ){}

                    settingSwitch(
                        label = "消息通知",
                        checked = notifyEnabled,
                        description = "接收新消息提醒",
                        onCheckedChange = { notifyEnabled = it }
                    )

                    settingToggle(
                        label = "开发者模式",
                        checked = devMode,
                        onCheckedChange = { devMode = it }
                    )

                    settingDangerItem(
                        "Logout",
                        "Press After You Sure"
                    ){}

//                    settingSingleSelect(
//                        label = "主题",
//                        options = ThemeMode.entries,
//                        selected = theme,
//                        optionLabel = { it.displayName },
//                        onSelect = { theme = it }
//                    )
                }

            }
        }
}