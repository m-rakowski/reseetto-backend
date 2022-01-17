import com.bifanas.BifanasBackendApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BifanasBackendApplication.class)
@Transactional
public class BifanasBackendApplicationTests {


    @Test
    public void contextOK() throws Exception {

    }

}
