/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import archive.Main;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.wikipedia.Wiki;

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
public class SpamList {

   /* public static boolean isSpam(String content) throws IOException {
        boolean is = false;
        String[] list = Main.spam.split("\n");
        for (String tmp : list) {

            tmp = tmp.replace("\\b", "");
            tmp = tmp.replace("\\d", "");
            tmp = tmp.replace("\\", "");

            for (String tmp1 : getUrls(content)) {

                if (tmp1.toLowerCase().contains(tmp.toLowerCase())
                        && !tmp.contains("wikimedia")
                        && !tmp.contains("wikidata")
                        && !tmp.contains("wikipedia")) {
                    is = true;
                }
            }
        }

        return is;
    }

    public static List<String> getUrls(String text) {
        List<String> urls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            urls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)).replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)", ""));
        }

        return urls;
    }
*/
}
