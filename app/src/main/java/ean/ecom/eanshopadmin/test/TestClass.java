package ean.ecom.eanshopadmin.test;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

import java.util.Scanner;
import java.util.regex.Pattern;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

/**
 * Created by Shailendra (WackyCodes) on 25/09/2020 13:58
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
class TestClass {

    public static void main(String[] agr){

        Scanner scanner = new Scanner( System.in );

        String sc = scanner.nextLine().trim();

        if (isValidWeight( sc )){
            System.out.println( "Matched!" );
        }else{
            System.out.println( "Invalid x Not Match" );
        }

    }
    private static boolean isValidWeight(String val){
        System.out.println( val );
//        String weightPattern = "[0-9]{1,}[.]?[[0-9]{0,2}]{0,2}" + "[x]{0,1}" + "[0-9]+";
//        Pattern pat = Pattern.compile(weightPattern);
//        boolean bool = pat.matcher(val).matches();
//        boolean bool =Pattern.matches( "[0-9]+", val );

//        if (!bool){
//            return false;
//        }

        return true;
    }

}
