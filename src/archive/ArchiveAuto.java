package archive;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import org.wikipedia.Wiki;
import utility.Dates;
import utility.Rashf;
import static utility.Rashf.isNoArchive;
import static utility.Rashf.isSeen;

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
public class ArchiveAuto {

    public void data(Wiki wiki) throws IOException, ParseException, LoginException {

        //جلب الصفحات التي تحتوي على قالب الأرشيف الآلي
        String[] pages = wiki.whatTranscludesHere("قالب:أرشيف آلي", 4);

        for (String tmp : pages) {
            int max = 1;
            String c = wiki.getPageText(tmp);
            if (c.contains("{{أرشيف آلي")) {
                String[] p = wiki.listPages(getArchiveAuto(c), null, 4);
                for (String t : p) {

                    //جلب آخر صفحة أرشيف للصفحة
                    if (t.contains("أرشيف") && Character.isDigit(t.charAt(t.length() - 1))) {
                        int x = Integer.parseInt(t.replaceFirst("^.*\\D", ""));
                        if (x >= max) {
                            max = x;
                        }
                    }

                }
                String archive = getArchiveAuto(c) + "/أرشيف " + max;
                edit(wiki, tmp, archive);

            }
            max = 1;
        }
    }

    //جلب اسم الصفحة التي يتم وضع الأرشيف فيها
    public static String getArchiveAuto(String content) {
        String template = "";
        Pattern pattern = Pattern.compile("\\{\\{أرشيف آلي.{1,}\\}\\}");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            template = matcher.group(0).split("\\|")[1].replace("}}", "");
        }
        return template;
    }

    //تعديل الصفحة
    public static void edit(Wiki wiki, String title, String archive) throws IOException, ParseException, LoginException {
        String content = wiki.getPageText(title);

        String finals = "";
        LinkedHashMap sections = Rashf.cleanSection(wiki.getSectionMap(title));

        for (Iterator it = sections.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            if (!entry.getValue().toString().endsWith(";;;;")) {
                int sectionNum = (int) entry.getKey();
                String contentSection = wiki.getSectionText(title, sectionNum);
                if (Dates.getDates(contentSection) != null) {
                    if ((Dates.getAfterDate(Dates.getDates(contentSection), 10) || isSeen(contentSection))
                            && Rashf.isAllowed(contentSection)) {
                        content = content.replace(contentSection, "");
                        finals = finals.replace(contentSection, "") + "\n\n" + contentSection;
                    }
                }
            }
        }

        int actual_size = (int) wiki.getPageInfo(archive).get("size");
        if (actual_size > 50000) {
            archive = archive.replaceAll("\\d{1,}$", "") + (Integer.parseInt(archive.replaceFirst("^.*\\D", "")) + 1);
        }

        System.out.println("********************************");

        if (!"".equals(finals) && !"".equals(getArchiveAuto(content))) {
            Login.login(wiki);
            try {
                if (wiki.getPageInfo(archive).get("exists").equals(true)) {
                    String x = wiki.getPageText(archive);
                    wiki.edit(archive, (x + "\n" + finals).replaceAll("(\r?\n){3,}", "\n\n"), "روبوت:إضافة أقسام مؤرشفة", true, true, -2, null);
                } else {
                    wiki.edit(archive, (finals).replaceAll("(\r?\n){3,}", "\n\n"), "روبوت:إضافة أقسام مؤرشفة", true, true, -2, null);
                }
                wiki.edit(title, content.replaceAll("(\r?\n){3,}", "\n\n"), "روبوت:أرشفة نقاشات قديمة", true, true, -2, null);

            } catch (Exception e) {

            }
        }
    }

    public ArchiveAuto(Wiki wiki) throws IOException, LoginException, FailedLoginException, ParseException, InterruptedException {

        data(wiki);

    }
}
