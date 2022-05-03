package org.codeman.beanMapping.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author hdgaadd
 * Created on 2021/12/27
 */
@Mapper
public interface Convert {
    Convert INSTANCE = Mappers.getMapper(Convert.class);
    /*@Mappings({
            @Mapping(source = "a", target = "b"),
    })*/
    B aToB(A a);
}
