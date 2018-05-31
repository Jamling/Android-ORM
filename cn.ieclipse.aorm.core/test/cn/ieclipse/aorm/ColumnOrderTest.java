package cn.ieclipse.aorm;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.List;

import cn.ieclipse.aorm.annotation.Column;
import cn.ieclipse.aorm.annotation.Table;
import junit.framework.TestCase;

public class ColumnOrderTest extends TestCase {
    public void testOrder() {
        List<String> names = Mapping.getInstance().getColumns(null,
                Bean1.class);
                
        assertEquals(Arrays.asList("id, email, name").toString(),
                names.toString());
    }
}

@Table(name = "b1")
class Bean1 {
    @Column(order = 1)
    String name;
    
    @Column(id = true)
    long id;
    @Column(order = 0)
    String email;
}
