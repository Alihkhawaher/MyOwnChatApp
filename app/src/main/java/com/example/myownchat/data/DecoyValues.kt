package com.example.myownchat.data

sealed class Settings(val iconName: String, val settingsName: String) {
    object personSetting: Settings("person_icon", "Person")
    object friendsSetting: Settings("friends_icon", "Friends")
    object appStyleSetting: Settings("app_styling_icon", "App styling")
    object batterySetting: Settings("battery_saving_icon", "Battery saving")
    object languageSetting: Settings("language_icon", "Language")
    object helpSetting: Settings("help_icon", "Help")
    object aboutAppSetting: Settings("about_app_icon", "About app")
}

val SETTINGS = listOf(
    Settings.personSetting,
    Settings.friendsSetting,
    Settings.appStyleSetting,
    Settings.batterySetting,
    Settings.languageSetting,
    Settings.helpSetting,
    Settings.aboutAppSetting
)