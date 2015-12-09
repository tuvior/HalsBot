package TwitchBot.title;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageTitle {

    private static final Pattern URL_PATTERN = Pattern.compile("(([a-zA-Z0-9]{1,6})://)?([_a-zA-Z\\d\\-]+(\\.[_a-zA-Z\\d\\-]+)+)(([_a-zA-Z\\d\\-\\\\\\./?=&#]+[_a-zA-Z\\d\\-\\\\/])+)*");
    private static final Pattern TITLE_TAG_PATTERN = Pattern.compile("<title>(.*)</title>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);


    public static boolean checkForUrl(String message) {
        return message.matches(".*(([a-zA-Z0-9]{1,6})://)?([_a-z\\d\\-]+(\\.[_a-z\\d\\-]+)+)(([_a-z\\d\\-\\\\\\./]+[_a-z\\d\\-\\\\/])+)*.*");
    }

    public static String getPageTitle(String url) {
        try {
            Matcher m = URL_PATTERN.matcher(url);
            m.find();
            String site = m.group();

            if (!site.contains("http://") && !site.contains("https://")) {
                if (site.contains("youtube")) {
                    site = "https://" + site;
                } else {
                    site = "http://" + site;
                }

            }

            URL u = new URL(site);
            URLConnection conn = u.openConnection();

            String title = "";

            ContentType contentType = getContentTypeHeader(conn);
            assert contentType != null;
            if (!contentType.contentType.equals("text/html"))
                return "";
            else {
                Charset charset = getCharset(contentType);
                if (charset == null)
                    charset = Charset.defaultCharset();

                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        in, charset));
                int n, totalRead = 0;
                char[] buf = new char[1024];
                StringBuilder content = new StringBuilder();

                while (totalRead < 8192
                        && (n = reader.read(buf, 0, buf.length)) != -1) {
                    content.append(buf, 0, n);
                    totalRead += n;
                }
                reader.close();

                String cont = content.toString();
                if (cont.contains("<title></title>")) {
                    cont = cont.substring(cont.indexOf("<title></title>") + 15);
                }

                Matcher matcher = TITLE_TAG_PATTERN.matcher(cont);
                if (matcher.find()) {
                    title = matcher.group(1).replaceAll("[\\s<>]+", " ")
                            .trim();
                    title = StringEscapeUtils.unescapeHtml4(title);

                }
            }
            return title;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private static ContentType getContentTypeHeader(URLConnection conn) {
        int i = 0;
        boolean moreHeaders;
        do {
            String headerName = conn.getHeaderFieldKey(i);
            String headerValue = conn.getHeaderField(i);
            if (headerName != null && headerName.equals("Content-Type"))
                return new ContentType(headerValue);

            i++;
            moreHeaders = headerName != null || headerValue != null;
        } while (moreHeaders);

        return null;
    }

    private static Charset getCharset(ContentType contentType) {
        if (contentType != null && contentType.charsetName != null
                && Charset.isSupported(contentType.charsetName))
            return Charset.forName(contentType.charsetName);
        else
            return null;
    }

    private static final class ContentType {
        private static final Pattern CHARSET_HEADER = Pattern.compile(
                "charset=([-_a-zA-Z0-9]+)", Pattern.CASE_INSENSITIVE
                        | Pattern.DOTALL);

        private String contentType;
        private String charsetName;

        private ContentType(String headerValue) {
            if (headerValue == null)
                throw new IllegalArgumentException(
                        "ContentType must be constructed with a not-null headerValue");
            int n = headerValue.indexOf(";");
            if (n != -1) {
                contentType = headerValue.substring(0, n);
                Matcher matcher = CHARSET_HEADER.matcher(headerValue);
                if (matcher.find())
                    charsetName = matcher.group(1);
            } else
                contentType = headerValue;
        }
    }

}
