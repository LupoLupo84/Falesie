package com.example.falesie.tool

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun EditaTesto(
    testo: String,
    selezioneAttuale: String,
    fontSizeDescrizione: TextUnit = 12.sp,
    startDescrizione: Dp = 5.dp,
    fontSizeTesto: TextUnit = 30.sp,
    startTesto: Dp = 12.dp,
    lineaSingola: Boolean = true,
    //mutableListOf: MutableList<String>
): String {
    //var ritorno = selezionaAttuale
    //var text by remember { mutableStateOf(TextFieldValue("")) }
    var text by remember { mutableStateOf(selezioneAttuale) }
    if (text == "") {
        text = selezioneAttuale
    }

    Column(
        //modifier = Modifier.wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = startDescrizione),
                fontSize = fontSizeDescrizione,
                text = testo
            )
        }
        Row(
            //modifier = Modifier.padding(start = 12.dp)
                    modifier = Modifier.padding(start = startTesto)
        ) {
            //BasicTextField(value = "", onValueChange = {}, Modifier.fillMaxWidth())
            BasicTextField(
                //textStyle = TextStyle.Default.copy(fontSize = 30.sp),
                textStyle = TextStyle.Default.copy(fontSize = fontSizeTesto),
                value = text,
                onValueChange = { newText ->
                    text = newText
                },
                singleLine = lineaSingola,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
            )

        }

        Divider(
            modifier = Modifier.padding(top = 2.dp, bottom = 2.dp),
            thickness = 2.dp
        )
    }


    return text

}


@Composable
fun EditaDropdown(
    testo: String,
    selezioneAttuale: String,
    selezioneMenu: List<String>
): String {
    var ritorno = ""
    Row(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Text(
            modifier = Modifier.padding(start = 5.dp),
            fontSize = 12.sp,
            text = testo
        )
    }
    Row(
        modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Box(

        ) {
            ritorno =
                dropDownMenu(
                    //secondPadding,
                    30,
                    selezioneAttuale,
                    selezioneMenu
                    //mutableListOf("Settore 1", "Settore 2", "Settore 3")
                )
        }
    }

    Divider(
        modifier = Modifier.padding(top = 2.dp, bottom = 2.dp),
        thickness = 2.dp
    )
    return ritorno
}




@Composable
fun dropDownMenu(
    dimensione: Int,
    selezioneAttuale: String,
    settoriFalesia: List<String>
): String {
    var columnHeightDp by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current
    //val settoriFalesia: MutableList<String> = mutableListOf("Settore 1", "Settore 2", "Settore 3")
    val selezione = rememberSaveable { mutableStateOf(selezioneAttuale) }
    //if (selezione.value == "" || selezione.value == "0"){
    if (selezione.value == "") {
        selezione.value = selezioneAttuale
    }

    val apriMenu = rememberSaveable { mutableStateOf(false) }




    LazyColumn(
    ) {
        item {
            Column(
            ) {


                Text(
                    modifier = Modifier.clickable {
                        apriMenu.value = !apriMenu.value
                    },
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = dimensione.sp,
                    text = selezione.value,
                )

            }
        }

        if (apriMenu.value) {
            settoriFalesia.forEach() { settore ->
                item {
                    Text(
                        modifier = Modifier.clickable {
                            apriMenu.value = !apriMenu.value
                            selezione.value = settore
                        }
                        //.background(color = MaterialTheme.colorScheme.secondaryContainer)
                        ,
                        fontSize = (dimensione * 0.7).sp,
                        text = settore

                    )
                }
            }
        }


    }


    return selezione.value

}



@Composable
fun myCheckbox(
    testo: String,
    selezioneAttuale: Boolean,
    fontSizeDescrizione: TextUnit = 16.sp,
    startCheckbox: Dp = 0.dp,
): Boolean {
    var isChecked by remember {mutableStateOf(selezioneAttuale) }


    Column {
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                modifier = Modifier.padding(start = startCheckbox),
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Green, // Colore quando selezionato
                    uncheckedColor = Color.DarkGray, // Colore quando non selezionato
                    checkmarkColor = Color.White // Colore del segno di spunta
                )
            )
            Text(
                fontSize = fontSizeDescrizione,
                text = testo
            )

        }


    }
    Divider(
        modifier = Modifier.padding(top = 2.dp, bottom = 2.dp),
        thickness = 2.dp
    )

    return isChecked
}