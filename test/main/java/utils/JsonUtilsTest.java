package utils;

import app.model.Contract;
import org.junit.Test;

import static app.utils.JsonUtils.FromJSON;
import static app.utils.JsonUtils.ToJSON;
import static org.junit.Assert.assertEquals;

public class JsonUtilsTest {
    @Test
    public void shouldSerializeToJSON() throws Exception {
        assertEquals("{\"name\":\"abc\",\"names\":[\"hi\",\"hello\"]}", ToJSON(new Contract()));
    }
    @Test
    public void shouldDeserializeFromJSON() throws Exception {
        assertEquals(new Contract(), FromJSON("{\"name\":\"abc\",\"names\":[\"hi\",\"hello\"]}", Contract.class));
    }
}
