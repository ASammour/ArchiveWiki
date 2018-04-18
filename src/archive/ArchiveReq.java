package archive;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import org.wikipedia.Wiki;
import utility.Dates;
import utility.Rashf;

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
public class ArchiveReq {

    /**
     * هذا هو الملف الخاص بأرشفة صفحة الطلبات في ويكيبيديا العربية وهي كالتالي:
     * وب:طلبات مراجعة التعديلات وب:إخطار_الإداريين/إخفاء وب:إخطار_الإداريين/منع
     * وب:إخطار_الإداريين/إزالة منع وب:إخطار_الإداريين/حماية
     * وب:إخطار_الإداريين/إزالة حماية وب:إخطار_الإداريين/إخفاء وب:صور/طلبات
     * وب:طلبات صلاحيات/إعطاء وب:طلبات صلاحيات/إزالة وب:طلبات النقل وب:هل تعلم
     * ويكيبيديا:إخطار الإداريين/استرجاع وب:طلبات مراجعة المقالات
     * ويكيبيديا:تغيير اسم المستخدم ويكيبيديا:طلب تدقيق مستخدم
     */
    public static void data(Wiki wiki) throws IOException, FailedLoginException, LoginException, ParseException, InterruptedException {

        Map my = new HashMap();

        //قائمة الصفحات التي يتم العمل عليها
        //النص الأول يمثل عنوان الصفحة المراد ارشفتها
        //والنص الثاني يمثل عنوان الصفحة الأرشيف التي سيقوم البوت بوضع النقاشات بداخلها
        //سيقوم البوت تلقائيا بتوليد صفحة أرشيف الشهر الحالي بناء على النص الثاني الخاص بكل صفحة
        /**
         * في حال كان الطلب مقبولا أو مرفوضا فإن البوت سيقوم بأرشفته بعد مرور
         * يومين على قبول أو رفض الطلب
         */
        my.put("ويكيبيديا:هل تعلم/معلومات مقترحة", "ويكيبيديا:هل تعلم/معلومات مقترحة/أرشيف/");
        my.put("ويكيبيديا:صور/طلبات", "ويكيبيديا:صور/طلبات/أرشيف/");
        my.put("ويكيبيديا:طلبات صلاحيات/إعطاء", "ويكيبيديا:طلبات صلاحيات/إعطاء/أرشيف/");
        my.put("ويكيبيديا:إخطار الإداريين/حماية/الحالية", "ويكيبيديا:إخطار الإداريين/حماية/أرشيف/");
        my.put("ويكيبيديا:إخطار الإداريين/منع/الحالية", "ويكيبيديا:إخطار الإداريين/منع/أرشيف/");
        my.put("ويكيبيديا:إخطار الإداريين/استرجاع/الحالية", "ويكيبيديا:إخطار الإداريين/استرجاع/أرشيف/");
        my.put("ويكيبيديا:إخطار الإداريين/إخفاء/الحالية", "ويكيبيديا:إخطار الإداريين/إخفاء/أرشيف/");
        my.put("ويكيبيديا:طلبات مراجعة التعديلات", "ويكيبيديا:طلبات مراجعة التعديلات/أرشيف/");
        my.put("ويكيبيديا:إخطار الإداريين/إزالة منع/الحالية", "ويكيبيديا:إخطار الإداريين/إزالة منع/أرشيف/");
        my.put("ويكيبيديا:إخطار الإداريين/إزالة حماية/الحالية", "ويكيبيديا:إخطار الإداريين/إزالة حماية/أرشيف/");
        my.put("ويكيبيديا:طلبات النقل/الحالية", "ويكيبيديا:طلبات النقل/أرشيف/");
        my.put("ويكيبيديا:طلبات صلاحيات/إزالة", "ويكيبيديا:طلبات صلاحيات/إزالة/أرشيف/");
        my.put("ويكيبيديا:تغيير اسم المستخدم/طلبات تغيير", "ويكيبيديا:تغيير اسم المستخدم/أرشيف/");
        my.put("ويكيبيديا:تغيير اسم المستخدم/طلبات انتزاع", "ويكيبيديا:تغيير اسم المستخدم/أرشيف/");
        my.put("ويكيبيديا:طلبات مراجعة المقالات", "ويكيبيديا:طلبات مراجعة المقالات/أرشيف/");
        my.put("ويكيبيديا:طلب تدقيق مستخدم", "ويكيبيديا:طلب تدقيق مستخدم/أرشيف/");

        //فحص الصفحات السابقة
        //والمرور عليها واحدة واحدة
        Iterator it = my.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            it.remove();
            edit(pair.getKey().toString(), pair.getValue().toString(), wiki);
        }

    }

    //هنا يتم فحص حالة الطلبات
    //والقيام بالأرشفة عند تحقق الشروط
    public static void edit(String title, String archiveTitle, Wiki wiki) throws IOException, FailedLoginException, ParseException, LoginException {
        ArrayList archive = new ArrayList();

        String content = wiki.getPageText(title);

        //جلب الطلبات الخاصة داخل كل صفحة
        LinkedHashMap<String, String> map = Rashf.cleanSection(wiki.getSectionMap(title));
        Iterator nest = map.entrySet().iterator();
        while (nest.hasNext()) {
            Map.Entry nestpair = (Map.Entry) nest.next();
            nest.remove();

            if (!nestpair.getValue().toString().endsWith(";;;;")) {
                String contentOfSection = wiki.getSectionText(title, Integer.parseInt(nestpair.getKey().toString()));

                //إذا كان القسم يحتوي قالب وضع طلب مكتمل
                //إذا كان القسم تم الرد عليه سواء بالقبول أو الرفض
                if (Rashf.isResolved(contentOfSection)) {
                    // قم بالأرشفة
                    if (Dates.getDates(contentOfSection) != null) {
                        if (Dates.getAfterDate(Dates.getDates(contentOfSection), 2)) {
                            archive.add(contentOfSection);
                        }
                    }
                }

            }
        }
            String afterArchive = "";
            int x = 0;
            for (Object tmp : archive) {
                if (tmp.toString().contains("{{وضع طلب|4")) {
                    String movedTitle = Rashf.getMovedTitle(tmp.toString());
                    Login.login(wiki);
                    String old_content = "";
                    if ((wiki.getPageInfo("نقاش:" + movedTitle).get("exists")).equals(true)) {
                        old_content = wiki.getPageText("نقاش:" + movedTitle);
                        wiki.edit("نقاش:" + movedTitle, old_content + "\n" + tmp.toString(), "روبوت:إضافة طلبات مؤرشفة من [[" + title + "]]", true, true, -2, null);
                    } else {
                        wiki.edit("نقاش:" + movedTitle, tmp.toString(), "روبوت:إضافة طلبات مؤرشفة من [[" + title + "]]", true, true, -2, null);
                    }
                }
                content = content.replace(tmp.toString(), "");
                afterArchive = afterArchive + tmp.toString() + "\n";
                x++;
            }

            if (x != 0) {
                Login.login(wiki);

                //هنا يتم التعديل بالأرشفة
                wiki.edit(title, content.replaceAll("(\r?\n){3,}", "\n\n"), "روبوت:أرشفة طلبات مكتملة", true, true, -2, null);

                //إضافة النقاش المؤرشف إلى الأرشيف
                String old_content = "";
                if ((wiki.getPageInfo((archiveTitle + Dates.getDateAr())).get("exists")).equals(true)) {
                    old_content = wiki.getPageText(archiveTitle + Dates.getDateAr());
                    wiki.edit(archiveTitle + new Dates().getDateAr(), old_content + "\n" + afterArchive.replaceAll("(\r?\n){3,}", "\n\n"), "روبوت:إضافة طلبات مؤرشفة من [[" + title + "]]", true, true, -2, null);
                } else {
                    wiki.edit(archiveTitle + Dates.getDateAr(), afterArchive.replaceAll("(\r?\n){3,}", "\n\n"), "روبوت:إضافة طلبات مؤرشفة من [[" + title + "]]", true, true, -2, null);
                }

                archive.clear();
            }
        }
    
    public ArchiveReq(Wiki wiki) throws IOException, LoginException, FailedLoginException, ParseException, InterruptedException {

        data(wiki);

    }

}
