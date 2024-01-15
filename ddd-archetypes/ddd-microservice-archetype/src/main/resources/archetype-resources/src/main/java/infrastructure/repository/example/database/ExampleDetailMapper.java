#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infrastructure.repository.example.database;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${package}.infrastructure.repository.example.database.dataobject.ExampleDetail;

public interface ExampleDetailMapper extends BaseMapper<ExampleDetail> {
}
