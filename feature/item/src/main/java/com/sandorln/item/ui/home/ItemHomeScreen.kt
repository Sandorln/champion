package com.sandorln.item.ui.home

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sandorln.design.component.BaseFilterTag
import com.sandorln.design.component.BaseLazyColumnWithPull
import com.sandorln.design.component.BaseTextEditor
import com.sandorln.design.theme.Colors
import com.sandorln.design.theme.Dimens
import com.sandorln.design.theme.IconSize
import com.sandorln.design.theme.LolChampionThemePreview
import com.sandorln.design.theme.Spacings
import com.sandorln.design.theme.TextStyles
import com.sandorln.model.data.item.ItemData
import com.sandorln.model.data.map.MapType
import com.sandorln.model.type.ItemTagType
import kotlin.math.floor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ItemHomeScreen(
    itemHomeViewModel: ItemHomeViewModel = hiltViewModel()
) {
    val currentItemList by itemHomeViewModel.displayItemList.collectAsState()
    val currentSpriteMap by itemHomeViewModel.currentSpriteMap.collectAsState()
    val uiState by itemHomeViewModel.itemUiState.collectAsState()

    val (bootItemList, notBootItemList) = currentItemList.partition { it.tags.contains(ItemTagType.Boots) }
    val (consumableItemList, normalItemList) = notBootItemList.partition { it.tags.contains(ItemTagType.Consumable) }

    val pullToRefreshState = rememberPullToRefreshState(
        positionalThreshold = Dimens.PullHeight
    )

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading)
            pullToRefreshState.endRefresh()
    }

    LaunchedEffect(pullToRefreshState.isRefreshing) {
        if (pullToRefreshState.isRefreshing)
            itemHomeViewModel.sendAction(ItemHomeAction.RefreshItemData)
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val spanCount = floor(this.maxWidth / IconSize.XXLargeSize).toInt()
        val bootsItemListChunkList = bootItemList.chunked(spanCount)
        val consumableItemChunkList = consumableItemList.chunked(spanCount)
        val normalItemChunkList = normalItemList.chunked(spanCount)

        BaseLazyColumnWithPull(
            pullToRefreshState = pullToRefreshState
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = Spacings.Spacing03,
                            horizontal = Spacings.Spacing05
                        ),
                    verticalArrangement = Arrangement.spacedBy(Spacings.Spacing03)
                ) {
                    ItemNewFilerList(
                        isNewItemSelect = uiState.isSelectNewItem,
                        onToggleNewItemFilter = {
                            itemHomeViewModel.sendAction(ItemHomeAction.ToggleSelectNewItem)
                        }
                    )

                    ItemTagTypeFilerList(
                        selectItemTag = uiState.selectTag,
                        onToggleItemTagTypeFilter = { itemTagType ->
                            val action = ItemHomeAction.ToggleItemTagType(itemTagType)
                            itemHomeViewModel.sendAction(action)
                        }
                    )

                    ItemMapFilerList(
                        isSelectMapType = uiState.isSelectMapType,
                        onClickMapFilterTag = {
                            val action = ItemHomeAction.ChangeMapTypeFilter(it)
                            itemHomeViewModel.sendAction(action)
                        }
                    )
                }
            }

            stickyHeader {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Colors.Blue06.copy(alpha = 1f),
                                    Colors.Blue06.copy(alpha = 0.0f)
                                ),
                                startY = Spacings.Spacing08.value
                            )
                        )
                ) {
                    BaseTextEditor(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = Spacings.Spacing05,
                                vertical = Spacings.Spacing03
                            ),
                        text = uiState.searchKeyword,
                        hint = "아이템 검색",
                        onChangeTextListener = { search ->
                            val action = ItemHomeAction.ChangeItemSearchKeyword(search)
                            itemHomeViewModel.sendAction(action)
                        }
                    )
                }
            }

            /* 장화 아이템 */
            if (bootsItemListChunkList.isNotEmpty())
                baseItemList(
                    title = "장화",
                    spanCount = spanCount,
                    spriteMap = currentSpriteMap,
                    itemChunkList = bootsItemListChunkList
                )

            /* 소모성 아이템 */
            if (consumableItemChunkList.isNotEmpty())
                baseItemList(
                    title = "소모성 아이템",
                    spanCount = spanCount,
                    spriteMap = currentSpriteMap,
                    itemChunkList = consumableItemChunkList
                )

            /* 보통 아이템 */
            if (normalItemChunkList.isNotEmpty())
                baseItemList(
                    title = "일반 아이템",
                    spanCount = spanCount,
                    spriteMap = currentSpriteMap,
                    itemChunkList = normalItemChunkList
                )
        }
    }
}

