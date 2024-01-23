package com.sandorln.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sandorln.design.R
import com.sandorln.design.theme.Colors
import com.sandorln.design.theme.IconSize
import com.sandorln.design.theme.LolChampionTheme
import com.sandorln.design.theme.Radius
import com.sandorln.design.theme.Spacings
import com.sandorln.design.theme.TextStyles

@Composable
fun BaseTextEditor(
    modifier: Modifier = Modifier,
    text: String = "",
    textStyle: TextStyle = TextStyles.Body01,
    hint: String = "",
    isShowRounderLine: Boolean = true,
    onChangeTextListener: (String) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    var textFocus by remember {
        mutableStateOf(false)
    }
    val focusColor = if (textFocus) {
        Colors.BaseColor
    } else {
        Colors.Gray05
    }

    Row(
        modifier = modifier
            .heightIn(min = if (isShowRounderLine) 46.dp else 0.dp)
            .run {
                if (isShowRounderLine) {
                    border(
                        width = 1.dp,
                        color = focusColor,
                        shape = RoundedCornerShape(Radius.Radius02)
                    )
                        .background(
                            color = Colors.Blue06,
                            shape = RoundedCornerShape(Radius.Radius02)
                        )
                        .padding(
                            horizontal = Spacings.Spacing03,
                            vertical = Spacings.Spacing00
                        )
                } else
                    this
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            modifier = Modifier
                .onKeyEvent { event ->
                    if (event.key.nativeKeyCode == android.view.KeyEvent.KEYCODE_BACK) {
                        focusManager.clearFocus()
                        true
                    } else {
                        false
                    }
                }
                .onFocusChanged { textFocus = it.isFocused }
                .weight(1f),
            value = text,
            cursorBrush = SolidColor(Colors.BaseColor),
            onValueChange = onChangeTextListener,
            maxLines = 1,
            keyboardActions = KeyboardActions {
                focusManager.clearFocus()
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            textStyle = textStyle,
            decorationBox = { innerTextField ->
                if (text.isEmpty())
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = hint,
                        color = Colors.Gray05,
                        style = textStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                innerTextField.invoke()
            }
        )

        Icon(
            modifier = Modifier.size(IconSize.LargeSize),
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = null,
            tint = focusColor
        )
    }
}

@Preview
@Composable
fun BaseTextEditorPreview() {
    LolChampionTheme {
        Surface {
            BaseTextEditor(
                hint = "챔피언 검색",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}