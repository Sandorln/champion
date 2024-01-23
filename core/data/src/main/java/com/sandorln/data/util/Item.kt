package com.sandorln.data.util

import com.sandorln.database.model.ItemEntity
import com.sandorln.model.data.item.ItemData
import com.sandorln.model.type.ItemTagType
import com.sandorln.network.model.NetworkItem

fun ItemEntity.asData(): ItemData = ItemData(
    id = id,
    version = version,
    name = name,
    description = description,
    depth = depth,
    inStore = inStore,
    from = from,
    into = into,
    tags = tags.asItemTagTypeSet(),
    image = image.asData(),
    mapType = maps.asData()
)

fun NetworkItem.asEntity(id: String, version: String): ItemEntity = ItemEntity(
    id = id,
    version = version,
    name = name,
    description = description,
    depth = depth,
    inStore = inStore,
    tags = tags,
    from = from.filterNotNull(),
    into = into.filterNotNull(),
    image = image.asEntity(),
    maps = maps.asMapTypeEntity()
)

fun List<String>.asItemTagTypeSet(): Set<ItemTagType> {
    val tagSet = mutableSetOf<ItemTagType>()

    forEach { value ->
        val lowerValue = value.lowercase()

        /* 예외 또는 중복 태그 처리 */
        when (lowerValue) {
            "spellvamp" -> {
                tagSet.add(ItemTagType.LifeSteal)
                return@forEach
            }
        }

        val itemTagType = runCatching {
            ItemTagType.entries.firstOrNull { itemTagType ->
                lowerValue == itemTagType.name.lowercase()
            }
        }.getOrNull() ?: return@forEach

        tagSet.add(itemTagType)
    }

    return tagSet
}