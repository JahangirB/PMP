package com.jahangirb.pmpprep

import com.jahangirb.pmpprep.data.ChatCommand
import com.jahangirb.pmpprep.data.ChatCommands
import com.jahangirb.pmpprep.data.ProgressStore
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.nio.file.Files

/** The chat's "/" commands (index.html §12.2.2, tests/10-ai-assist): built in, the person's own, and synced. */
class ChatCommandsTest {
    private val own = listOf(ChatCommand("agile", "Quiz me on agile."))

    @Test fun theMenuFollowsTheTypedName() {
        assertEquals(listOf("progress", "improve", "plan", "weakest", "agile"), ChatCommands.matching("/", own)!!.map { it.name })
        assertEquals(listOf("plan"), ChatCommands.matching("/PL", own)!!.map { it.name })
        assertNull(ChatCommands.matching("/plan now", own))
        assertNull(ChatCommands.matching("hello", own))
    }

    @Test fun aTypedCommandBecomesItsMessage() {
        assertEquals("Quiz me on agile.", ChatCommands.expand("/agile", own))
        assertEquals(ChatCommands.BUILT_IN[3].text + "\n\nin agile", ChatCommands.expand("/weakest in agile", own))
        assertEquals("/nothing", ChatCommands.expand("/nothing", own))
    }

    @Test fun namesAreCheckedAndTheCommandsSync() {
        assertEquals("/plan is already a command. Pick another name.", ChatCommands.check("plan", "x", own, null).second)
        assertEquals("agile", ChatCommands.check("/Agile", "x", own, "agile").first)
        assertEquals("Use up to 24 letters, digits or hyphens for the name, with no spaces.", ChatCommands.check("two words", "x", own, null).second)
        val s = ProgressStore(Files.createTempDirectory("pmp").toFile())
        s.setChatCommands(own)
        assertEquals("""[{"t":"Quiz me on agile.","n":"agile"}]""".length, s.toJson().getJSONArray("commands").toString().length)
        val back = ProgressStore(Files.createTempDirectory("pmp").toFile())
        back.restore(s.toJson().toString())
        assertEquals(own, back.chatCommands)
        back.setChatCommands(emptyList())
        assertEquals(false, back.toJson().has("commands"))
    }
}
