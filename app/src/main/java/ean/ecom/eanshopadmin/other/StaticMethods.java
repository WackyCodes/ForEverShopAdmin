package ean.ecom.eanshopadmin.other;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;

public class StaticMethods {

    public static void showToast(Context context, String msg){
        Toast.makeText( context, msg, Toast.LENGTH_SHORT ).show();
    }
    public static void showToast(String msg, Context context){
        Toast.makeText( context, msg, Toast.LENGTH_SHORT ).show();
    }

    public static void gotoURL(Context context, String urlLink){
        Uri uri = Uri.parse( urlLink );
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity( intent );
    }

    public static String getCrrDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    public static String getCrrTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    public static String getRandomNumAccordingToDate(){
        Date date =  Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());

        String crrDateDay = simpleDateFormat.format(new Date());

        return crrDateDay;
    }

    public static String getRandomIndex(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String randomIndex = simpleDateFormat.format(new Date()) ;
        return randomIndex;
    }

    public static String getTwoDigitRandom(){
        Random random = new Random();
        // Generate random integers in range 0 to 9999
        int rand_int1 = 0;
        String randNum = "";
        do {
            rand_int1 = random.nextInt(1000);
        }while ( rand_int1 <= 0 );

        if (rand_int1<99){
            rand_int1 = rand_int1*100;
        }

        randNum = String.valueOf( rand_int1 ).substring( 0, 2 );

        return randNum;
    }

    public static String getOTPDigitRandom(){
        Random random = new Random();
        // Generate random integers in range 0 to 9999
        int rand_int1 = 0;
        String randNum = "";
        do {
            rand_int1 = random.nextInt(10000);
        }while ( rand_int1 <= 0 );

        if (rand_int1<9999){
            rand_int1 = rand_int1*100;
        }
        randNum = String.valueOf( rand_int1 ).substring( 0, 4 );

        return randNum;
    }

    public static String getFiveDigitRandom(){
        Random random = new Random();
        // Generate random integers in range 0 to 9999
        int rand_int1 = 0;
        String randNum = "";
        do {
            rand_int1 = random.nextInt(100000);
        }while ( rand_int1 <= 0 );

        if (rand_int1<999){
            rand_int1 = rand_int1*1000;
        }else if (rand_int1<9999){
            rand_int1 = rand_int1*100;
        }else if (rand_int1<=99999){
            rand_int1 = rand_int1*10;
        }
        randNum = String.valueOf( rand_int1 ).substring( 0, 5 );

        return randNum;
    }

