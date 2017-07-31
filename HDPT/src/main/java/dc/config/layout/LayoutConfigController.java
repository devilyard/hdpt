/*
 * @(#)LayoutConfigController.java Created on Oct 23, 2012 5:45:46 PM
 *
 * ��Ȩ����Ȩ���� B-Soft ��������Ȩ����
 */
package dc.config.layout;

import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ctd.config.MultiConfigController;
import ctd.dictionary.Dictionaries;
import ctd.dictionary.Dictionary;
import ctd.dictionary.DictionaryItem;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public class LayoutConfigController extends MultiConfigController {

	private static final long serialVersionUID = 3437649247355350262L;

	private static final Logger logger = LoggerFactory
			.getLogger(LayoutConfigController.class);

	private static LayoutConfigController lcc = new LayoutConfigController();

	private LayoutConfigController() {
		this.calalog = "LayoutConfigController";
	}

	public static LayoutConfigController instance() {
		return lcc;
	}

	public Layout getLayout(String id) {
		Dictionary layoutDic = Dictionaries.instance().getDic("layout");
		DictionaryItem layoutItem=layoutDic.getItem(id);
		id=layoutItem.getProperty("layoutId");
		Layout layout = (Layout) this.cache.get(id);
		if (layout == null) {
			Document doc = getConfigDoc(id);
			if (doc != null) {
				layout = new Layout();
				layout.setDefineDoc(doc);
				this.cache.put(id, layout);
			} else {
				logger.error("Layout id[{}] not found.", id);
			}
		}
		return layout;
	}
	
	public Layout getTemplates(String id){
		Layout layout=new Layout();
		Document doc = getConfigDoc(id);
		 if (doc != null) {
				layout = new Layout();
				layout.setDefineDoc(doc);
			} else {
				logger.error("Layout id[{}] not found.", id);
			}
		 return layout;
	}
}
