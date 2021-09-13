package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        //Get response from www.cbr.ru
        URL url = new URL("http://www.cbr.ru/scripts/XML_daily.asp");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "Windows-1251"));
        String response = reader.readLine();
        reader.close();

        //Split response XML for different valutes
        String[] stringValuteArray = response.split("<Valute");
        List<Valute> valuteList = new ArrayList<>();

        /*Mapping xml for each valute
        * for {parsingResponseString}
        * 0 - NumCode;
        * 1 - CharCode;
        * 2 - Nominal;
        * 3 - Name;
        * 4 - Value. */
        String toIdStart = "ID=\"";
        String toIdEnd = "\">";
        String[] mappingWords = {"NumCode", "CharCode", "Nominal", "Name", "Value"};
        String[] parsingResponseString = new String[5];

        //Parsing xml to valuteList
        for (int i = 1; i < stringValuteArray.length; i++) {
            Valute someValute = new Valute();

            int indexIdStart = stringValuteArray[i].indexOf(toIdStart) + toIdStart.length();
            int indexIdEnd = stringValuteArray[i].indexOf(toIdEnd);

            someValute.id = stringValuteArray[i].substring(indexIdStart, indexIdEnd);

            for (int j = 0; j < parsingResponseString.length; j++) {
                int indexStart = stringValuteArray[i].indexOf("<" + mappingWords[j] + ">") + mappingWords[j].length() + 2;
                int indexEnd = stringValuteArray[i].indexOf("</" + mappingWords[j] + ">");
                parsingResponseString[j] = stringValuteArray[i].substring(indexStart, indexEnd);
            }

            someValute.numCode = parsingResponseString[0];
            someValute.charCode = parsingResponseString[1];
            someValute.nominal = Integer.parseInt(parsingResponseString[2]);
            someValute.name = parsingResponseString[3];
            someValute.value = Double.parseDouble(parsingResponseString[4].replace(",","."));

            valuteList.add(someValute);
        }

        //Request first Valute
        String firstValuteID = "R01135";
        Valute firstValute = null;
        //Request second Valute
        String secondValuteID = "R01535";
        Valute secondValute = null;

        //Get needed valutes
        for (Valute someValute : valuteList) {
            if (someValute.id.equals(firstValuteID)) firstValute = someValute;

            if (someValute.id.equals(secondValuteID)) secondValute = someValute;
        }

        //Solution
        Double neededValue = ((double) firstValute.nominal / firstValute.value) * (secondValute.value / (double) secondValute.nominal);

        //Result
        System.out.println("1 " + firstValute.charCode + " = " + String.format("%.4f", neededValue) + " " + secondValute.charCode + ".");

    }


}

class Valute {
    String id;

    String numCode;

    String charCode;

    Integer nominal;

    String name;

    Double value;

    @Override
    public String toString() {

        return "ID=" + id + "; "
                + "NumCode=" + numCode + "; "
                + "CharCode=" + charCode + "; "
                + "Nominal=" + nominal + "; "
                + "Name=" + name + "; "
                + "Value=" + value + ".";
    }
}