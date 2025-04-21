package com.marcelo.souza.buscar.cep.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.marcelo.souza.buscar.cep.R
import com.marcelo.souza.buscar.cep.presentation.theme.CEPTheme
import com.marcelo.souza.buscar.cep.presentation.theme.TypographySubTitleTemplate
import com.marcelo.souza.buscar.cep.presentation.theme.TypographyTitleTemplate

@Composable
fun TemplateError(
    navController: NavController,
    title: String,
    subTitle: String,
    titleButton: String,
    onAction: (() -> Unit)
) {
    Scaffold(topBar = {
        TopAppBar(
            onClose = {
                navController.popBackStack()
            }
        )
    }) { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
        ) {
            val (titleText, subTitleText, image, button) = createRefs()
            val margin12 = dimensionResource(R.dimen.size_12)
            val margin20 = dimensionResource(R.dimen.size_20)
            val margin30 = dimensionResource(R.dimen.size_30)

            Text(
                text = title,
                style = TypographyTitleTemplate.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .constrainAs(titleText) {
                        top.linkTo(parent.top, margin = margin30)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Text(
                text = subTitle,
                style = TypographySubTitleTemplate.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .constrainAs(subTitleText) {
                        top.linkTo(titleText.bottom, margin = margin20)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Image(
                painter = painterResource(id = R.drawable.erro),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(image) {
                        bottom.linkTo(
                            button.top
                        )
                        top.linkTo(
                            subTitleText.bottom
                        )
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            PrimaryButton(
                onClickBtn = {
                    onAction()
                    navController.popBackStack()
                },
                text = titleButton,
                isLoading = false,
                modifier = Modifier
                    .constrainAs(button) {
                        bottom.linkTo(parent.bottom, margin = margin12)
                        start.linkTo(parent.start, margin = margin30)
                        end.linkTo(parent.end, margin = margin30)
                    }
            )
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
internal fun PreviewTemplateError() {
    CEPTheme {
        TemplateError(
            navController = rememberNavController(),
            title = "Ocorreu um erro!",
            subTitle = "Tente buscar o cep novamente",
            titleButton = "Tentar novamente",
            onAction = {}
        )
    }
}