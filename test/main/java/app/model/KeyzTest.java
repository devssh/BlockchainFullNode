package app.model;

import org.junit.Test;

import java.util.Base64;

import static app.model.Keyz.GenerateSeed;
import static app.model.Keyz.GenerateKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class KeyzTest {
    @Test
    public void shouldReturnDeterministicKeys() throws Exception {
        String seedString = GenerateSeed();
        Keyz random1 = GenerateKey(seedString);
        Keyz random2 = GenerateKey(seedString);
        assertEquals(random1, random2);
    }

    @Test
    public void shouldGenerateSeedAndKeysOfProperLengthAndRandomness() throws Exception {
        assertEquals(684, GenerateSeed().length());
        assertEquals(512, Base64.getDecoder().decode(GenerateSeed()).length);

        assertNotEquals(GenerateSeed(), GenerateSeed());

        assertEquals(192, GenerateKey(GenerateSeed()).privateKey.length());
        assertEquals(120, GenerateKey(GenerateSeed()).publicKey.length());

        assertNotEquals(GenerateKey(GenerateSeed()), GenerateKey(GenerateSeed()));
    }
}
