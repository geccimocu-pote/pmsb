package bean;

public class PurchaseOrder {
	private String poNo;
	private String customerNo;
	private String productNo;
	private int poQty;
	private String deliveryDate;
	private String shipDate;
	private int finFlg;
	private String orderDate;
	private String registuser;


	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public int getPoQty() {
		return poQty;
	}
	public void setPoQty(int poQty) {
		this.poQty = poQty;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getShipDate() {
		return shipDate;
	}
	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}
	public int getFinFlg() {
		return finFlg;
	}
	public void setFinFlg(int finFlg) {
		this.finFlg = finFlg;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getRegistuser() {
		return registuser;
	}
	public void setRegistuser(String registuser) {
		this.registuser = registuser;
	}

}
