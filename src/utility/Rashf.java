/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ASammour
 *
 * هذا الكود منشور تحت رخصة المشاع الابداعي لك كامل الحرية في نسخ وإعادة توزيع و
 * تبديل ومشاركة الكود حتى للأغراض التجارية
 *
 */
/**
 * هذا الكود كتب خصيصا ليعمل على ويكيبيديا العربية وتم نقاش آلية عمل هذا البوت
 * عدة مرات في الميدان وحاز على الإذن بالعمل في ويكيبيديا العربية بعد التأكد من
 * سلامة النتائج
 */
public class Rashf {

    public static String getRashf(String content) {
        String finals = "";
        Pattern pattern = Pattern.compile("\\{\\{رشف\\}\\}[.\\s\\S]{1,}\\{\\{رشف\\}\\}");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String localUrl = matcher.group(0);
            finals = finals + localUrl + "\n";
        }
        return finals;
    }

    public static String cleanRashf(String content) {
        Pattern pattern = Pattern.compile("\\{\\{رشف\\}\\}[.\\s\\S]{1,}\\{\\{رشف\\}\\}");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String localUrl = matcher.group(0);
            content = content.replace(localUrl, "");
        }
        return content;
    }

    public static String[] clean(String[] s) {
        for (int i = 0; i < s.length; i++) {
            s[i] = s[i].replace("{{", "");
            s[i] = s[i].replace("}}", "");
        }
        return s;
    }

    /**
     * هل القسم منظور؟.
     * @param content
     * @return 
     */
    public static boolean isSeen(String content) {
        boolean is = false;
        if (content.contains("{{منظور}}")) {
            is = true;
        }
        return is;
    }

    /**
     * هل توجد خلاصة؟.
     * @param content
     * @return 
     */
    public static boolean isWithConclusion(String content) {
        boolean is = false;
        if (content.contains("{{خلاصة")) {
            is = true;
        }
        return is;
    }

    /**
     * لا توجد خلاصة
     * @param content
     * @return 
     */
    public static boolean isWithoutConclusion(String content) {
        boolean is = false;
        if (!content.contains("{{خلاصة")) {
            is = true;
        }
        return is;
    }

    /**
     * قالب:لا للأرشفة
     * @param content
     * @return 
     */
    public static boolean isNoArchive(String content) {
        boolean is = false;
        if (content.contains("{{لا للأرشفة}}")) {
            is = true;
        }
        return is;
    }

    /**
     * قالب:منظور
     * @param content
     * @return 
     */
    public static boolean isResolved(String content) {
        boolean is = false;
        if (content.contains("{{وضع طلب|مقبول}}")
                || content.contains("{{وضع طلب|مرفوض}}")
                || content.contains("{{وضع طلب|1}}")
                || content.contains("{{وضع طلب|0}}")
                || content.contains("{{وضع طلب|2}}")
                || content.startsWith("{{وضع طلب|4")) {
            is = true;
        }
        return is;
    }

    /**
     * جلب اسم نقاش المقالة
     * @param content
     * @return 
     */
    public static String getMovedTitle(String content) {
        String title = "";
        Pattern pattern = Pattern.compile("\\{\\{وضع طلب(.*?)\\}\\}");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            title = matcher.group(0).split("\\|")[2].replace("{{", "").replace("}}", "").trim();
        }
        return title;
    }
    
    
    /**
     * هل من المسموح الأرشفة؟.
     * @param content
     * @return
     * @throws IOException 
     */
    public static boolean isAllowed (String content) throws IOException{
        boolean tf = false;
        if (!isNoArchive(content)){
            tf = true;
        }
        return tf;
    }

    /**
     * تنظيف الأقسام
     * @param m
     * @return 
     */
    public static LinkedHashMap<String, String> cleanSection(Map m) {
        LinkedHashMap mymap = new LinkedHashMap();
        int i = 0;
        int counter = 0;
        for (Iterator it = m.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            if (entry.getKey().contains(".")) {
                i++;
                mymap.put(counter + i, entry.getValue() + ";;;;");
            } else {
                counter = Integer.parseInt(entry.getKey());
                mymap.put(counter + i, entry.getValue());
            }
        }
        return mymap;
    }

}
