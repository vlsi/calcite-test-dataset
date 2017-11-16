package org.apache.calcite.adapter.geode.application.domain.bookshop;

import java.util.ArrayList;

public class Customer {

	private Integer customerNumber;

	private String firstName;

	private String lastName;

	private Address primaryAddress;

	private ArrayList<Integer> myBookOrders;


	public Customer() {
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public Customer(int customerNumber, String firstName, String lastName) {
		super();
		this.customerNumber = customerNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.myBookOrders = new ArrayList();

	}

	public Customer(int customerNumber, String firstName, String lastName, String postalCode) {
		this(customerNumber, firstName, lastName);
		this.primaryAddress = new Address(postalCode);
	}

	public Customer(Integer customerNumber, String firstName,
			String lastName, Address address,
			ArrayList<Integer> orders) {
		super();
		this.customerNumber = customerNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.primaryAddress = address;
		this.myBookOrders = orders;
	}

	public Customer(Integer customerNumber, String firstName,
			String lastName, Address address) {
		super();
		this.customerNumber = customerNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.primaryAddress = address;
	}

	public void addOrder(Integer orderKey) {
		if (myBookOrders == null) {
			myBookOrders = new ArrayList<>();
		}

		myBookOrders.add(orderKey);
	}

	public ArrayList<Integer> getMyBookOrders() {
		return myBookOrders;
	}

	public void setMyBookOrders(ArrayList<Integer> orders) {
		this.myBookOrders = orders;
	}

	public Integer getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(Integer customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customerNumber == null) ? 0 : customerNumber.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((myBookOrders == null) ? 0 : myBookOrders.hashCode());
		result = prime * result
				+ ((primaryAddress == null) ? 0 : primaryAddress.hashCode());
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
		Customer other = (Customer) obj;
		if (customerNumber == null) {
			if (other.customerNumber != null)
				return false;
		}
		else if (!customerNumber.equals(other.customerNumber))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		}
		else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		}
		else if (!lastName.equals(other.lastName))
			return false;
		if (myBookOrders == null) {
			if (other.myBookOrders != null)
				return false;
		}
		else if (!myBookOrders.equals(other.myBookOrders))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Customer [customerNumber=" + customerNumber
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", postalCode=" + getPrimaryAddress().getPostalCode() + "]";
	}

	public Address getPrimaryAddress() {
		return primaryAddress;
	}

	public void setPrimaryAddress(Address primaryAddress) {
		this.primaryAddress = primaryAddress;
	}

}
