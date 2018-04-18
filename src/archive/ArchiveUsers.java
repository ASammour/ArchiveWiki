/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archive;

import utility.Dates;
import static utility.Rashf.clean;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.security.auth.login.LoginException;
import org.wikipedia.Wiki;
import utility.Rashf;
import static utility.Rashf.cleanRashf;
import static utility.Rashf.getRashf;
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
public class ArchiveUsers {

    /**
     * @param s
     * @return
     */
    public void data(Wiki wiki) throws IOException, LoginException, ParseException {
        String[] user = wiki.whatTranscludesHere("قالب:أرشفة آلية", 3);
        for (String tmp : user) {
            if (!tmp.contains("/") && !wiki.getPageInfo(tmp).get("size").equals(1)) {
                String content = wiki.getPageText(tmp);
                Pattern pattern = Pattern.compile("\\{\\{أرشفة آلية(.*?)\\}\\}");
                Matcher matcher = pattern.matcher(content);
                while (matcher.find()) {
                    String localUrl = matcher.group(0);
                    String[] params = localUrl.split("\\|");
                    params = clean(params);

                    if (params.length == 4) {
                        //params[0] = أرشفة آلية
                        //params[1] = حجم/قسم
                        //params[2] = قيمة الحجم أو قيمة الأيام
                        //params[3] = مثال
                        switch (params[1]) {
                            case "حجم":
                                int input_size = Integer.parseInt(params[2]);
                                System.out.println("الحجم المدخل = " + input_size);
                                int actual_size = (int) wiki.getPageInfo(tmp).get("size");
                                System.out.println(actual_size);
                                actual_size = actual_size / 1024;
                                if (actual_size >= input_size) {
                                    System.out.println("حجم الصفحة أكبر من المحدد");
                                    String rashf = getRashf(content);
                                    editSize(wiki, params[3], content, rashf, tmp, localUrl);
                                }
                                break;
                            case "قسم":
                                String finals = "";
                                LinkedHashMap<String, String> sections = Rashf.cleanSection(wiki.getSectionMap(tmp));

                                Iterator<Entry<String, String>> itr = sections.entrySet().iterator();
                                while (itr.hasNext()) {
                                    Map.Entry nestpair = (Map.Entry) itr.next();
                                    itr.remove();
                                    if (!nestpair.getValue().toString().endsWith(";;;;")) {
                                        String list = nestpair.getKey().toString();
                                        try {
                                            list = wiki.getSectionText(tmp, Integer.parseInt(list));
                                        } catch (IOException | NumberFormatException | UnknownError t) {
                                            list = "";
                                        }

                                        Date date = Dates.getDates(list);
                                        if (date != null) {
                                            if (Dates.getAfterDate(date, Integer.parseInt(params[2]))) {
                                                content = content.replace(list, "");
                                                finals = finals + list + "\n";
                                            }
                                        } else {
                                            content = content.replace(list, "");
                                            finals = finals + list + "\n";
                                        }
                                    }
                                }
                                editSection(wiki, params[3], content, finals, tmp, localUrl);

                                break;
                        }
                    }
                }
            }
        }
    }

    public static void editSize(Wiki wiki, String example, String content, String rashf, String title, String rtemplate) throws IOException, LoginException {

        /*استبدال القالب القديم بالجديد*/
        System.out.println("عددي");
        String newversion = example.replaceAll("\\d{1,}$", "") + (Integer.parseInt(example.replaceFirst("^.*\\D", "")) + 1);
        String newrtemplate = rtemplate.replace(example, newversion);
        rashf = rashf.replace(rtemplate, newrtemplate);

        if (!cleanRashf(content).equals("") && isAllowed(cleanRashf(content))) {
            try {
                Login.login(wiki);
                if (wiki.getPageInfo(example).get("exists").equals(true)) {
                    String old = wiki.getPageText(example);
                    old = addTemp(old, example);
                    wiki.edit(example, old + "\n" + cleanRashf(content).replaceAll("(\r?\n){3,}", "\n\n"), "روبوت:إضافة نقاش مؤرشف", true, true, -2, null);
                } else {
                    wiki.edit(example, addTemp(cleanRashf(content), example).replaceAll("(\r?\n){3,}", "\n\n"), "روبوت:إضافة نقاش مؤرشف", true, true, -2, null);
                }
                wiki.edit(title, rashf.replaceAll("(\r?\n){3,}", "\n\n"), "روبوت:أرشفة", true, true, -2, null);
            } catch (Exception e) {

            }

        }

    }

    public static void editSection(Wiki wiki, String example, String content, String rashf, String title, String rtemplate) throws IOException, LoginException {

        if (!rashf.equals("") && isAllowed(cleanRashf(content))) {
            Login.login(wiki);
            if ((int) wiki.getPageInfo(example).get("size") <= 60000) {
                try {
                    if (wiki.getPageInfo(example).get("exists").equals(true)) {
                        String old = wiki.getPageText(example);
                        old = addTemp(old, example);
                        wiki.edit(example, old + "\n" + cleanRashf(rashf).replaceAll("(\r?\n){3,}", "\n\n"), "روبوت:إضافة نقاش مؤرشف", true, true, -2, null);
                    } else {
                        wiki.edit(example, addTemp(cleanRashf(rashf), example).replaceAll("(\r?\n){3,}", "\n\n"), "روبوت:إضافة نقاش مؤرشف", true, true, -2, null);
                    }
                    wiki.edit(title, content.replaceAll("(\r?\n){3,}", "\n\n"), "روبوت:أرشفة", true, true, -2, null);

                } catch (IOException | LoginException e) {

                }
            } else {
                String newversion = example.replaceAll("\\d{1,}$", "") + (Integer.parseInt(example.replaceFirst("^.*\\D", "")) + 1);
                String newrtemplate = rtemplate.replace(example, newversion);
                content = content.replace(rtemplate, newrtemplate);
                try {
                    if (wiki.getPageInfo(newversion).get("exists").equals(true)) {
                        String old = wiki.getPageText(newversion);
                        old = addTemp(old, newversion);
                        wiki.edit(newversion, old + "\n" + cleanRashf(rashf).replaceAll("(\r?\n){3,}", "\n\n"), "روبوت:إضافة نقاش مؤرشف", true, true, -2, null);
                    } else {
                        wiki.edit(newversion, addTemp(cleanRashf(rashf), newversion).replaceAll("(\r?\n){3,}", "\n\n"), "روبوت:إضافة نقاش مؤرشف", true, true, -2, null);
                    }

                    wiki.edit(title, content.replaceAll("(\r?\n){3,}", "\n\n"), "روبوت:أرشفة", true, true, -2, null);
                } catch (IOException | LoginException e) {

                }
            }
        }
    }

    public static String addTemp(String content, String example) {
        if (!content.contains("{{أرشيف صفحة رئيسية}}")) {
            content = "{{أرشيف صفحة رئيسية}}\n" + content;
        }

        if (!content.contains("{{تمت الأرشفة}}")) {
            content = "{{تمت الأرشفة}}\n" + content;
        }
        if (!content.contains("{{تصفح أرشيف")) {
            content = "{{تصفح أرشيف|" + Integer.parseInt(example.replaceFirst("^.*\\D", "")) + "}}\n" + content;
        }
        return content;
    }

    public ArchiveUsers(Wiki wiki) throws IOException, LoginException, ParseException {
        data(wiki);
    }

}
