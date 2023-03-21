package com.hrhrng.drugforecast.Service;


import com.hrhrng.drugforecast.common.stub.EmailHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    EmailHelper emailHelper;
    Map<String, List<String>> notifyMap;

    EmailService() {
        notifyMap = new HashMap<>();
    }

    public void addNotify(String disease, String mail) {
        var list = notifyMap.getOrDefault(disease, new ArrayList<>());
        list.add(mail);
        notifyMap.put(disease, list);
    }


    public List<String> getNotify(String disease) {
        return notifyMap.getOrDefault(disease, new ArrayList<>());
    }

    public void notifyEmail(String disease) {
        getNotify(disease).forEach(i -> {
            try {
                emailHelper.sendEmail(i, "查询结果通知","你对" + disease + "的查询结果已经生成，请重新查询查看");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
