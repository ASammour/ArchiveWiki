/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ASammour
 * 
 * هذا الكود منشور تحت رخصة المشاع الابداعي
 * لك كامل الحرية في نسخ وإعادة توزيع و تبديل ومشاركة الكود حتى للأغراض التجارية
 *
 */

/**
 * هذا الكود كتب خصيصا ليعمل على ويكيبيديا العربية
 * وتم نقاش آلية عمل هذا البوت عدة مرات في الميدان
 * وحاز على الإذن بالعمل في ويكيبيديا العربية
 * بعد التأكد من سلامة النتائج
 */

public class Dates {

    /**
     * 
     * هذا الكود المخصص للتعامل مع التواريخ داخل النقاشات
     * ويقوم بتحليل التواريخ لمعرفة آخر تاريخ داخل النقاش
     * يمكن لهذا البوت أن يتعرف على التواريخ العربية
     * والتواريخ السريانية المعربة
     */
    
    
    /**
     * يتم هنا قراءة التواريخ داخل كل قسم ومعرفة آخر تاريخ في القسم
     */
    
    public static Date getDates(String contentOfSection) throws ParseException {
        ArrayList a = new ArrayList();

        
        Matcher infoMatcher = Pattern.compile("(?<Day>[0-3]?[0-9]) (?<Month>يناير|فبراير|مارس|أبريل|إبريل|مايو|يونيو|يونيه|يوليو|يوليه|أغسطس|سبتمبر|أكتوبر|نوفمبر|ديسمبر|إبريل) (?<Year>[1-2][0-9][0-9][0-9]) ", Pattern.CANON_EQ).matcher(contentOfSection);
        while (infoMatcher.find()) {
            String f = infoMatcher.group().trim();
            f = f.replace("إبريل", "أبريل");
            f = f.replace("يونيه", "يونيو");
            f = f.replace("يوليه", "يوليو");
            SimpleDateFormat ar = new SimpleDateFormat("dd MMMM yyyy", new Locale("ar"));
            Date startDate = ar.parse(f);
            a.add(startDate);
        }

        infoMatcher = Pattern.compile("(?<Day>[0-3]?[0-9]) (?<Month>كانون الثاني|شباط|آذار|نيسان|أيار|تموز|آب|أيلول|تشرين الأول|تشرين الثاني|كانون الأول|نوار|نوران|حزير|حزيران) (?<Year>[1-2][0-9][0-9][0-9]) ", Pattern.MULTILINE).matcher(contentOfSection);
        while (infoMatcher.find()) {
            String f = infoMatcher.group().trim();
            f = f.replace("حزيران", "حزير");
            f = f.replace("أيار", "نوار");
            SimpleDateFormat ar = new SimpleDateFormat("dd MMMM yyyy", new Locale("ar", "SY"));
            Date startDate = ar.parse(f);
            a.add(startDate);
        }

        if (!a.isEmpty()) {
            //جلب تاريخ آخر تعليق
            return (Date) a.get(a.size() - 1);
        } else {
            return null;
        }
    }

    /***
     * جلب تاريخ الشهر بالنمط الآتي
     * 03/2018
     * أي شهر مارس 
     * في عام 2018
     */
    public static String getDate(Date d) {
        SimpleDateFormat ar = new SimpleDateFormat("MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        Date current = c.getTime();
        System.out.println(current);
        return ar.format(current);
    }

    //التحقق إذا كان التاريخ يتحاوز عدد أيام محددة
    public static boolean getAfterDate(Date d, int period) {
        SimpleDateFormat formattedDate = new SimpleDateFormat("dd MMMM YYYY");
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, period);  // number of days to add      
        Date tomorrow = c.getTime();
        System.out.println(tomorrow);
        return getFullDate().after(tomorrow);
    }

    //التاريخ الكامل يوم شهر سنة
    public static Date getFullDate() {
        SimpleDateFormat ar = new SimpleDateFormat("dd MMMM yyyy", new Locale("ar"));
        Date date = new Date();
        return date;
    }
    
    /**
     * جلب التاريخ العربي للشهر الحالي بالنمط الآتي
     * مارس 2018
     * على سبيل المثال
     * @return 
     */
    public static String getDateAr() {
        SimpleDateFormat ar = new SimpleDateFormat("MMMM yyyy", new Locale("ar"));
        Date date = new Date();
        return ar.format(date);

    }
}
