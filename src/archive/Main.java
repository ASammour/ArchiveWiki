/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archive;

import java.io.IOException;
import java.text.ParseException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
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
public class Main {

    public static void main(String[] args) throws IOException, FailedLoginException, LoginException, ParseException, InterruptedException {
        Wiki wiki = new Wiki("ar.wikipedia.org");
        new ArchiveReq(wiki);
        new ArchiveWp(wiki);
        new ArchiveAuto(wiki);
        new ArchiveTalk(wiki);
        new ArchiveUsers(wiki);
        new ArchiveUserTalks(wiki);

    }
}
