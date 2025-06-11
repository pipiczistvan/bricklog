package hu.piware.bricklog.feature.set.presentation.dashboard.utils

import androidx.compose.ui.graphics.Color
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.theme_animal_crossing
import bricklog.composeapp.generated.resources.theme_architecture
import bricklog.composeapp.generated.resources.theme_art
import bricklog.composeapp.generated.resources.theme_botanicals
import bricklog.composeapp.generated.resources.theme_brickheadz
import bricklog.composeapp.generated.resources.theme_city
import bricklog.composeapp.generated.resources.theme_creator
import bricklog.composeapp.generated.resources.theme_dc
import bricklog.composeapp.generated.resources.theme_disney
import bricklog.composeapp.generated.resources.theme_dreamzzz
import bricklog.composeapp.generated.resources.theme_duplo
import bricklog.composeapp.generated.resources.theme_fortnite
import bricklog.composeapp.generated.resources.theme_friends
import bricklog.composeapp.generated.resources.theme_harry_potter
import bricklog.composeapp.generated.resources.theme_icons
import bricklog.composeapp.generated.resources.theme_ideas
import bricklog.composeapp.generated.resources.theme_jurassic_world
import bricklog.composeapp.generated.resources.theme_marvel
import bricklog.composeapp.generated.resources.theme_minecraft
import bricklog.composeapp.generated.resources.theme_name_animal_crossing
import bricklog.composeapp.generated.resources.theme_name_architecture
import bricklog.composeapp.generated.resources.theme_name_art
import bricklog.composeapp.generated.resources.theme_name_botanicals
import bricklog.composeapp.generated.resources.theme_name_brickheadz
import bricklog.composeapp.generated.resources.theme_name_city
import bricklog.composeapp.generated.resources.theme_name_creator
import bricklog.composeapp.generated.resources.theme_name_dc
import bricklog.composeapp.generated.resources.theme_name_disney
import bricklog.composeapp.generated.resources.theme_name_dreamzzz
import bricklog.composeapp.generated.resources.theme_name_duplo
import bricklog.composeapp.generated.resources.theme_name_fortnite
import bricklog.composeapp.generated.resources.theme_name_friends
import bricklog.composeapp.generated.resources.theme_name_harry_potter
import bricklog.composeapp.generated.resources.theme_name_icons
import bricklog.composeapp.generated.resources.theme_name_ideas
import bricklog.composeapp.generated.resources.theme_name_jurassic_world
import bricklog.composeapp.generated.resources.theme_name_marvel
import bricklog.composeapp.generated.resources.theme_name_minecraft
import bricklog.composeapp.generated.resources.theme_name_ninjago
import bricklog.composeapp.generated.resources.theme_name_speed_champions
import bricklog.composeapp.generated.resources.theme_name_star_wars
import bricklog.composeapp.generated.resources.theme_name_super_mario
import bricklog.composeapp.generated.resources.theme_name_technic
import bricklog.composeapp.generated.resources.theme_name_wednesday
import bricklog.composeapp.generated.resources.theme_ninjago
import bricklog.composeapp.generated.resources.theme_speed_champions
import bricklog.composeapp.generated.resources.theme_star_wars
import bricklog.composeapp.generated.resources.theme_super_mario
import bricklog.composeapp.generated.resources.theme_technic
import bricklog.composeapp.generated.resources.theme_wednesday
import hu.piware.bricklog.feature.set.presentation.dashboard.domain.FeaturedTheme

private val architecture = FeaturedTheme(
    imageResId = Res.drawable.theme_architecture,
    contentDescriptionResId = Res.string.theme_name_architecture,
    color = Color(0xFF2D2D2D),
    theme = "Architecture"
)

private val harryPotter = FeaturedTheme(
    imageResId = Res.drawable.theme_harry_potter,
    contentDescriptionResId = Res.string.theme_name_harry_potter,
    color = Color(0xFF002539),
    theme = "Harry Potter"
)

private val icons = FeaturedTheme(
    imageResId = Res.drawable.theme_icons,
    contentDescriptionResId = Res.string.theme_name_icons,
    color = Color(0xFF242424),
    theme = "Icons"
)

private val ideas = FeaturedTheme(
    imageResId = Res.drawable.theme_ideas,
    contentDescriptionResId = Res.string.theme_name_ideas,
    color = Color(0xFF426B38),
    theme = "Ideas"
)

private val city = FeaturedTheme(
    imageResId = Res.drawable.theme_city,
    contentDescriptionResId = Res.string.theme_name_city,
    color = Color(0xFF304A5E),
    theme = "City"
)

private val starWars = FeaturedTheme(
    imageResId = Res.drawable.theme_star_wars,
    contentDescriptionResId = Res.string.theme_name_star_wars,
    color = Color(0xFF121212),
    theme = "Star Wars"
)

