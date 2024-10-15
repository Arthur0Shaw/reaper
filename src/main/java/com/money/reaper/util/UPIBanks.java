package com.money.reaper.util;

public enum UPIBanks {

	STATE_BANK_OF_INDIA("State Bank of India"),
	ICICI_BANK("ICICI Bank"),
	BANDHAN_BANK("Bandhan Bank");

	private final String displayName;

	UPIBanks(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String toString() {
		return displayName;
	}

}