//    public static String getRandomShopId(Context context){
//        String shopId = "";
//        if ( CURRENT_CITY_CODE.equals( "BHOPAL" ) ){
//            shopId = "4620" + getFiveDigitRandom();
//            return shopId;
//        }else if ( CURRENT_CITY_CODE.equals( "INDORE" ) ){
//            shopId = "4520" + getFiveDigitRandom();
//            return shopId;
//        }else{
//            Toast.makeText( context, "City not registered yet!", Toast.LENGTH_SHORT ).show();
//            return shopId;
//        }
//    }

    public static String getRandomProductId(Context context){
        if (SHOP_ID != null){
            String productId = SHOP_ID.substring( 4 ) + getFiveDigitRandom();
//        String str1 = SHOP_ID.substring( 4 );
            return productId;
        }else{
            return null;
        }
    }

    // Remove Duplicate from string...
    public static String removeDuplicate(String[] reqString){
        // Remove duplicates...
        LinkedHashSet <String> tagSet = new LinkedHashSet <>( Arrays.asList(reqString));
        StringBuffer stringBuffer = new StringBuffer();
        int tempInd = 0;

        for (String s : tagSet){
            if (s != null){
                stringBuffer.append( s.trim() );
                stringBuffer.append( ", " );
            }
            tempInd++;
        }

        return stringBuffer.toString();
    }

    public static void writeFileInLocal(@NonNull Context context, String fileName, String textMsg){
        try {
//            FileOutputStream fileOS = openFileOutput( fileName, MODE_PRIVATE );
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter( fileOS );
//            outputStreamWriter.write( textMsg );
//            outputStreamWriter.close();
            File folder1 = new File(context.getExternalFilesDir( Environment.getExternalStorageDirectory().getAbsolutePath() ), fileName);
            folder1.mkdirs();
            File pdfFile = new File(folder1, fileName + ".txt");
//            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream( pdfFile );
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter( fileOutputStream );
            outputStreamWriter.write( textMsg );
            outputStreamWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFileFromLocal( @NonNull Context context, String fileName, boolean isChecked){
        StringBuilder fileValue = null;
        File file = new File( context.getExternalFilesDir( Environment.getExternalStorageDirectory().getAbsolutePath() ) + "/" + fileName + "/" + fileName + ".txt" );
        if (file.exists()){
            try {
                FileReader fileReader = new FileReader( file );
                while((fileReader.read( ))!= -1){
                    if (fileValue != null){
                        fileValue.append( (char) fileReader.read() );
                    }else{
                        fileValue = new StringBuilder( String.valueOf( (char) fileReader.read() ) );
                    }
                }

                return fileValue.toString().trim();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }else{
            return null;
        }
    }

    public static String readFileFromLocal( Context context, String fileName ){
        String msg = null;
        try {
            File file = new File( context
                    .getExternalFilesDir( Environment.getExternalStorageDirectory().getAbsolutePath() ), fileName + "/"+ fileName + ".txt");
            if (file.exists()){
                FileInputStream fileIS = new FileInputStream( file );
//            FileInputStream fileIS = openFileInput( fileName );
                InputStreamReader inputStreamReader = new InputStreamReader( fileIS );
                char[] inputBuffer = new char[100];
                int charRead;
                while(( charRead = inputStreamReader.read( inputBuffer )) > 0){
                    String readString = String.copyValueOf( inputBuffer, 0, charRead );
                    if (msg != null){
                        msg += readString;
                    }else{
                        msg = readString;
                    }
                }
                inputStreamReader.close();
            }

        }catch (Exception e){
            showToast( context, e.getMessage() );
        }finally{
            return msg;
        }
    }

    public static String getTimeFromNow( String dateData, String timeData ){
        String timing =  "on " + dateData;
        try
        {
            SimpleDateFormat format = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss", Locale.getDefault());
//            format.applyPattern( "yyyy/MM/ddXHH:mm:ss" );
            Date past = format.parse(dateData + " " + timeData );
            Date now = format.parse( format.format( new Date() ) );
            long diff = now.getTime() - past.getTime();
            /*
            1000 milliseconds = 1 second
            60 seconds = 1 minute
            60 minutes = 1 hour
            24 hours = 1 day
             */
            long seconds = diff / 1000;
            long minutes = diff / (60 * 1000);
            long hours = diff / (60 * 60 * 1000);
            long days = diff / (24 * 60 * 60 * 1000);

//            long seconds= TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
//            long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
//            long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
//            long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if(seconds<=1)
            {
                timing = "Just now";
            }
            else if(seconds<60)
            {
                timing = seconds + " sec ago";
            }
            else if(minutes<60)
            {
                timing = minutes + " Min ago";
            }
            else if(hours<24)
            {
                timing = hours + " hr ago";
            }
            else if (days < 8)
            {
                timing = days + " days ago";
            }
        }
        catch (Exception j){
            j.printStackTrace();
        } finally {
            return timing;
        }
    }

    public static String getDateUnder31( int situation)
    {
         /*
           <item>Today's Order</item>
        <item>Last 2 days</item>
        <item>Last 5 days</item>
        <item>Last 7 days</item>
        <item>Last 30 days</item>
        // Not use yet.!
        <item>Last 2 Months</item>
        <item>Last 6 Months</item>
         */
        String currDate = StaticMethods.getCrrDate(); // "yyyy/MM/dd"
        String[] arr = currDate.split( "/" );
        int yearVal = Integer.valueOf (arr[0] );
        int monthVal = Integer.valueOf (arr[1] );
        int dayVal = Integer.valueOf (arr[2] );
//        int yearVal = Integer.valueOf ( currDate.substring( 0, 4 ) );
//        int monthVal = Integer.valueOf (  currDate.substring( 5, 7 ) );
//        int dayVal = Integer.valueOf( currDate.substring( 8 ) );

        // Suppose .. situation is under 30 Days

        // Check dayVal < situation ( 3 < 4 )
        // dayVal = 29 - (situation - dayVal);

        if ( dayVal <= situation ){
            if (monthVal == 5 || monthVal == 7 || monthVal == 10 || monthVal == 12 ){
                monthVal = monthVal - 1;
                dayVal = 30 - (situation - dayVal);
            }
            else if (monthVal == 3){
                if (yearVal % 4 == 0){
                    dayVal = 29 - (situation - dayVal);
                }else{
                    dayVal = 28 - (situation - dayVal);
                }
                monthVal = monthVal - 1;
            }
            else {
                /*
                        if (monthVal == 1 || monthVal == 2 || monthVal == 4 || monthVal == 6
                     || monthVal == 8 || monthVal == 9 || monthVal == 11){...}
                         */
                if (monthVal == 1){
                    yearVal = yearVal - 1;
                    monthVal = 12;
                }else{
                    monthVal = monthVal - 1;
                }
                dayVal = 31 - (situation - dayVal); // it is included to get data...
            }

            currDate = String.valueOf( yearVal );

            if ( monthVal <= 9 ){
                currDate = currDate + "/0" + monthVal;
            }else{
                currDate = currDate + "/" + monthVal;
            }

            if ( dayVal <= 9 ){
                currDate = currDate + "/0" + dayVal;
            }else {
                currDate = currDate + "/" + dayVal;
            }
//            currDate = yearVal + "/" + monthVal + "/" + dayVal;
            return currDate;
        }
        else{
            dayVal = dayVal - situation;

            currDate = String.valueOf( yearVal );

            if ( monthVal <= 9 ){
                currDate = currDate + "/0" + monthVal;
            }else{
                currDate = currDate + "/" + monthVal;
            }

            if ( dayVal <= 9 ){
                currDate = currDate + "/0" + dayVal;
            }else {
                currDate = currDate + "/" + dayVal;
            }
//            currDate = yearVal + "/" + monthVal + "/" + dayVal;
            return currDate;
        }
        // Do Greater than...
    }


    // Responsive Price...
    public static String getResponsivePrice(String price){
        String returnPrice = "";
        int round = 0;
        char[] val = price.toCharArray();
        for (int i = val.length-1; i>=0; i-- ){
            round++;
            returnPrice = val[i] + returnPrice;
            // Put , or Not...
            if (round%3 == 0 && i!=0){
                returnPrice = "," + returnPrice;
            }else
            if ( i==1 && returnPrice.length() > 3) {
                if (price.length() < 8 && price.length() % 2 == 0) {
                    returnPrice = "," + returnPrice;
                }
            }
        }
        return returnPrice;
    }

    // Weigth Pattern
    public static boolean isValidWeight(EditText editText, Context context){
        String val = editText.getText().toString().trim();
        String weightPattern = "[0-9]{1,}[.]?[0-9]{0,}" + "[x]{0,1}" + "[0-9]+";
        /**
         * Case 1 : 234
         * case 2 : 233.2
         * case 3 : 32x2
         * case 4 : 32.3x2
         */
        Pattern pat = Pattern.compile(weightPattern);
        boolean bool = pat.matcher(val).matches();

        if (TextUtils.isEmpty( val )) {
            editText.setError( "Please Enter Weight! " );
            return false;
        } else if (!bool){
            editText.setError( "Incorrect Weight! " );
            showToast( context, "Please Enter Correct weight! Ex. 367  or  367.3  or  367x7  " );
            return false;
        }

        return true;
    }


}
