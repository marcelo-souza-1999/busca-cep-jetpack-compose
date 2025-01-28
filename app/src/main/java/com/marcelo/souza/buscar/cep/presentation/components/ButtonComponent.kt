package com.marcelo.souza.buscar.cep.presentation.components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.marcelo.souza.buscar.cep.R
import com.marcelo.souza.buscar.cep.presentation.theme.CEPTheme
import com.marcelo.souza.buscar.cep.presentation.theme.TypographyTitle

@Composable
fun PrimaryButton(
    onClickBtn: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean = false,
    backgroundColor: Color = MaterialTheme.colorScheme.primary
) {
    Button(
        onClick = {
            if (!isLoading) onClickBtn.invoke()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        modifier = modifier,
        shape = RectangleShape
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = dimensionResource(R.dimen.size_2),
                modifier = Modifier.size(dimensionResource(R.dimen.size_20))
            )
        } else {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                style = TypographyTitle.titleLarge
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun PreviewShowButton() {
    CEPTheme {
        val context = LocalContext.current

        PrimaryButton(
            onClickBtn = {
                Log.d("BtnClick", "Botão clicado")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.size_20)),
            text = context.getString(R.string.text_btn_search_cep),
            isLoading = false,
            backgroundColor = MaterialTheme.colorScheme.primary
        )
    }
}