private val technic = FeaturedTheme(
    imageResId = Res.drawable.theme_technic,
    contentDescriptionResId = Res.string.theme_name_technic,
    color = Color(0xFF6E2525),
    theme = "Technic"
)

private val disney = FeaturedTheme(
    imageResId = Res.drawable.theme_disney,
    contentDescriptionResId = Res.string.theme_name_disney,
    color = Color(0xFF165947),
    theme = "Disney"
)

private val creator = FeaturedTheme(
    imageResId = Res.drawable.theme_creator,
    contentDescriptionResId = Res.string.theme_name_creator,
    color = Color(0xFFDBA191),
    theme = "Creator"
)

private val minecraft = FeaturedTheme(
    imageResId = Res.drawable.theme_minecraft,
    contentDescriptionResId = Res.string.theme_name_minecraft,
    color = Color(0xFF3E2745),
    theme = "Minecraft"
)

private val ninjago = FeaturedTheme(
    imageResId = Res.drawable.theme_ninjago,
    contentDescriptionResId = Res.string.theme_name_ninjago,
    color = Color(0XFF7A6000),
    theme = "Ninjago"
)

private val superMario = FeaturedTheme(
    imageResId = Res.drawable.theme_super_mario,
    contentDescriptionResId = Res.string.theme_name_super_mario,
    color = Color(0xFF6b0000),
    theme = "Super Mario"
)

private val animalCrossing = FeaturedTheme(
    imageResId = Res.drawable.theme_animal_crossing,
    contentDescriptionResId = Res.string.theme_name_animal_crossing,
    color = Color(0xFF35DB75),
    theme = "Animal Crossing"
)

private val art = FeaturedTheme(
    imageResId = Res.drawable.theme_art,
    contentDescriptionResId = Res.string.theme_name_art,
    color = Color(0xFFB5A95C),
    theme = "Art"
)

private val botanicals = FeaturedTheme(
    imageResId = Res.drawable.theme_botanicals,
    contentDescriptionResId = Res.string.theme_name_botanicals,
    color = Color(0xFF003305),
    theme = "Botanicals"
)

private val brickheadz = FeaturedTheme(
    imageResId = Res.drawable.theme_brickheadz,
    contentDescriptionResId = Res.string.theme_name_brickheadz,
    color = Color(0xFF858585),
    theme = "BrickHeadz"
)

private val dc = FeaturedTheme(
    imageResId = Res.drawable.theme_dc,
    contentDescriptionResId = Res.string.theme_name_dc,
    color = Color(0xFF0A0047),
    theme = "DC Comics Super Heroes"
)

private val dreamzzz = FeaturedTheme(
    imageResId = Res.drawable.theme_dreamzzz,
    contentDescriptionResId = Res.string.theme_name_dreamzzz,
    color = Color(0xFF7D0066),
    theme = "Dreamzzz"
)

private val duplo = FeaturedTheme(
    imageResId = Res.drawable.theme_duplo,
    contentDescriptionResId = Res.string.theme_name_duplo,
    color = Color(0xFF005E06),
    theme = "Duplo"
)

private val fortnite = FeaturedTheme(
    imageResId = Res.drawable.theme_fortnite,
    contentDescriptionResId = Res.string.theme_name_fortnite,
    color = Color(0xFFA38D00),
    theme = "Fortnite"
)

private val friends = FeaturedTheme(
    imageResId = Res.drawable.theme_friends,
    contentDescriptionResId = Res.string.theme_name_friends,
    color = Color(0xFF3A6F7A),
    theme = "Friends"
)

private val jurassicWorld = FeaturedTheme(
    imageResId = Res.drawable.theme_jurassic_world,
    contentDescriptionResId = Res.string.theme_name_jurassic_world,
    color = Color(0xFF524A3E),
    theme = "Jurassic World"
)

private val marvel = FeaturedTheme(
    imageResId = Res.drawable.theme_marvel,
    contentDescriptionResId = Res.string.theme_name_marvel,
    color = Color(0xFF000000),
    theme = "Marvel Super Heroes"
)

private val speedChampions = FeaturedTheme(
    imageResId = Res.drawable.theme_speed_champions,
    contentDescriptionResId = Res.string.theme_name_speed_champions,
    color = Color(0XFF964906),
    theme = "Speed Champions"
)

private val wednesday = FeaturedTheme(
    imageResId = Res.drawable.theme_wednesday,
    contentDescriptionResId = Res.string.theme_name_wednesday,
    color = Color(0XFF2A0242),
    theme = "Wednesday"
)

val FEATURED_THEMES = listOf(
    architecture,
    harryPotter,
    icons,
    ideas,
    city,
    starWars,
    technic,
    disney,
    creator,
    minecraft,
    ninjago,
    superMario,
    //animalCrossing,
    art,
    botanicals,
    brickheadz,
    dc,
    dreamzzz,
    //duplo,
    fortnite,
    //friends,
    jurassicWorld,
    marvel,
    //speedChampions,
    //wednesday
)
