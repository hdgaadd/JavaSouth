package org.codeman;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RpcRequest implements Serializable {

    private String interfaceName;

    private String methodName;

    private Object[] parameters;

    private Class<?>[] paramTypes;

}
