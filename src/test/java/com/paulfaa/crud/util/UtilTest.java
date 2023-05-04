package com.paulfaa.crud.util;

import com.paulfaa.crud.entity.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.paulfaa.crud.util.Util.findStatusByName;

public class UtilTest {

    @Test
    public void testFindStatusByName(){
        //Act
        Status result = findStatusByName("CREATED");

        //Assert
        Assertions.assertEquals(Status.CREATED, result);
    }

    @Test
    public void testFindStatusByNameInvalidName(){
        //Act
        Status result = findStatusByName("invalid");

        //Assert
        Assertions.assertNull(result);
    }
}
