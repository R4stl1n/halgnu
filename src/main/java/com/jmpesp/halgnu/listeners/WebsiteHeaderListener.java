package com.jmpesp.halgnu.listeners;

import com.jmpesp.halgnu.util.CommandHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebsiteHeaderListener extends ListenerAdapter {

    private String m_command = ".title";
    private String m_pastUrl = "";
    private String m_regex = "/((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?)/\n";

    public WebsiteHeaderListener() {
        try {
            enableSSLSocket();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {
        checkIfUrl(event.getMessage());
        if (event.getMessage().startsWith(m_command)) {
            if(CommandHelper.checkForAmountOfArgs(event.getMessage(), 0)) {
               
                event.respond(getTitle());
                
            } else {
                
                event.respond("Ex: "+m_command+"");
            }
        }
    }

    private void enableSSLSocket() throws KeyManagementException, NoSuchAlgorithmException {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new X509TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }

    private String getTitle() {
        Document doc = null;
        try {

            doc = Jsoup.connect(cleanUrl()).get();
            String title = doc.title();
            return title;
        } catch (IOException e) {
            e.printStackTrace();
            return "Could not connect to website";
        }
    }
    
    private String cleanUrl() {
        String newUrl = "";
                
        if (m_pastUrl.startsWith("http://"))
            newUrl = m_pastUrl;

        if (m_pastUrl.startsWith("www"))
            newUrl = "http://" + m_pastUrl;
        
        return newUrl;
    }
    
    private void checkIfUrl(String text) {

        List<String> links = new ArrayList();

        String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        while (m.find()) {
            String urlStr = m.group();
            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                urlStr = urlStr.substring(1, urlStr.length() - 1);
            }
            links.add(urlStr);
        }

        if (links.size() >= 1) {
            m_pastUrl = links.get(0);
        }
    }
}
