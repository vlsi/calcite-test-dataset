package org.apache.calcite.adapter.geode.application.domain.bookshop;

public class Address {

	private String addressLine1;

	private String addressLine2;

	private String addressLine3;

	private String city;

	private String state;

	private String postalCode;

	private String country;

	private String phoneNumber;

	private String addressTag;

	public Address() {
	}

	public Address(String postalCode) {
		this.postalCode = postalCode;
	}

	public Address(String addressLine1, String addressLine2,
			String addressLine3, String city, String state, String postalCode,
			String country, String phoneNumber, String addressTag) {

		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.addressLine3 = addressLine3;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
		this.country = country;
		this.phoneNumber = phoneNumber;
		this.addressTag = addressTag;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddressTag() {
		return addressTag;
	}

	public void setAddressTag(String addressTag) {
		this.addressTag = addressTag;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Address address = (Address) o;

		if (addressLine1 != null ? !addressLine1.equals(address.addressLine1) : address.addressLine1 != null)
			return false;
		if (addressLine2 != null ? !addressLine2.equals(address.addressLine2) : address.addressLine2 != null)
			return false;
		if (addressLine3 != null ? !addressLine3.equals(address.addressLine3) : address.addressLine3 != null)
			return false;
		if (city != null ? !city.equals(address.city) : address.city != null) return false;
		if (state != null ? !state.equals(address.state) : address.state != null) return false;
		if (postalCode != null ? !postalCode.equals(address.postalCode) : address.postalCode != null) return false;
		if (country != null ? !country.equals(address.country) : address.country != null) return false;
		if (phoneNumber != null ? !phoneNumber.equals(address.phoneNumber) : address.phoneNumber != null) return false;
		return addressTag != null ? addressTag.equals(address.addressTag) : address.addressTag == null;
	}

	@Override
	public int hashCode() {
		int result = addressLine1 != null ? addressLine1.hashCode() : 0;
		result = 31 * result + (addressLine2 != null ? addressLine2.hashCode() : 0);
		result = 31 * result + (addressLine3 != null ? addressLine3.hashCode() : 0);
		result = 31 * result + (city != null ? city.hashCode() : 0);
		result = 31 * result + (state != null ? state.hashCode() : 0);
		result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
		result = 31 * result + (country != null ? country.hashCode() : 0);
		result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
		result = 31 * result + (addressTag != null ? addressTag.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Address [addressTag=" + addressTag + ", addressLine1="
				+ addressLine1 + ", city=" + city + ", state=" + state
				+ ", postalCode=" + postalCode + ", country=" + country
				+ ", phoneNumber=" + phoneNumber + "]";
	}
}
