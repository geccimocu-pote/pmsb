package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.CustomerMaster;
import bean.Order;
import bean.ProductMaster;
import dao.CustomerMasterDAO;
import dao.ProductMasterDAO;
import dao.PurchaseOrderDAO;

@WebServlet("/order")
public class OrderMain extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("JSPから実行してください。");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

//セッション確認
		HttpSession session = request.getSession();
		boolean login = loginCheck(session);

// >>ログアウト状態のとき
		if (login == false) {
			request.setAttribute("alertMessage", "ログインができてません。ログインしてください。");
			request.getRequestDispatcher("login.jsp").forward(request, response);//ログイン画面を表示

// >>ログイン状態のとき↓↓
		} else {

			String getAct = request.getParameter("doAct");//hiddenの"doAct"
			String leadtime = request.getParameter("leadtime");//hiddenの"leadtime"
			Order orderbean=getScreenValue(request);//画面の値

			switch (getAct) {
				//「顧客コード」が入力されたとき。顧客名を表示する。
				case "searchCustomer":

					if(request.getParameter("customerNo") == "") {//➀もし顧客コードが空白に書換えられただけなら、
						orderbean.setCustomerName(null);//➁顧客名も空白にする。
						break;
					}

					CustomerMaster cm=searchCustomer(request);//顧客先マスタテーブル検索
					if(cm.getCustomerNo()==null) {//➀もし顧客コードがnullなら、
						request.setAttribute("alertMessage", "顧客マスタに登録がありません。");//➁アラート出して、
						orderbean.setCustomerNo(null);//➂顧客コードの入力をクリアする。
					}

					orderbean.setCustomerName(cm.getCustomerName());//顧客名set
					leadtime=String.valueOf(cm.getDelivaryLeadtime());//顧客リードタイムset
					break;

				//「品番」が入力されたとき。品名を表示する。
				case "searchProduct":
					if(request.getParameter("productNo") == "") {//➀もし品番が空白に書換えられただけなら、
						orderbean.setProductName(null);//➁品番も空白にする。
						break;
					}

					ProductMaster pm=searchProductName(request);//品番マスタテーブル検索
					if(pm.getProductNo()==null) {//➀もし品番がnullなら、
						request.setAttribute("alertMessage", "品番マスタに登録がありません。");//➁アラート出して、
						orderbean.setProductNo(null);//➂品番の入力をクリアする。
					}
					orderbean.setProductName(pm.getProductName());//品名set
					break;

				//「納期」が入力されたとき。値が過去日でないことをチェックする。
				case "checkDate":
					int input=Integer.parseInt(orderbean.getDeliveryDate().replace("-",""));//入力された年月日
					int now=ymd();//今日の年月日
					if(input < now) { //➀もし入力値が今日より前なら、
						request.setAttribute("alertMessage", "過去日は入力できません。");//➁アラート出して、
						orderbean.setDeliveryDate(null);//➂納期の入力をクリアする。
					}
					break;

				//「数量」が入力されたとき。値のチェックをする。
				case "checkQty":
					if(request.getParameter("poQty") == "") {//➀もし数量が空白に書換えられただけなら、
						break;//➁特に何もしない。
					}

					try{
						int qty=Integer.parseInt(orderbean.getPoQty());//入力された数量
						if(qty == 0) {//➀もし入力値が0なら、
							request.setAttribute("alertMessage", "数量に「0」は入力できません。");//➁アラート出して、
							orderbean.setPoQty(null);//➂数量の入力をクリアする。
						}else if(qty < 0){//➀入力値が0より小さければ、
							request.setAttribute("alertMessage", "数量にマイナスの値は入力できません。");//➁アラート出して、
							orderbean.setPoQty(null);//➂数量の入力をクリアする。
						}
					}catch(NumberFormatException e) {//➀数字以外が入力されたら、
						request.setAttribute("alertMessage", "数字を入力してください。");//➁アラート出して、
						orderbean.setPoQty(null);//➂数量の入力をクリアする。
					}
					break;

				//「登録」が押されたとき。入力情報をデータベースに登録する。
				case "regist":
					String message=checkBlank(request);//入力漏れがあればメッセージ内容を受け取る
					if(message != null) {//もし入力漏れがあれば、
						request.setAttribute("alertMessage", message);//アラート出す。
						break;
					}

					String shipdate=getShipDate(orderbean, Integer.parseInt(leadtime));//出荷日算出
					int result = insertPurchaseOrder(orderbean,shipdate,request);//受注テーブル更新
					if (result > 0) {//➀更新に成功したら、
						request.setAttribute("alertMessage", "受注情報を登録しました。");//➁アラート出して、
						orderbean=null;//➂画面をクリアする。
					}else {//➀更新に失敗したら、
						request.setAttribute("alertMessage", "登録に失敗しました。");//➁アラート出す。
					}
					break;

				//「キャンセル」が押されたとき。画面の入力値をリセットする。
				case "cancel":
					orderbean=null;
					break;

				default:
			}

			request.setAttribute("orderbean", orderbean);//画面の値
			request.setAttribute("leadtime", leadtime);//顧客リードターム
			request.getRequestDispatcher("order.jsp").forward(request, response);
		}
