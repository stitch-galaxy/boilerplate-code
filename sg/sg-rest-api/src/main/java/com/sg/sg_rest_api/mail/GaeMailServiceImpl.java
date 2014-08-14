/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.sg_rest_api.mail;

import com.sg.domain.service.SgServiceLayerException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

/**
 *
 * @author tarasev
 */
@Service
public class GaeMailServiceImpl implements MailService {

    public void sendEmailVerificationEmail(String token, String email) {
        try
        {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("admin@stitchgalaxy.com", "stitchgalaxy.com admin"));
        msg.addRecipient(Message.RecipientType.TO,
                new InternetAddress(email, "Dear user"));
        msg.setSubject(email + ", finish signing up for StitchGalaxy");
        msg.setText(token);
        Transport.send(msg);
        }
        catch(Exception e)
        {
            throw new SgServiceLayerException(e);
        }
    }
    
}
