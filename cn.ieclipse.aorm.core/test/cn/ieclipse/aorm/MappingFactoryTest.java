package cn.ieclipse.aorm;

import static org.junit.Assert.*;

import org.junit.Test;

public class MappingFactoryTest {
    
    @Test
    public void testPropertyToColumnType() {
        MappingFactory factory = new MappingFactory.DefaultMappingFactory();
        String actual = factory.propertyToColumnType(int.class);
        String expected = "INTEGER";
        assertEquals(expected, actual);
        
        actual = factory.propertyToColumnType(String.class);
        assertEquals("TEXT", actual);
        
        actual = factory.propertyToColumnType(Object.class);
        assertEquals("Object", actual);
    }
    
}
