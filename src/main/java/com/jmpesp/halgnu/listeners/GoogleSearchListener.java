package com.jmpesp.halgnu.listeners;

import com.jmpesp.halgnu.models.MemberModel;
import com.jmpesp.halgnu.util.CommandHelper;
import com.jmpesp.halgnu.util.PermissionHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoogleSearchListener extends ListenerAdapter {

    private String m_command = ".google";
    
    private List<MemberModel.MemberStatus> neededPermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.OG,
                    MemberModel.MemberStatus.ADMIN,
                    MemberModel.MemberStatus.MEMBER,
                    MemberModel.MemberStatus.PROSPECT
            ));

    public static void sendHelpMsg(GenericMessageEvent event) {
        event.getBot().sendIRC().message(event.getUser().getNick(), ".google <query> - Google query to execute");
    }
    
    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {

        if (event.getMessage().startsWith(m_command)) {
            if(PermissionHelper.HasPermissionFromList(neededPermissions, event.getUser().getNick())) {
                if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 1)) {
                    event.respond(getGoogleResult(CommandHelper.removeCommandFromString(event.getMessage())));
                } else {
                    event.respond("Ex: " + m_command + " <query>");
                }
            } else {
                event.respond("Permission denied");
            }
        }
    }
    
    private String getGoogleResult(String query) {
        String google = "http://www.google.com/search?q=";
        String search = query;
        String charset = "UTF-8";
        String userAgent = "HalGNU 1.0 (+https://github.com/R4stl1n/halgnu)";
        String result = "No results found";
        String title = "";
        String url = "";
        
        Elements links = null;
        
        try {
            links = Jsoup.connect(google + URLEncoder.encode(search, charset)).userAgent(userAgent).get().select("li.g>h3>a");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(links != null) {
            if(links.size() >= 1) {

                title = links.first().text();
                url = links.first().absUrl("href");
                try {
                    url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                System.out.println("Title: " + title);
                System.out.println("URL: " + url);
            }
        }
        
        result = title + ": " + url;
        
        return result;
    }
}
