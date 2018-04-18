/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archive;

import java.io.IOException;
import javax.security.auth.login.FailedLoginException;
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

public class Login {
    
    /***
     * هذا الملف ضروري جدا لعمل البوت
     * وفي هذا الملف يتم كتابة اسم المستخدم وكلمة المرور الخاصة بحساب مشغل البوت
     * في حال كانت هذه القيم غير معينة أو أنها غير صحيحة
     * فإن البوت لن يعمل
     * @throws IOException
     * @throws FailedLoginException 
     */
    
    public static void login (Wiki wiki) throws IOException, FailedLoginException{
        
        /*
        اكتب اسم حسابك بين علامتي التنصيص أدناه
        */
        String userName = "";
        
        /*
        اكتب كلمة المرور الخاصة بحسابك بين علامتي التنصيص أدناه
        */        
        String password = "";
        
        if (userName.equals("") || password.equals("")){
            throw new java.lang.Error("لا يمكن تشغيل البوت بسبب عدم تسجيل الدخول!");
        }
        
        wiki.login(userName, password);
    }
}
