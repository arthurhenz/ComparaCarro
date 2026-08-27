package com.auth

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theme.Theme

internal const val TERMS_OF_USE_TEXT =
    """
    Termos de Uso e Política de Privacidade

    Ao utilizar o ComparaCarros, você concorda com os termos descritos a seguir.

    1. Aceitação dos termos
    O uso do aplicativo implica a aceitação integral destes Termos de Uso e da nossa
    Política de Privacidade.

    2. Uso do serviço
    O ComparaCarros oferece informações e comparações de veículos apenas para fins
    informativos, não constituindo garantia de preços, disponibilidade ou condições
    de venda.

    3. Dados pessoais
    As informações fornecidas no cadastro são utilizadas exclusivamente para
    autenticação e personalização da experiência dentro do aplicativo, não sendo
    compartilhadas com terceiros sem consentimento.

    4. Responsabilidades do usuário
    O usuário compromete-se a fornecer informações verdadeiras e a não utilizar o
    aplicativo para fins ilícitos ou que violem direitos de terceiros.

    5. Alterações
    Estes termos podem ser atualizados periodicamente. O uso contínuo do aplicativo
    após alterações constitui aceitação dos novos termos.
    """

@Composable
internal fun TermsOfUseDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Termos de Uso & Privacidade",
                style = Theme.typography.titleLarge,
                color = Theme.colors.textPrimary,
            )
        },
        text = {
            Text(
                text = TERMS_OF_USE_TEXT.trimIndent(),
                style = Theme.typography.bodyMedium,
                color = Theme.colors.textSecondary,
                modifier = Modifier.verticalScroll(rememberScrollState()).heightIn(max = 400.dp),
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Fechar",
                    color = Theme.colors.accentPrimary,
                )
            }
        },
        containerColor = Theme.colors.surface,
    )
}
