package org.apache.calcite.adapter.geode.application.domain.bookshop;

public class BookOrderItem {

	private int orderLine;

	private Integer itemNumber;

	private float quantity;

	private float discount;

	public BookOrderItem() {
	}

	public BookOrderItem(int orderLine, Integer itemNumber, float quantity,
			float discount) {
		super();
		this.orderLine = orderLine;
		this.itemNumber = itemNumber;
		this.quantity = quantity;
		this.discount = discount;
	}

	public int getOrderLine() {
		return orderLine;
	}

	public void setOrderLine(int orderLine) {
		this.orderLine = orderLine;
	}

	public Integer getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(Integer itemNumber) {
		this.itemNumber = itemNumber;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	@Override
	public String toString() {
		return "BookOrderItem [orderLine=" + orderLine + ", itemNumber="
				+ itemNumber + ", quantity=" + quantity + ", discount="
				+ discount + "]";
	}
}
