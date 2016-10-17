package org.epamqa.training.test;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.DataProvider;

/**
 * Created by AlexSh on 17.10.2016.
 */


public class DataForLogin {
      @DataProvider(name="dataforlogin")
       public static Object [][] createDataForLogin() {
           return new Object[][] {
                   { true, "epam", "1234"},
                   { false, "epam", ""},
                   { false, "", ""},
                   { false, "", "1234" },
                   { false, StringUtils.repeat("qwerty0987", 10), StringUtils.repeat("QWERTY1234",10)},
                   { false, "epam", "12345" },
                   { true, "epam", "1234"}
           };


       }
}
