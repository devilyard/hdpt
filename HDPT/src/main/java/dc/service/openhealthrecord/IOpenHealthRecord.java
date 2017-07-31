package dc.service.openhealthrecord;

import ctd.annotation.RpcService;

public interface IOpenHealthRecord {
	@RpcService
	public String openHealthRecord(String cardType, String cardId, String phonenum);

	@RpcService
	public String isOpenHelathRecord(String cardType, String cardId, String phonenum);
}
