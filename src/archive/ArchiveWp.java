/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archive;

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
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import org.wikipedia.Wiki;
import utility.Dates;
import utility.Rashf;
import static utility.Rashf.cleanRashf;
import static utility.Rashf.isAllowed;

/**
 *
 * @author ASammour
 */
public class ArchiveWp {


    /*
    هذا الملف الخاص بأرشفة نقاشات الميدان
    وهي كالتالي:
        ويكيبيديا:الميدان/سياسات
        ويكيبيديا:إخطار الإداريين/أخرى
        ويكيبيديا:الميدان/تقنية
        ويكيبيديا:الميدان/اقتراحات
        ويكيبيديا:الميدان/لغويات
        ويكيبيديا:الميدان/منوعات
        ويكيبيديا:الميدان/إدارة
        ويكيبيديا:الميدان/تقنية/أخبار
     */
    public ArchiveWp(Wiki wiki) throws IOException, LoginException, FailedLoginException, ParseException {

        data(wiki);
    }

    public void data(Wiki wiki) throws IOException, FailedLoginException, LoginException, ParseException {

        Map my = new HashMap();

        ArrayList withConclusion = new ArrayList();
        ArrayList news = new ArrayList();
        ArrayList withoutConclusion = new ArrayList();
        ArrayList seen = new ArrayList();

        /*
        قائمة الصفحات
        والصفحات المقابلة
         */
        //قائمة الصفحات التي يتم العمل عليها
        //النص الأول يمثل عنوان الصفحة المراد ارشفتها
        //والنص الثاني يمثل عنوان الصفحة الأرشيف التي سيقوم البوت بوضع النقاشات بداخلها
        //سيقوم البوت تلقائيا بتوليد صفحة أرشيف الشهر الحالي بناء على النص الثاني الخاص بكل صفحة
        my.put("ويكيبيديا:الميدان/سياسات", "ويكيبيديا:الميدان/سياسات/");
        my.put("ويكيبيديا:إخطار الإداريين/أخرى/الحالية", "ويكيبيديا:إخطار الإداريين/أخرى/");
        my.put("ويكيبيديا:الميدان/تقنية", "ويكيبيديا:الميدان/تقنية/");
        my.put("ويكيبيديا:الميدان/اقتراحات", "ويكيبيديا:الميدان/اقتراحات/");
        my.put("ويكيبيديا:الميدان/لغويات", "ويكيبيديا:الميدان/لغويات/");
        my.put("ويكيبيديا:الميدان/منوعات", "ويكيبيديا:الميدان/منوعات/");
        my.put("ويكيبيديا:الميدان/إدارة", "ويكيبيديا:الميدان/إدارة/");
        my.put("ويكيبيديا:الميدان/تقنية/أخبار", "ويكيبيديا:الميدان/تقنية/");

        Iterator it = my.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            System.out.println(pair.getKey() + " = " + pair.getValue());

            String title = pair.getKey().toString();
            String archiveTitle = pair.getValue().toString();

            String content = wiki.getPageText(title);

            LinkedHashMap<String, String> map = wiki.getSectionMap(title); //جلب النقاشات

            map = (LinkedHashMap<String, String>) Rashf.cleanSection(map);
            Iterator nest = map.entrySet().iterator();
            while (nest.hasNext()) {
                Map.Entry nestpair = (Map.Entry) nest.next();
                nest.remove();
                if (!nestpair.getValue().toString().endsWith(";;;;")) {
                    String contentOfSection = wiki.getSectionText(title, Integer.parseInt(nestpair.getKey().toString()));

                    if (isAllowed (contentOfSection)) {

                        //إذا كان القسم يحتوي قالب خلاصة 
                        if (Rashf.isWithConclusion(contentOfSection)) {
                            if (Dates.getDates(contentOfSection) != null) {
                                if (Dates.getAfterDate(Dates.getDates(contentOfSection), 14)) {
                                    System.out.println("أسبوعان مع خلاصة");
                                    withConclusion.add(contentOfSection + ",,,,,,,,,," + title + ",,,,,,,,,," + archiveTitle);
                                }
                            }
                        } //لايحتوي على خلاصة
                        else {
                            //archive قم بالأرشفة
                            if (Dates.getDates(contentOfSection) != null) {
                                if (Rashf.isWithoutConclusion(contentOfSection)
                                        && Dates.getAfterDate(Dates.getDates(contentOfSection), 23)) {
                                    System.out.println("3 أسابيع مع عدم وجود خلاصة");
                                    withoutConclusion.add(contentOfSection + ",,,,,,,,,," + title + ",,,,,,,,,," + archiveTitle);
                                    continue;
                                }
                            }
                        }

                        //نشرة الأخبار التقنية
                        if (contentOfSection.contains("يرجى إعلام المستخدمين الآخرين بهذه التغييرات. لن تؤثر كافة التغييرات عليك")) {
                            if (Dates.getDates(contentOfSection) != null) {
                                if (Dates.getAfterDate(Dates.getDates(contentOfSection), 7)) {
                                    System.out.println("نشرة الأخبار التقنية");
                                    news.add(contentOfSection + ",,,,,,,,,," + title + ",,,,,,,,,," + archiveTitle);
                                }
                            }
                        }

                        //الأقسام المنظورة
                        if (Rashf.isSeen(contentOfSection)) {
                            if (Dates.getDates(contentOfSection) != null) {
                                System.out.println("الأقسام المنظورة");
                                seen.add(contentOfSection + ",,,,,,,,,," + title + ",,,,,,,,,," + archiveTitle);

                            }
                        }
                    }
                }
            }

            map.clear();
            it.remove();
        }
        if (!withoutConclusion.isEmpty()) {
            edit(withoutConclusion, "روبوت:أرشفة قسم بعد مرور أكثر من ثلاثة أسابيع، مع عدم وجود خلاصة.", "روبوت:إضافة قسم مؤرشف، مع عدم وجود خلاصة", wiki);

        }

