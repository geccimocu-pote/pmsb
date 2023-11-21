package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.OrderList;
import bean.ProductMaster;
import bean.PurchaseOrder;
import dao.ProductMasterDAO;
import dao.PurchaseOrderDAO;

/**
 * Servlet implementation class OrderListMain
 */
@WebServlet("/OrderListMain")
public class OrderListMain extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();
		out.println("JSPから実行してください。");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		//セッション確認
		HttpSession session = request.getSession();
		boolean login = loginCheck(session);
		if (login == false) {
			request.setAttribute("alertMessage", "ログインができてません。ログインしてください。");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} else {

			//リクエストパラメーターのbunkiの値を取得する。
			String getBunki = request.getParameter("bunki");

			//画面の入力情報を取得してOrderListビーンに収める。
			OrderList orderBean = getScreenValue(request);

			//一覧データ格納用PurchaseOrderビーンを用意する。
			List<PurchaseOrder> getorederlist = null;

			//bunkiの値の内容で処理を分岐する。
			switch (getBunki) {

			//品番が入力された場合
			case "searchProductNo":
				ProductMaster getOrder = searchProductNoMethod(request); //ProductNoで検索するメソッド

				//入力されたProductNoがデータベーステーブルに未登録の場合
				//エラーメッセージを、登録ありの場合orderlistBeanに値を渡す。
				if (getOrder.getProductName() == null) {

					if (request.getParameter("productNo") == null || request.getParameter("productNo").isEmpty()) {
						request.getRequestDispatcher("orderList.jsp").forward(request, response);

						break;

					} else {

						orderBean.setProductName(""); //品番に登録がないときは、品名をカラにする。

						//アラート用のメッセージをリクエスト属性で保存
						request.setAttribute("alertMessage", "入力された品番は登録されていません。");
					}

				//品番があるとき、受注テーブルの中の同じ品番を探す。
				} else {
					orderBean.setProductName(getOrder.getProductName());
					PurchaseOrderDAO pod = new PurchaseOrderDAO();
					getorederlist = pod.searchPurchseOrder(orderBean.getProductNo());
				}

				break;

			default:
			}

			//画面に表示する情報（ビーン）をリクエスト属性で保存する。
			//名前："orderlist"  /   値：orderlistBean
			request.setAttribute("orderlist", getorederlist);
			request.setAttribute("screenBean", orderBean);

			//画面を遷移(せんい)する。
			request.getRequestDispatcher("orderList.jsp").forward(request, response);
		}
	}

	/**
	 * リクエストパラメータProductNoから値を取得して、データベーステーブルに
	 * 問い合わせて、OrderListビーンで受け取ってOrderListビーンで戻す。
	 * @param request
	 * @return OrderList
	 */
	// ProductMasterは、品番マスタテーブル。
	private ProductMaster searchProductNoMethod(HttpServletRequest request) {
		//1.リクエストパラメータProductNoから値を取得する。
		String getProductNo = request.getParameter("productNo");

		//2.ProductMasterDAOをインスタンス化する。
		ProductMasterDAO ud = new ProductMasterDAO();

		//3.searchメソッドを呼び出す。引数はgetProductNo,
		//  戻り値はOrderListビーンなので、OrderListビーンで受け取る。
		ProductMaster getOrderList = ud.searchProductMaster(getProductNo);

		//4.受け取ったOrderListビーンを戻す。
		return getOrderList;

	}

	/**
	 * 画面のリクエストパラメーターを取得し、OrderListビーンに格納するメソッドはgetScreenValue
	 * @param request
	 * @return OrderList
	 */
	private OrderList getScreenValue(HttpServletRequest request) {
		//画面に入力された情報をOrderListビーンに格納する。
		OrderList orderlistBean = new OrderList(); //このOrderListBeanを戻り値とする。

		//jspの name="productNo"  の値を取得して、OrderListBeanのproductNoにセットする。
		orderlistBean.setProductNo(request.getParameter("productNo"));
		orderlistBean.setProductName(request.getParameter("productName"));

		//画面情報が格納されたOrderListBeanを戻す。
		return orderlistBean;
	}

	//----------------------------------------------------------------------------

	//ログインのセッションがあるかチェック
	public boolean loginCheck(HttpSession session) {
		String result = (String) session.getAttribute("Login");
		if (result == null) {
			return false;
		} else {
			return true;
		}

	}

}
