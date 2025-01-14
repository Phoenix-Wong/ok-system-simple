package ${packageParent}.api.service.impl;

import ${packageParent}.api.feign.${entity}Feign;
import ${packageParent}.api.service.${table.serviceName};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * ${table.comment!} 服务实现类-Feign
 * </p>
 *
 * @author ${author}
 */
@Service
public class ${entity}ServiceFeignImpl implements ${table.serviceName}{

    @Autowired
    private ${entity}Feign ${entity?lower_case}Feign;

}
