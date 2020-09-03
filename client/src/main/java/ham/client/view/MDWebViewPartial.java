package ham.client.view;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.options.MutableDataSet;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import java.io.IOException;
import java.util.Arrays;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Enrico Gruner
 */
public class MDWebViewPartial {
    private String uri = null;
    private String content = null;
    private WebView wv;
    
    public MDWebViewPartial(String text, String type) {
        if(type.equals("uri"))
            this.uri = text;
        else
            this.content = text;
    }
    
    private void generateView() {
            MutableDataSet options = new MutableDataSet();
            options.setFrom(ParserEmulationProfile.MULTI_MARKDOWN);
            options.set(Parser.EXTENSIONS, Arrays.asList(AutolinkExtension.create()));

            Parser parser = Parser.builder(options).build();
            HtmlRenderer renderer = HtmlRenderer.builder(options).build();
            
            if(this.content == null) {
                try {
                    this.content = IOUtils.toString(getClass().getResourceAsStream(this.uri), "UTF-8");
                }
                catch(IOException e) {
                    // FIX ME                
                }
            }
            Node document = parser.parse(this.content);
            String html = renderer.render(document);

            this.wv = new WebView();
            WebEngine we = wv.getEngine();
            
            we.setUserStyleSheetLocation(getClass().getResource("/ham/client/view/mdWebView.css").toExternalForm());
            we.loadContent(html);
    }
    
    public WebView getWebView() {
        this.generateView();
        
        return this.wv;
    }
}