// >>ログイン状態のとき↑↑
	}



//----------------------------------------以下メソッド-------------------------------------------------

//【ログインチェック】
	public boolean loginCheck(HttpSession session) {
		String result = (String) session.getAttribute("Login");
		if (result == null) {
			return false;
		} else {
			return true;
		}
	}


//【画面の取得】
	private Order getScreenValue(HttpServletRequest request) {
		Order getScreen=new Order();
		getScreen.setCustomerName(request.getParameter("customerName"));
		getScreen.setCustomerNo(request.getParameter("customerNo"));
		getScreen.setDeliveryDate(request.getParameter("deliveryDate"));
		getScreen.setPoQty(request.getParameter("poQty"));
		getScreen.setProductName(request.getParameter("productName"));
		getScreen.setProductNo(request.getParameter("productNo"));
		return getScreen;
	}


//【顧客先マスタテーブル検索】検索キー：顧客コード
	private CustomerMaster searchCustomer (HttpServletRequest request) {
		CustomerMasterDAO dao=new CustomerMasterDAO();
		CustomerMaster bean=dao.searchCustomerMaster((request.getParameter("customerNo")));
		return bean;
	}


//【品番マスタテーブル検索】検索キー：品番
	public ProductMaster searchProductName(HttpServletRequest request) {
		ProductMasterDAO dao=new ProductMasterDAO();
		ProductMaster bean=dao.searchProductMaster((request.getParameter("productNo")));
		return bean;
	}


//【現在年月日】yyyyMMdd
	public int ymd(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		int date=Integer.parseInt(sdf.format(calendar.getTime()));
		return date;
	}


//【出荷日計算】yyyy/MM/dd
	public String getShipDate(Order orderbean, int leadtime) {
		Calendar calendar = Calendar.getInstance();
		String deliverydate=orderbean.getDeliveryDate().replace("-", "");
		String y=deliverydate.substring(0,4);
		String m=deliverydate.substring(4,6);
		String d=deliverydate.substring(6);
		calendar.set(Calendar.YEAR,Integer.parseInt(y));
		calendar.set(Calendar.MONTH,Integer.parseInt(m)-1);
		calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(d));
		calendar.add(Calendar.DAY_OF_MONTH, -leadtime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String shipdate=sdf.format(calendar.getTime());
		return shipdate;
	}


//【受注テーブル更新】insert
	public int insertPurchaseOrder(Order orderBean,String shipdate,HttpServletRequest request) {
		PurchaseOrderDAO dao=new PurchaseOrderDAO();
		int result=dao.insertPurchaseOrder(orderBean,shipdate, request);
		return result;
	}


//【入力漏れチェック】
	private String checkBlank(HttpServletRequest request) {
		String message=null;
		if(request.getParameter("deliveryDate").isEmpty() || request.getParameter("poQty").isEmpty()) {
			message="入力漏れがあります。";
		}
		return message;
	}
}
