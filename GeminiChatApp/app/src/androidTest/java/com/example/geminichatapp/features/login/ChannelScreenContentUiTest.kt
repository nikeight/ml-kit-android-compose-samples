package com.example.geminichatapp.features.login

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import org.junit.Rule
import org.junit.Test

class ChannelScreenContentUiTest{

    @get:Rule
    val composeTestRule = createComposeRule()

    private val greetingsButton = composeTestRule.onNodeWithText("Hello &\nWelcome")
    private val geminiImage = composeTestRule.onNodeWithTag("gemini_logo_img")
    private val proceedButton = composeTestRule.onNodeWithText("Proceed")
    private val termsAndCondition = composeTestRule.onNodeWithText("Terms and Condition")
    private val instructionText = composeTestRule.onNodeWithText("Please provide your own Gemini API key,\n in order to continue")

    @Test
    fun loginScreenUiComponentTest(){
        composeTestRule.setContent {
            LoginScreen {}
        }

        composeTestRule.onRoot()
            .printToLog("loginScreenRoot")

        listOf(
            greetingsButton,
            geminiImage,
            proceedButton,
            termsAndCondition,
            instructionText
        ).forEach { node ->
            node.assertExists()
        }
    }
}