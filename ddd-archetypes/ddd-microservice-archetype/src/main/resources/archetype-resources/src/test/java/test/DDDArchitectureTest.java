#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.test;

import io.github.chensheng.dddboot.test.ArchitectureTest;
import org.junit.Test;

public class DDDArchitectureTest {
    @Test
    public void testDDDArchitecture() {
        ArchitectureTest.validateDDD("${package}");
    }
}
