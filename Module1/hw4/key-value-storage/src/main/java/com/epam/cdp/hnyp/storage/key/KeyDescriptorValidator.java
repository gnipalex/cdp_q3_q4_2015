package com.epam.cdp.hnyp.storage.key;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class KeyDescriptorValidator {
    public static List<String> validate(KeyDescriptor descriptor) {
        List<String> errors = new ArrayList<>();
        if (descriptor.getBlocksCount() < 1) {
            errors.add("blocksCount should be positive");
        }
        if (StringUtils.isEmpty(descriptor.getKey())) {
            errors.add("key should not be empty");
        }
        if (descriptor.getValueLength() < 1) {
            errors.add("valueLength should be positive");
        }
        if (descriptor.getStartBlock() < 0) {
            errors.add("startBlock should be >= 0");
        }
        if (descriptor.getClass() == null) {
            errors.add("class should be specified");
        }
        return errors;
    }
}
