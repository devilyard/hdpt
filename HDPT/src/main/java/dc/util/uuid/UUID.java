package dc.util.uuid;

public class UUID {

	private String mpiID;
	private String sessionID;

	public UUID(String sUuid) throws UUIDInitException {
		sUuid = sUuid.replace(".ehr", "");
		String[] uuid = sUuid.split("-");
		if (uuid.length == 1) {
			mpiID = uuid[0];
		} else if (uuid.length == 2) {
			sessionID = uuid[0];
			mpiID = uuid[1];
		} else {
			throw new UUIDInitException("Invalid uuid.");
		}
	}

	public String getMpiID() {
		return mpiID;
	}

	public String getSessionID() {
		return sessionID;
	}
}
