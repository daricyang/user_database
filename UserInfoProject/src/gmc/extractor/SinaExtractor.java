/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gmc.extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 新浪微博提取器
 *
 * @author Pok
 */
public class SinaExtractor {

    public static final int FOLLOW = 1;
    public static final int FANS = 2;

    /**
     * 提取新浪微博用户信息
     *
     * @param source 新浪微博用户信息页面源代码
     * @return 哈希表，key为信息名，value为信息内容
     */
    public HashMap<String, String> extractInfo(String source) {
        HashMap<String, String> info = new HashMap<String, String>();
        String regex = "label S_txt2.*?t<\\\\/div>";
        List<String> al = ReglarExpression.ReglarArray(regex, source);
        for (String s : al) {
            try {
                s = s.substring(15);
                s = s.replaceAll("\\\\t", "")
                        .replaceAll("\\\\n", "")
                        .replaceAll("\\\\r", "")
                        .replaceAll("<\\\\/div>", "")
                        .replaceAll("<div class=\\\\\"con\\\\\">", ",")
                        .replaceAll("<span class=\\\\\"S_func1\\\\\" node-type=\\\\\"tag\\\\\">", " ")
                        .replaceAll("<.*?>", "").replaceAll("\\\\", "");
                String[] temp = s.split(",");
                info.put(temp[0].trim(), temp[1].trim());
            } catch (Exception ex) {
            }
        }
        return info;
    }

    /**
     * 提取用户的粉丝或关注
     *
     * @param source 用户粉丝页或关注页
     * @return 粉丝或关注列表
     */
    public List<String> extractorFriendship(String source) {
        List<String> friendList = new ArrayList<String>();
        String regex = "<img usercard=\\\\\".*?\"";
        List<String> al = ReglarExpression.ReglarArray(regex, source);
        for (String s : al) {
            s = s.replaceAll("<img usercard=\\\\\"id=", "").replaceAll("\\\\\"", "");
            friendList.add(s);
        }
        return friendList;
    }

    public int extractorPage(String source, int type) {
        int page = -1;
        String res = "";
        String regex = "";
        if (type == SinaExtractor.FOLLOW) {
            regex = "<strong node-type=\\\\\"follow\\\\\">.*?<\\\\/strong>";
        } else {
            regex = "<strong node-type\\\\\"fans\\\\\">.*?<\\\\/strong>";
        }
        res = ReglarExpression.Reglar(regex, source);
        res = res.replaceAll("</*?>", "");
        try {
            page = Integer.parseInt(res);
            page = page / 20;
            double temp = page / 20.0;
            if (temp > page) {
                page++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return page;
    }
}
