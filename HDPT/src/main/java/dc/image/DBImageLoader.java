package dc.image;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import dc.dao.IDao;
import dc.util.DBUtil;

public class DBImageLoader implements IImageLoader {

	private static String entryName = "EHR_PersonalPhoto";
	private static String fieldName = "DocContent";
	@Autowired
	private IDao baseDao;

	@Override
	public boolean load(String mpiId, OutputStream outputStream)
			throws IOException {
		String where = DBUtil.createStringQuery("MPIID", mpiId);
		List<Object> list = baseDao.findField(entryName, fieldName, where);
		if (list == null || list.size() == 0) {
			return false;
		}
		byte[] image = (byte[]) list.get(0);
		outputStream.write(image);
		return true;
	}

}
