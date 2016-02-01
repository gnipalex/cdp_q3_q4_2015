package com.epam.cdp.hnyp.architecture.integration.consumer;

import java.text.MessageFormat;
import java.util.Map;

import org.apache.camel.Exchange;

import com.epam.cdp.hnyp.architecture.integration.producer.ArifmeticCommand;

public class ArifmeticCommandBodyTransformer {

    private Map<String, String> operatorMapping;
    
    public void transform(Exchange exchange) {
        ArifmeticCommand command = exchange.getIn().getBody(ArifmeticCommand.class);
        
        String operationToTransform = command.getOperation();
        String transformedOperation = operatorMapping.get(operationToTransform);
        
        assertOperationTransformed(transformedOperation, operationToTransform);
        
        command.setOperation(transformedOperation);
    }

    private void assertOperationTransformed(String transformedOperation, String operationToTransform) {
        if (transformedOperation == null) {
            throw new IllegalArgumentException(MessageFormat.format("operation [{0}] can not be mapped", operationToTransform));
        }
    }

    public void setOperatorMapping(Map<String, String> operatorMapping) {
        this.operatorMapping = operatorMapping;
    }

}
