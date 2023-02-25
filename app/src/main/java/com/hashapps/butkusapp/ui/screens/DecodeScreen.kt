package com.hashapps.butkusapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hashapps.butkusapp.R
import com.hashapps.butkusapp.ui.models.DecodeViewModel
import com.hashapps.butkusapp.ui.theme.ButkusAppTheme

/** The message decoding screen. Consists of:
 * - Text field for the message to decode. Note that before decoding, trailing
 *   tags of the form '#<tag here>' and a single space preceding them will be
 *   stripped off
 * - (If message decoded) The decoded message
 * - Action button (Decode) */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DecodeScreen(
    modifier: Modifier = Modifier,
    vm: DecodeViewModel = DecodeViewModel(),
    butkusInitialized: Boolean,
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ElevatedCard(
            modifier = modifier.fillMaxWidth(),
        ) {
            CompositionLocalProvider(
                LocalTextInputService provides null
            ) {
                OutlinedTextField(
                    modifier = modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    enabled = !vm.uiState.inProgress,
                    value = vm.uiState.message,
                    onValueChange = vm::updateEncodedMessage,
                    singleLine = false,
                    label = { Text(stringResource(R.string.encoded_message_label)) },
                    trailingIcon = {
                        IconButton(
                            enabled = vm.uiState.message.isNotEmpty(),
                            onClick = vm::clearEncodedMessage,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(R.string.clear_plaintext),
                            )
                        }
                    },
                    supportingText = {
                        Text(stringResource(R.string.encoded_message_support))
                    },
                )
            }
        }

        Button(
            modifier = modifier.fillMaxWidth(),
            enabled = butkusInitialized && !vm.uiState.inProgress && vm.uiState.message.isNotEmpty(),
            onClick = vm::decodeMessage,
        ) {
            Text(
                text = stringResource(R.string.decode),
                style = MaterialTheme.typography.titleLarge,
            )
        }

        if (vm.uiState.inProgress) {
            LinearProgressIndicator(modifier = modifier.align(Alignment.CenterHorizontally))
        }

        if (vm.uiState.decodedMessage != null) {
            ElevatedCard(modifier = modifier.fillMaxWidth()) {
                Row(
                    modifier = modifier.padding(8.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        LocalContext.current.resources.getQuantityString(
                        R.plurals.result_length,
                        vm.uiState.decodedMessage!!.length,
                        vm.uiState.decodedMessage!!.length,
                    ))

                    IconButton(
                        onClick = vm::clearDecodedMessage,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.clear_decoded_message)
                        )
                    }
                }

                Divider(thickness = 1.dp)

                SelectionContainer {
                    Text(
                        modifier = modifier.padding(8.dp),
                        text = vm.uiState.decodedMessage!!,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DecodeScreenPreviewDefault() {
    ButkusAppTheme {
        DecodeScreen(butkusInitialized = true)
    }
}