@Composable
fun ItemIconBody(
    item: ItemData? = null,
    currentSpriteMap: Map<String, Bitmap?> = emptyMap()
) {
    Column(
        modifier = Modifier.width(IconSize.XXLargeSize),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val bitmap = item?.image?.getImageBitmap(currentSpriteMap)
        when {
            bitmap != null -> {
                Image(
                    modifier = Modifier.size(IconSize.XXLargeSize),
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }

            item != null -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(IconSize.XXLargeSize)
                        .padding(Spacings.Spacing00),
                    color = Colors.BaseColor,
                    strokeWidth = 3.dp
                )
            }
        }

        Text(
            modifier = Modifier.padding(vertical = 1.dp),
            text = item?.name ?: "",
            style = TextStyles.Body03.copy(fontSize = 8.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            color = Colors.Gold02
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ItemNewFilerList(
    isNewItemSelect: Boolean = false,
    onToggleNewItemFilter: () -> Unit = {}
) {
    Column {
        Text(
            text = "아이템 목록",
            style = TextStyles.SubTitle02,
            color = Colors.Gold02
        )
        Spacer(modifier = Modifier.height(Spacings.Spacing00))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Spacings.Spacing02),
            verticalArrangement = Arrangement.spacedBy(Spacings.Spacing01)
        ) {
            BaseFilterTag(
                !isNewItemSelect,
                title = "모든 아이템",
                onClickTag = onToggleNewItemFilter
            )
            BaseFilterTag(
                isNewItemSelect,
                title = "새로운 아이템",
                onClickTag = onToggleNewItemFilter
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ItemMapFilerList(
    isSelectMapType: MapType = MapType.ALL,
    onClickMapFilterTag: (mapType: MapType) -> Unit = {}
) {
    Column {
        Text(
            text = "등장 맵",
            style = TextStyles.SubTitle02,
            color = Colors.Gold02
        )
        Spacer(modifier = Modifier.height(Spacings.Spacing00))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Spacings.Spacing02),
            verticalArrangement = Arrangement.spacedBy(Spacings.Spacing01)
        ) {
            MapType.entries.forEach { mapType ->
                BaseFilterTag(
                    isCheck = isSelectMapType == mapType,
                    title = mapType.mapName,
                    onClickTag = {
                        onClickMapFilterTag.invoke(mapType)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ItemTagTypeFilerList(
    selectItemTag: Set<ItemTagType> = emptySet(),
    onToggleItemTagTypeFilter: (itemTagType: ItemTagType) -> Unit = {}
) {
    Column {
        Text(
            text = "아이템 능력",
            style = TextStyles.SubTitle02,
            color = Colors.Gold02
        )
        Spacer(modifier = Modifier.height(Spacings.Spacing00))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Spacings.Spacing02),
            verticalArrangement = Arrangement.spacedBy(Spacings.Spacing01)
        ) {
            ItemTagType.entries.forEach { itemTagType ->
                if (itemTagType == ItemTagType.Boots || itemTagType == ItemTagType.Consumable) return@forEach

                BaseFilterTag(
                    isCheck = selectItemTag.contains(itemTagType),
                    title = itemTagType.typeName,
                    onClickTag = {
                        onToggleItemTagTypeFilter.invoke(itemTagType)
                    }
                )
            }
        }
    }
}

private fun LazyListScope.baseItemList(
    title: String = "제목",
    spanCount: Int = 5,
    spriteMap: Map<String, Bitmap?> = emptyMap(),
    itemChunkList: List<List<ItemData>> = mutableListOf()
) {
    item {
        Text(
            modifier = Modifier.padding(
                start = Spacings.Spacing01,
                top = Spacings.Spacing03,
                bottom = Spacings.Spacing00
            ),
            text = title,
            style = TextStyles.Body02,
            color = Colors.Gray05
        )
    }

    items(itemChunkList.size) { columnIndex ->
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            items(spanCount) { rowIndex ->
                val item = runCatching {
                    itemChunkList[columnIndex][rowIndex]
                }.getOrNull()

                ItemIconBody(
                    item = item,
                    currentSpriteMap = spriteMap
                )
            }
        }
    }
}

@Preview
@Composable
fun ItemFilerListPreview() {
    LolChampionThemePreview {
        ItemMapFilerList()
    }
}

@Preview
@Composable
fun ItemIconBodyPreview() {
    LolChampionThemePreview {
        ItemIconBody()
    }
}