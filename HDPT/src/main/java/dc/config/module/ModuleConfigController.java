/*
 * @(#)ModuleConfigController.java Created on Oct 22, 2012 5:27:10 PM
 *
 * 版权：版权所有 B-Soft 保留所有权力。
 */
package dc.config.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ctd.config.Config;
import ctd.config.SingleConfigController;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public class ModuleConfigController extends SingleConfigController {

	private static final long serialVersionUID = 6882469337108287594L;

	private static final Logger logger = LoggerFactory
			.getLogger(ModuleConfigController.class);

	private static ModuleConfigController mcc = new ModuleConfigController();

	private ModuleConfigController(){
		calalog = "ModuleConfigController";
	}
	
	public static ModuleConfigController instance() {
		return mcc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ctd.config.ConfigController#setDefineDoc(org.dom4j.Document)
	 */
	@Override
	public void setDefineDoc(Document doc) {
		this.defineDoc = doc;
		if (this.defineDoc == null) {
			logger.error("can't init module-config from file: {}"
					+ this.ctrlFilename);
			return;
		}
		Element root = this.defineDoc.getRootElement();
		if (root != null) {
			@SuppressWarnings("unchecked")
			List<Element> appEles = root.elements("app");
			for (Element appEle : appEles) {
				App app = new App(appEle);
				this.cache.put(app.getId(), app);
			}
		}
	}

	public App getApplication(String id) {
		return (App) this.cache.get(id);
	}

	public List<App> getAllApplications() {
		List<App> ls = new ArrayList<App>();
		Collection<Config> c = this.cache.values();
		for (Config ec : c) {
			ls.add((App) ec);
		}
		return ls;
	}

	/**
	 * @param id
	 * @return
	 */
	public Module getModule(String id) {
		System.out.println(id);
		if (this.defineDoc == null) {
			return null;
		}
		Element root = this.defineDoc.getRootElement();
		Element el = (Element) root.selectSingleNode("//module[@id='" + id
				+ "']");
		if (el == null) {
			return null;
		}
		String pid = el.getParent().attributeValue("id");
		App app = (App) cache.get(pid);
		return app == null ? null : app.getModule(id); 
	}

}