        if (!withConclusion.isEmpty()) {
            edit(withConclusion, "روبوت:أرشفة قسم بعد مرور أكثر من أسبوعين، مع وجود خلاصة.", "روبوت:إضافة قسم مؤرشف، مع وجود خلاصة.", wiki);

        }

        if (!news.isEmpty()) {
            edit(news, "روبوت:أرشفة نشرة الأخبار التقنية الأسبوعية", "روبوت:إضافة نشرة الأخبار التقنية الأسبوعية", wiki);

        }

        if (!seen.isEmpty()) {
            edit(seen, "روبوت:أرشفة أقسام منظورة", "روبوت:إضافة أقسام منظورة", wiki);

        }

    }

    public void edit(ArrayList archive, String summary1, String summary2, Wiki wiki) throws IOException, LoginException, ParseException {

        for (Object tmp : archive) {

            String[] param =  (tmp.toString()).split(",,,,,,,,,,");
            String content = param[0];
            String title = param[1];
            String archiveTitle = param[2];

            String con = wiki.getPageText(title);

            System.out.println("********************************");
            System.out.println(wiki.getCurrentUser());
            Login.login(wiki);
            wiki.edit(title, con.replace(content, "").replaceAll("(\r?\n){3,}", "\n\n"), summary1, true, true, -2, null);
            if (wiki.getPageInfo(archiveTitle + Dates.getDate(Dates.getDates(content))).get("exists").equals(true)) {
                String x = wiki.getPageText((archiveTitle + Dates.getDate(Dates.getDates(content))));
                wiki.edit(archiveTitle + Dates.getDate(Dates.getDates(content)), (x + "\n" + content).replaceAll("(\r?\n){3,}", "\n\n"), summary2, true, true, -2, null);
            } else {
                wiki.edit(archiveTitle + Dates.getDate(Dates.getDates(content)), ("\n" + content).replaceAll("(\r?\n){3,}", "\n\n"), summary2, true, true, -2, null);
            }
        }
    }
}
