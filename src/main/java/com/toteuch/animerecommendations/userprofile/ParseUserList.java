package com.toteuch.animerecommendations.userprofile;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ParseUserList {

    private static final Logger log = LoggerFactory.getLogger(ParseUserList.class);
    private static final String USER_LIST_URL = "https://myanimelist.net/users.php";
    private static final String DIV_ONLINE_USER_CLASS = "normal_header";
    private static final String DIV_ONLINE_USER_TEXT = "Recently Online Users";

    public List<String> getUsernamesFromMAL() {
        List<String> parsedUsernames = new ArrayList<>();
        try {
            Connection.Response res = Jsoup.connect(USER_LIST_URL)
                    .method(Connection.Method.GET)
                    .execute();
            if (res.statusCode() == 200) {
                Document doc = res.parse();
                Element divOnlineUser = null;
                for (Element element : doc.body().getElementsByClass(DIV_ONLINE_USER_CLASS)) {
                    if (DIV_ONLINE_USER_TEXT.equals(element.text())) {
                        divOnlineUser = element;
                        break;
                    }
                }

                if (divOnlineUser != null && divOnlineUser.parent() != null) {
                    Element divOnlineUserParent = divOnlineUser.parent();
                    Elements profileElements = divOnlineUserParent.getElementsByAttributeValueContaining("href", "/profile/");
                    for (Element profileElement : profileElements) {
                        if (profileElement.children().isEmpty()) {
                            parsedUsernames.add(profileElement.text());
                        }
                    }
                }
            } else {
                log.error("GET {} : {}-{}", USER_LIST_URL, res.statusCode(), res.statusMessage());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return parsedUsernames;
    }
}
