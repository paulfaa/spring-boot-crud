package com.paulfaa.crud.util;

import com.paulfaa.crud.entity.Status;

public class Util {
    public static Status findStatusByName(String name) {
        Status result = null;
        for (Status status : Status.values()) {
            if (status.name().equals(name)) {
                result = status;
                break;
            }
        }
        return result;
    }
}
