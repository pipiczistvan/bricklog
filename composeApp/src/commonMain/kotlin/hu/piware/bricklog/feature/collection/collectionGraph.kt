package hu.piware.bricklog.feature.collection

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.presentation.collection_edit.CollectionEditScreenRoot
import hu.piware.bricklog.feature.collection.presentation.collection_list.CollectionListScreenRoot
import hu.piware.bricklog.feature.collection.presentation.collection_share_edit.CollectionShareEditArguments
import hu.piware.bricklog.feature.collection.presentation.collection_share_edit.CollectionShareEditScreenRoot
import hu.piware.bricklog.feature.core.presentation.navigation.CustomNavType
import hu.piware.bricklog.feature.set.presentation.SetRoute
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

sealed interface CollectionRoute {
    @Serializable
    data object Graph : CollectionRoute

    @Serializable
    data class CollectionEditScreen(val collectionId: CollectionId?) : CollectionRoute

    @Serializable
    data class CollectionShareEditScreen(
        val arguments: CollectionShareEditArguments,
    ) : CollectionRoute

    @Serializable
    data object CollectionListScreen : CollectionRoute
}

fun NavGraphBuilder.collectionGraph(navController: NavHostController) {
    navigation<CollectionRoute.Graph>(
        startDestination = CollectionRoute.CollectionEditScreen(null),
    ) {
        composable<CollectionRoute.CollectionEditScreen> {
            CollectionEditScreenRoot(
                onBackClick = {
                    navController.navigateUp()
                },
                onCollectionDeleted = {
                    navController.popBackStack(SetRoute.SetListScreen::class, true)
                },
                onCollectionShareEditClick = { collectionId, userId ->
                    navController.navigate(
                        CollectionRoute.CollectionShareEditScreen(
                            CollectionShareEditArguments(
                                collectionId = collectionId,
                                userId = userId,
                            ),
                        ),
                    )
                },
            )
        }
        composable<CollectionRoute.CollectionShareEditScreen>(
            typeMap = mapOf(
                typeOf<CollectionShareEditArguments>() to CustomNavType.CollectionShareEditArgumentsType,
            ),
        ) {
            CollectionShareEditScreenRoot(
                onBackClick = {
                    navController.navigateUp()
                },
            )
        }
        composable<CollectionRoute.CollectionListScreen> {
            CollectionListScreenRoot(
                onBackClick = {
                    navController.navigateUp()
                },
                onCollectionEditClick = { collectionId ->
                    navController.navigate(CollectionRoute.CollectionEditScreen(collectionId))
                },
                onCollectionClick = { collectionId ->
                    navController.navigate(
                        SetRoute.SetListScreen(
                            SetListArguments.Collection(collectionId),
                        ),
                    )
                },
            )
        }
    }
}
