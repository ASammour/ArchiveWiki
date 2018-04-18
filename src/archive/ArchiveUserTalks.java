/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archive;

/**
 *
 * @author ahmed
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.security.auth.login.LoginException;
import org.wikipedia.Wiki;
import utility.Rashf;
import static utility.Rashf.isAllowed;

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
public class ArchiveUserTalks {

    /**
     * @param args the command line arguments
     */
    public void data(Wiki wiki) throws IOException, ParseException, LoginException {
        String title = "";
        String x = "";

        /**
         * جلب النقاشات الطويلة
         */
        String[] pages = wiki.getLinksOnPage("ويكيبيديا:إحصاءات/نقاشات طويلة");
        ArrayList a = new ArrayList();
        for (String page : pages) {

            title = page;

            /**
             * التأكد من أن صفحة النقاش لم تؤرشف من قبل
             */
            String[] p = wiki.listPages(title + "/", null, 3);

            if (p.length == 0 && title.startsWith("نقاش المست")) {
                a.add(title);

            }

        }

        for (Object tmp : a) {

            Date today = new Date();
            Date currentDate = new Date(today.getYear(), today.getMonth(), today.getDate());

            Date lastRevision = (wiki.getTopRevision(tmp.toString()).getTimestamp().getTime());
            lastRevision = new Date(lastRevision.getYear(), lastRevision.getMonth(), lastRevision.getDate());
            int days = (int) ((currentDate.getTime() - lastRevision.getTime()) / 86400000);
            if (days >= 5) {
                System.out.println(tmp.toString());
                String content = wiki.getPageText(tmp.toString());
                String finals = "";
                LinkedHashMap sections = Rashf.cleanSection(wiki.getSectionMap(tmp.toString()));

                /**
                 * أرشفة الأقسام
                 */
                try {

                    for (Iterator it = sections.entrySet().iterator(); it.hasNext();) {
                        Map.Entry entry = (Map.Entry) it.next();
                        if (!entry.getValue().toString().endsWith(";;;;")) {
                            int sectionNum = (int) entry.getKey();
                            String contentSection = wiki.getSectionText(tmp.toString(), sectionNum);
                            content = content.replace(contentSection, "");
                            finals = finals.replace(contentSection, "") + "\n\n" + contentSection;
                        }
                    }
                } catch (Throwable t) {
                    finals = wiki.getPageText(tmp.toString());
                    content = "";
                }

                System.out.println("********************************");

                if (!"".equals(finals) && isAllowed(finals)) {
                    Login.login(wiki);
                    try {
                        wiki.edit((tmp.toString()) + "/أرشيف " + 1, ("{{أرشيف نقاش}}\n" + "{{تصفح أرشيف|" + 1 + "}}\n\n" + finals).replaceAll("(\r?\n){3,}", "\n\n"), "روبوت:إضافة أقسام مؤرشفة", true, true, -2, null);
                        wiki.edit(tmp.toString(), content.replaceAll("(\r?\n){3,}", "\n\n") + "\n{{أرشيف آلي|" + tmp + "}}\n== أرشفة نقاشك ==\nمرحيا. تم أرشفة صفحة نقاشك لأن حجمها أصبح كبيرا جدا، وهذا يصعب من عملية تواصل المستخدمين معك. يمكنك مشاهدة النقاشات السابقة في يسار الصفحة. --~~~~", "روبوت:أرشفة صفحة نقاش طويلة", true, true, -2, null);

                    } catch (Exception e) {

                    }

                }

                finals = "";
            }
        }
    }

    public ArchiveUserTalks(Wiki wiki) throws IOException, ParseException, LoginException {
        data(wiki);
    }

}
