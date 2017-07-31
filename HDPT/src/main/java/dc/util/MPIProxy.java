/*
 * @(#)MPIHelper.java Created on Oct 24, 2012 5:36:48 PM
 *
 * 版权：版权所有 B-Soft 保留所有权力。
 */
package dc.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bsoft.mpi.server.MPIServiceException;
import com.bsoft.mpi.server.rpc.IMPIProvider;

import ctd.dictionary.Dictionaries;

import dc.SysArgs;
import dc.ssdev.extend.dic.CXMLDictionary;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public class MPIProxy {

	private static EntityRWLock baseInfoLock = new EntityRWLock();
	private static EntityRWLock detailInfoLock = new EntityRWLock();

	/**
	 * @param request
	 * @return
	 * @throws MPIServiceException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getBaseMPI(HttpServletRequest request,
			String mpiId, IMPIProvider mpiService) throws MPIServiceException {
		HttpSession session = HttpHelper.getSession(request, null);
		baseInfoLock.readlock(mpiId);
		try {
			Map<String, Object> info = (Map<String, Object>) session
					.getAttribute(SysArgs.MPI_BASE_INFO);
			if (mpiId.equals(session.getAttribute("MPI_BASE_INFO_MPIID"))
					&& info != null) {
				return info;
			}
		} finally {
			baseInfoLock.readUnlock(mpiId);
		}
		try {
			baseInfoLock.writelock(mpiId);
			Map<String, Object> info = (Map<String, Object>) session
					.getAttribute(SysArgs.MPI_BASE_INFO);
			if (mpiId.equals(session.getAttribute("MPI_BASE_INFO_MPIID"))
					&& info != null) {
				return info;
			}
			Map<String, Object> mpi = mpiService.getMPI(mpiId);
			if (mpi != null && !mpi.isEmpty()) {
				session.setAttribute(SysArgs.MPI_BASE_INFO, mpi);
				session.setAttribute("PhotosUrl",
						mpiService.getPhotosUrl(mpiId));

			}
			session.setAttribute("MPI_BASE_INFO_MPIID", mpiId);
			return mpi;
		} finally {
			baseInfoLock.writeUnlock(mpiId);
		}
	}

	/**
	 * @param request
	 * @param mpiId
	 * @param mpiService
	 * @return
	 * @throws MPIServiceException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getDetailMPI(HttpServletRequest request,
			String mpiId, IMPIProvider mpiService) throws MPIServiceException {
		HttpSession session = HttpHelper.getSession(request, null);
		try {
			detailInfoLock.readlock(mpiId);
			Map<String, Object> info = (Map<String, Object>) session
					.getAttribute(SysArgs.MPI_DETAIL_INFO);
			if (mpiId.equals(session.getAttribute("MPI_DETAIL_INFO_MPIID"))
					&& info != null) {
				return info;
			}
		} finally {
			detailInfoLock.readUnlock(mpiId);
		}
		try {
			detailInfoLock.writelock(mpiId);
			Map<String, Object> info = (Map<String, Object>) session
					.getAttribute(SysArgs.MPI_DETAIL_INFO);
			if (mpiId.equals(session.getAttribute("MPI_DETAIL_INFO_MPIID"))
					&& info != null) {
				return info;
			}
			Map<String, Object> mpi = mpiService.getMPIDetail(mpiId);
			Map<String, Object> baseInfo = getBaseMPI(request, mpiId,
					mpiService);
			if (mpi != null && !mpi.isEmpty()) {
				mpi = sumMap(baseInfo, mpi);
				session.setAttribute(SysArgs.MPI_DETAIL_INFO, mpi);
				session.setAttribute("MPI_DETAIL_INFO_MPIID", mpiId);
			}
			return mpi;
		} finally {
			detailInfoLock.writeUnlock(mpiId);
		}
	}

	/**
	 * 合并信息
	 * 
	 * @param baseInfo
	 * @param detailInfo
	 * @param contacts
	 *            联系人
	 * @param cards
	 *            卡种类：健康卡、市民卡等
	 * @param contactWays
	 *            病人联系方式
	 * @param addresses
	 *            地址
	 * @param certificates
	 *            证件类型
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, Object> sumMap(Map<String, Object> baseInfo,
			Map<String, Object> detailInfo) {
		Map<String, Object> detailInfo2 = new HashMap<String, Object>(baseInfo);
		for (String key : detailInfo.keySet()) {
			Object obj = detailInfo.get(key);
			if (key.equals("contacts")) {
				List<Map<String, Object>> contacts = (List<Map<String, Object>>) obj;
				for (Map<String, Object> c : contacts) {
					detailInfo2.put("contacts1", c);
				}
			} else if (key.equals("contactWays")) {
				List<Map<String, Object>> contactWays = (List<Map<String, Object>>) obj;
				for (Map<String, Object> cw : contactWays) {
					detailInfo2.put(
							"contactTypeCode" + cw.get("contactTypeCode"), cw);
				}
			} else if (key.equals("certificates")) {
				List<Map<String, Object>> certificates = (List<Map<String, Object>>) obj;
				for (Map<String, Object> c : certificates) {
					detailInfo2.put("certificates1", c);
				}
			}
		}
		CXMLDictionary certificateTypeCode = (CXMLDictionary) Dictionaries
				.instance().getDic("certificateTypeCode");
		Map<String, Object> map = new HashMap<String, Object>();
		for (String key : certificateTypeCode.items().keySet()) {
			map.put(key,
					certificateTypeCode.items().get(key).toString().split("=")[1]);
		}
		Map<String, Object> certificateTypeValue = (Map<String, Object>) detailInfo2
				.get("certificates1");
		String ckey = (String) certificateTypeValue.get("certificateTypeCode");
		Object cvalue = map.get(ckey);
		detailInfo2.put("certificateTypeValue", cvalue);
		return detailInfo2;
	}
}
