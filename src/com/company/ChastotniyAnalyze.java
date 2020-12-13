package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.FileReader;

public class ChastotniyAnalyze {

        public static void analyzeStart() throws Exception {
            char[] lowercase = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'};
            File file = new File("D:\\WarAndPease\\glava2.txt");
            File file2 = new File("D:\\WarAndPease\\glavax.txt");
            File file3 = new File("D:\\WarAndPease\\warandpease.txt");
            LinkedHashMap<Character,Character> shift=caesarShift(lowercase);
            file2 = symbolAnalyze(lowercase, shift, file, file2, file3);
            //System.out.println("bigramm----bigrammbigramm----bigrammbigramm----bigrammbigramm----bigrammbigramm----bigrammbigramm----bigramm");
            //bigramAnalyze(lowercase, shift, file2, file3);

        }

        //анализ при помощи биграмм
        private static void bigramAnalyze(char[] lowercase, LinkedHashMap<Character,Character> shift, File file2, File file3) throws Exception {
            LinkedHashMap<String,Integer> chastotGlavi = new LinkedHashMap<>();
            BufferedReader br = new BufferedReader(new FileReader(file2));
            String st;
            while ((st = br.readLine()) != null){
                chastotGlavi = bigramCounter(st,shift,chastotGlavi);
            }
            //System.out.println(chastotGlavi);
            chastotGlavi=sortBigByVal(chastotGlavi);
            System.out.println(chastotGlavi);
            LinkedHashMap<String,Integer> chastotAlpha = new LinkedHashMap<>();
            br = new BufferedReader(new FileReader(file3));
            st="";
            boolean x = true;
            while (((st = br.readLine()) != null)&&(x)){
                chastotAlpha = bigramCounter(st,shift,chastotAlpha);
                // x=false;
            }
            chastotAlpha=sortBigByVal(chastotAlpha);
            System.out.println(chastotAlpha);
            System.out.println("количество биграмм А: "+chastotGlavi.size()+" Количество биграмм Б "+chastotAlpha.size());

        }
        private static LinkedHashMap<String,Integer> bigramCounter(String str, LinkedHashMap<Character,Character> shift, LinkedHashMap<String,Integer> bufMap){
            //if(str.length()%2==1) str+=" ";
            char[] chArr = str.toCharArray();
            char[] pair = new char[2];
            str="";
            boolean A, B;
            for(int i = 1; i<chArr.length;)
            {

                pair[0]=Character.toLowerCase(chArr[i-1]);
                pair[1]=Character.toLowerCase(chArr[i]);
                A=shift.containsKey(pair[0]);
                B=shift.containsKey(pair[1]);
                if(A){
                    if(B) str=""+pair[0]+pair[1];
                    else str=""+pair[0]+"0";

                    if(i+2<chArr.length) i+=2;
                    else{
                        chArr[i]=' ';
                        i++;
                    }
                    bufMap=putPair(str,bufMap);
                }else i++;
            }
            return bufMap;
        }
        private static LinkedHashMap<String,Integer> putPair(String str, LinkedHashMap<String,Integer> bufMap){
            if(bufMap.containsKey(str)) {
                bufMap.put(str,bufMap.get(str)+1);
            }else{
                bufMap.put(str,1);
            }
            return bufMap;
        }
        private static LinkedHashMap<String,Integer> sortBigByVal(LinkedHashMap<String,Integer> map) {
            map = map.entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors
                            .toMap(Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (a, b) -> a,
                                    LinkedHashMap::new));
            return map;
        }//сортировка MAP

        //посимвольный анализ
        private static File symbolAnalyze(char[] lowercase, LinkedHashMap<Character,Character> shift, File file, File file2, File file3) throws Exception {

            LinkedHashMap<Character,Integer> chastotGlavi = new LinkedHashMap<>();
            LinkedHashMap<Character,Integer> chastotAlpha = new LinkedHashMap<>();
            //обнуление содержимого файла для измененной главы
            writeUsingFileWriter("",file2,false);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null){
                chastotGlavi=caesarCode(st,file2,shift,chastotGlavi,true, lowercase);
            }

            File file4 = new File("D:\\WarAndPease\\scoreswap.txt");
            br = new BufferedReader(new FileReader(file3));
            while ((st = br.readLine()) != null){
                chastotAlpha=caesarCode(st,file4,shift,chastotAlpha,false,lowercase);
            }
            chastotGlavi=sortByVal(chastotGlavi);
            chastotAlpha=sortByVal(chastotAlpha);

            System.out.println(chastotGlavi);
            System.out.println(chastotAlpha);

            shift=backShift(shift, new ArrayList<>(chastotGlavi.keySet()),new ArrayList<>(chastotAlpha.keySet()));
            System.out.println(shift);

            br = new BufferedReader(new FileReader(file2));
            while ((st = br.readLine()) != null){
                backReplace(st,shift, file4);
            }
            return file2;
        }
        private static LinkedHashMap<Character,Character> caesarShift(char[] lowercase) {

            LinkedHashMap<Character, Character> caescode = new LinkedHashMap<>();
            //создание карты сдвига
            for(int i=0;i<lowercase.length;i++)
            {
                if(i<31) caescode.put(lowercase[i],lowercase[i+2]);
                else caescode.put(lowercase[i],lowercase[i-31]);
            }
            return caescode;
        }//создание карты сдвига Цезарем
        public static LinkedHashMap<Character,Integer> caesarCode(String str, File file, Map<Character,Character> shift,LinkedHashMap<Character,Integer> buffMap,boolean vec, char[] lowercase) throws Exception {
            //переводим в массив символов
            char[] chArr = str.toCharArray();
            //обнуляем строку
            str="";
            //преобразование в новою строку
            for(char c:chArr) {

                if(Character.isLetter(c)&&shift.containsKey(Character.toLowerCase(c))){         //если символ буквеннй
                    if(Character.isLowerCase(c) ) { //если символ нижнего регистра и уже содержится такой ключ в карте
                        c=iputCharToMap(c,shift,buffMap,vec);
                    }else if(Character.isUpperCase(c)){   //если регистр верхний и уже содержится в карте
                        c=Character.toUpperCase(iputCharToMap(Character.toLowerCase(c),shift,buffMap,vec));
                    }
                }
                if(vec) str+=c;
            }

            str+="\n";
            if(vec) writeUsingFileWriter(str, file,true);
            else writeUsingFileWriter("", file,false);
            if(buffMap.size()<33)
            {
                for(char k:lowercase){
                    if(!buffMap.containsKey(k)) buffMap.put(k,0);
                }
            }
            return buffMap;
        } //Шифрование Цезарем
        private static char iputCharToMap(char c, Map<Character,Character> shift, LinkedHashMap<Character,Integer> buffMap, boolean vec){
            int k;
            if(vec) c=shift.get(c);
            if(buffMap.containsKey(c)) //если карта главы содержит такой ключ
            {
                k=buffMap.get(c);
                k++;
                buffMap.put(c,k);          //увеличиваем значение в карте главы
            }else{
                buffMap.put(c,1);        //иначе добавляем ключ С и его значение = 1
            }
            return c;
        }//подсчет Символов
        private static LinkedHashMap<Character,Character> backShift(LinkedHashMap<Character,Character> bufMap,ArrayList<Character> arr1,ArrayList<Character> arr2){
            bufMap.clear();
            for(char c:arr1)
            {
                bufMap.put(c,arr2.get(arr1.indexOf(c)));
            }
            return bufMap;
        }
        private static void backReplace(String str,LinkedHashMap<Character,Character> shift, File file) throws Exception {
            char[] chArr = str.toCharArray();
            str="";

            for(char c:chArr) {
                if(shift.containsKey(Character.toLowerCase(c))){         //если символ буквеннй
                    if(Character.isLowerCase(c)) { //если символ нижнего регистра и уже содержится такой ключ в карте
                        c=shift.get(c);
                    }else if(Character.isUpperCase(c)){   //если регистр верхний и уже содержится в карте
                        c=Character.toUpperCase(shift.get(Character.toLowerCase(c)));
                    }
                }
                str+=c;
            }
            str+="\n";
            writeUsingFileWriter(str, file,true);
        }
        //общее
        private static void writeUsingFileWriter(String data, File file, Boolean appe) throws Exception {

            FileWriter fr = null;
            try {
                fr = new FileWriter(file,appe);
                fr.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }//запись в файл
        private static LinkedHashMap<Character,Integer> sortByVal(LinkedHashMap<Character,Integer> map) {
            map = map.entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (a, b) -> a,
                                    LinkedHashMap::new));
            return map;
        }//сортировка MAP

    }


