import me.phoenixra.atum.core.utils.StringUtils;
import org.junit.jupiter.api.Test;

public class StringTest {
    @Test
    public void testBar() {
        System.out.println(StringUtils.createProgressBar(
                '@',10,0.99,"{#cd10F3}","?!","?"));

    }
}
