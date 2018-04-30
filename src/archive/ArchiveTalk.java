/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archive;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.security.auth.login.LoginException;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.wikipedia.Wiki;
import utility.Dates;
import utility.Rashf;
import static utility.Rashf.isAllowed;
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
public class ArchiveTalk {

    public static void data(Wiki wiki) throws IOException, ParseException, LoginException {

        String title = "";
        ArrayList archive = new ArrayList();

        /**
         * يتم جلب صفحات النقاش الطويلة باستخدام الرابط التالي وذلك للنطاقات
         * التالية نقاش المقالات نقاش التصنيف نقاش القالب نقاش الوحدة نقاش
         * ميدياويكي نقاش المساعدة نقاش البوابة
         */
        org.jsoup.nodes.Document document = Jsoup.connect("http://petscan.wmflabs.org/?language=ar&project=wikipedia&ns%5B1%5D=1&ns%5B5%5D=1&ns%5B9%5D=1&ns%5B11%5D=1&ns%5B13%5D=1&ns%5B15%5D=1&ns%5B101%5D=1&ns%5B829%5D=1&larger=1000&show_redirects=no&templates_yes=%D8%B1%D8%A3%D8%B3%20%D9%86%D9%82%D8%A7%D8%B4&sortby=size&sortorder=descending&interface_language=en&active_tab=tab_pageprops&doit=").get();
        Elements link = document.getElementsByClass("pagelink");
        Elements link1 = document.select("td").next().next();

        ArrayList a = new ArrayList();

        /**
         * جلب آخر أرشيف وصل إليه نقاش الصفحة
         */
        for (int i = 0; i < link1.size(); i++) {
            if (!link1.get(i).ownText().matches("[0-9]+")) {
                a.add(link1.get(i).ownText());
            }

        }

        for (int i = 0; i < link.size(); i++) {

            title = a.get(i) + ":" + link.get(i).ownText();
            System.out.println(title);
            if (!title.contains("مراجعة") & !title.contains("تصويت") && !title.contains("أرشيف")) {
                String[] p = wiki.listPages(title, null, Wiki.ALL_NAMESPACES);
                int max = 1;
                for (String t : p) {
                    /**
                     * يتأكد أنها ليست صفحة فرعية
                     */
                    if (t.contains("أرشيف") && t.contains("[0-9]{1,2}") == true) {
                        int x = Integer.parseInt(t.replaceFirst("^.*\\D", ""));
                        if (x >= max) {
                            max = x;
                        }
                    }

                }

                String content = wiki.getPageText(title);
                String finals = "";
                LinkedHashMap sections = Rashf.cleanSection(wiki.getSectionMap(title));

                for (Iterator it = sections.entrySet().iterator(); it.hasNext();) {

                    Map.Entry entry = (Map.Entry) it.next();
                    if (!entry.getValue().toString().endsWith(";;;;")) {

                        int sectionNum = (int) entry.getKey();
                        String contentSection = wiki.getSectionText(title, sectionNum);
                        if (new Dates().getDates(contentSection) != null && isAllowed(contentSection)) {
                            if ((new Dates().getAfterDate(new Dates().getDates(contentSection), 30))
                                    || isSeen(contentSection)) {
                                content = content.replace(contentSection, "");
                                finals = finals.replace(contentSection, "") + "\n\n" + contentSection;
                            }

                        } else {
                            if (isAllowed(contentSection)) {
                                content = content.replace(contentSection, "");
                                finals = finals.replace(contentSection, "") + "\n\n" + contentSection;
                            }
                        }
                    }

                }

                int actual_size = (int) wiki.getPageInfo(title + "/أرشيف " + max).get("size");
                if (actual_size > 50000) {
                    max = ++max;
                }

                System.out.println("********************************");

                if (finals != "") {
                    Login.login(wiki);
                    try {
                        if (wiki.getPageInfo(title + "/أرشيف " + max).get("exists").equals(true)) {
                            String x = wiki.getPageText(title + "/أرشيف " + max);
                            wiki.edit((title) + "/أرشيف " + max, (x + "\n" + finals).replaceAll("(\r?\n){3,}", "\n\n"), "روبوت:إضافة أقسام مؤرشفة", true, true, -2, null);
                        } else {
                            wiki.edit((title) + "/أرشيف " + max, ("{{أرشيف نقاش}}\n" + "{{تصفح أرشيف|" + max + "}}\n\n" + finals).replaceAll("(\r?\n){3,}", "\n\n"), "روبوت:إضافة أقسام مؤرشفة", true, true, -2, null);
                        }
                        wiki.edit(title, content.replaceAll("(\r?\n){3,}", "\n\n"), "روبوت:أرشفة نقاشات قديمة بعد مرور شهر كامل", true, true, -2, null);

                    } catch (Exception e) {

                    }
                }

                archive.clear();
                max = 1;
                finals = "";
                actual_size = 0;
                title = "";
            }
        }

    }

    public ArchiveTalk(Wiki wiki) throws IOException, ParseException, LoginException {
        data(wiki);
    }

}
