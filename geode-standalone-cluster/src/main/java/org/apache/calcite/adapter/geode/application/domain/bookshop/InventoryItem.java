package org.apache.calcite.adapter.geode.application.domain.bookshop;

public class InventoryItem {

	private int itemNumber;

	private float costToXYZ;

	private float priceToCustomer;

	private float quantityInStock;

	private String vendor;

	private String location;


	public InventoryItem() {
	}

	public InventoryItem(int itemNumber, float costToXYZ,
			float priceToCustomer, float quantityInStock, String vendor,
			String location) {
		super();
		this.itemNumber = itemNumber;
		this.costToXYZ = costToXYZ;
		this.priceToCustomer = priceToCustomer;
		this.quantityInStock = quantityInStock;
		this.vendor = vendor;
		this.location = location;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getItemNumber() {
		return this.itemNumber;
	}

	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}

	public float getCostToXYZ() {
		return costToXYZ;
	}

	public void setCostToXYZ(float costToXYZ) {
		this.costToXYZ = costToXYZ;
	}

	public float getPriceToCustomer() {
		return priceToCustomer;
	}

	public void setPriceToCustomer(float priceToCustomer) {
		this.priceToCustomer = priceToCustomer;
	}

	public float getQuantityInStock() {
		return quantityInStock;
	}

	public void setQuantityInStock(float quantityInStock) {
		this.quantityInStock = quantityInStock;
	}

	@Override
	public String toString() {
		return "InventoryItem [itemNumber=" + itemNumber + ", quantityInStock="
				+ quantityInStock + ", location=" + location + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(costToXYZ);
		result = prime * result + itemNumber;
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + Float.floatToIntBits(priceToCustomer);
		result = prime * result + Float.floatToIntBits(quantityInStock);
		result = prime * result + ((vendor == null) ? 0 : vendor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InventoryItem other = (InventoryItem) obj;
		if (Float.floatToIntBits(costToXYZ) != Float
				.floatToIntBits(other.costToXYZ))
			return false;
		if (itemNumber != other.itemNumber)
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		}
		else if (!location.equals(other.location))
			return false;
		if (Float.floatToIntBits(priceToCustomer) != Float
				.floatToIntBits(other.priceToCustomer))
			return false;
		if (Float.floatToIntBits(quantityInStock) != Float
				.floatToIntBits(other.quantityInStock))
			return false;
		if (vendor == null) {
			if (other.vendor != null)
				return false;
		}
		else if (!vendor.equals(other.vendor))
			return false;
		return true;
	}

}
