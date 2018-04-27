package utils;

import app.model.Address;
import app.model.Contract;
import org.junit.Test;

import static app.utils.JsonUtils.FromJSON;
import static app.utils.JsonUtils.ToJSON;
import static org.junit.Assert.assertEquals;

public class JsonUtilsTest {

    public static final Contract CONTRACT = new Contract("abc", new Address("1", "1"), new String[]{"id"});

    @Test
    public void shouldSerializeToJSON() throws Exception {
        assertEquals("{\"name\":\"abc\",\"address\":{\"blockDepth\":\"1\",\"transactionDepth\":\"1\"},\"fields\":[\"id\"]}", ToJSON(CONTRACT));
    }
    @Test
    public void shouldDeserializeFromJSON() throws Exception {
        assertEquals(CONTRACT, FromJSON("{\"name\":\"abc\",\"address\":{\"blockDepth\":\"1\",\"transactionDepth\":\"1\"},\"fields\":[\"id\"]}", Contract.class));
    }
}
