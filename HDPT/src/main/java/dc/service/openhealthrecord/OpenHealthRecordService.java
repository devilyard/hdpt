package dc.service.openhealthrecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.bsoft.mpi.Constants;
import com.bsoft.mpi.server.MPIServiceException;
import com.bsoft.mpi.server.rpc.IMPIProvider;

import ctd.annotation.RpcService;
import ctd.util.MD5StringUtil;

import dc.domain.main.YyUserAccount;
import dc.service.IRegisterService;


public class OpenHealthRecordService implements IOpenHealthRecord {

	
	@Autowired
	private IMPIProvider mpiService;
	
	@Autowired
	private IRegisterService  service;
	
	@RpcService
	@Override
	public String openHealthRecord(String cardType, String cardId, String phonenum) {
		
		try {
			List<Map<String, Object>> list = null;
			Map<String, Object> mpiArgs = new HashMap<String, Object>();
			//身份证
			if("01".equals(cardType)){
				mpiArgs.put(Constants.F_IDCARD, cardId);
				list = mpiService.getMPIDetail(mpiArgs); 
			}else{
				Map<String, Object> certificatesmap = new HashMap<String, Object>();
				certificatesmap.put("cardNo", cardId);
				certificatesmap.put("cardTypeCode", cardType);
				List<Map<String, Object>> certificateslist = new ArrayList<Map<String, Object>>();
				certificateslist.add(certificatesmap);
				mpiArgs.put("cards", certificateslist);
				list = mpiService.getMPIDetail(mpiArgs); 
			}
			
			if(list!= null && list.size()>0){
				Map<String,Object> m = list.get(0);
				
				Map<String, Object> o = service.queryForMap("JMHD_MEMBER", "MPIID= ?", new Object[]{m.get("mpiId")});
				if(o != null ){
					return "该证件号码已开通个人档案信息！";
				}
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("MPIID", m.get("mpiId"));
				map.put("USERNAME", m.get("personName"));
				map.put("NAME", m.get("personName"));
				map.put("STATUS", "0");
				map.put("HOMEADDRESS", m.get("address"));
				map.put("CONTACT", m.get("contactName"));
				map.put("CONTACTPHONE", m.get("contactPhone"));
				map.put("REGDATE", new Date());
				map.put("CERTIFICATETYPECODE", cardType);
				map.put("CERTIFICATENO", cardId);
				map.put("PHONENUMBER", phonenum);
				YyUserAccount account = service.queryT("YyUserAccount", "phoneno = ?", new Object[]{phonenum});
				if(account != null){
					service.saveUpdate(account, map);
				}else{
					account = new YyUserAccount();
					account.setUsername(String.valueOf(m.get("personName")));
					account.setPassword(MD5StringUtil.MD5Encode("123456"));
					account.setPhoneno(phonenum);
					account.setIdcard(cardId);
					account.setCardtype(cardType);
					account.setAddress(String.valueOf(m.get("address")));
					account.setBirthday((Date)m.get("birthday"));
					account.setSex(String.valueOf(m.get("sexCode")));
//					account.setNationality(m.get("nationalityCode")== null ? 1 : Integer.parseInt(String.valueOf(m.get("nationalityCode"))));
					account.setRegdate(new Date());
					service.save(account, map);
				}
			}else{
				return  "该证件号码找不到个人档案信息，无法开通";
			}
		
		} catch (MPIServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "调用出错！";
		} 
		// TODO Auto-generated method stub
		return "开通成功";
	}
	
	
	@RpcService
	@Override
	public String isOpenHelathRecord(String cardType, String cardId, String phonenum) {
		Map<String, Object> o = service.queryForMap("JMHD_MEMBER", "CERTIFICATETYPECODE=? and CERTIFICATENO=? and PHONENUMBER= ?", new Object[]{cardType,cardId,phonenum});
		if(o== null){
			return null;
		}else{
			return String.valueOf(o.get("MPIID"));
		}
	}
}
