package main;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Delivery;
import bean.ProductMaster;
import bean.ProductOrder;
import dao.ProductMasterDAO;
import dao.ProductOrderDAO;
import dao.ProductStockDAO;

@WebServlet("/delivery")
public class DeliveryMain extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("JSPから実行してください。");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
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

			String getAct = request.getParameter("doAct");
			Delivery delibean = getScreenValue(request);

			switch (getAct) {
				case "search"://[発注番号]入力されたとき。発注テーブルとかから情報ひっぱてくる。
					if(request.getParameter("orderNo") == "") {
						delibean=null;
						break;
					}
					//発注テーブルから発注番号のデータ取得。なければエラー。
					ProductOrder po=searchProductOrder(request);
					if(po.getOrderNo() == null) {
						request.setAttribute("alertMessage", "注文番号が存在しません。");
						delibean.setOrderNo(null);
					}
					//取得したデータを画面用beanに入れてく。
					delibean.setOrderNo(po.getOrderNo());//注文番号
					delibean.setRegistdate(po.getRegistdate());//発注日
					delibean.setProductNo(po.getProductNo());//品番
					delibean.setOrderQty(String.valueOf(po.getOrderQty()));//注文数量
					//品名を探しに品番マスタテーブル検索。
					String productNo=delibean.getProductNo();//（検索ワード「品番」渡す準備）
					ProductMaster pm=searchProductMaster(request,productNo);
					delibean.setProductName(pm.getProductName());//品名
					break;


				case "check":
					if(request.getParameter("dueQty") == "") {
						delibean.setDueQty(null);
						break;
					}
					int result = qtyCheck(request, delibean);
					if(result == 0) {
						request.setAttribute("alertMessage", "入力された数量と注文数量に差があります。");
						delibean.setDueQty(null);
					}
					break;

				case "regist"://登録ボタン押されたとき。テーブル更新。
					String message=checkBlank(request);
					if(message != null) {
						request.setAttribute("alertMessage", message);
						break;
					}
					//➀発注テーブル更新
					int result1 = registProductorder(request,delibean);
					if(result1 == 0) {
						request.setAttribute("alertMessage", "発注テーブルの更新に失敗しました。");
						break;
					}
					//➁在庫テーブル更新
					int result2 = registProductStock(request,delibean);
					if(result2 == 0) {
						request.setAttribute("alertMessage", "在庫テーブルの更新に失敗しました。");
						break;
					}
					request.setAttribute("alertMessage", "注文番号 [" +delibean.getOrderNo()+"] の納入処理を完了しました。");
					delibean=null;
					break;

				case "cancel":
					delibean=null;
					break;

				default:
			}

			request.setAttribute("delibean",delibean );
			request.getRequestDispatcher("delivery.jsp").forward(request, response);
		}
// >>ログイン状態のとき↑↑
	}

//----------------------------------------以下メソッド-------------------------------------------------

//ログインのセッションがあるかチェック
		public boolean loginCheck(HttpSession session) {
			String result = (String) session.getAttribute("Login");
			if (result == null) {
				return false;
			} else {
				return true;
			}
		}

//【画面の入力値取得】
		private Delivery getScreenValue(HttpServletRequest request) {
			Delivery getScreen=new Delivery();
			getScreen.setDueQty(request.getParameter("dueQty"));
			getScreen.setRegistdate(request.getParameter("registdate"));
			getScreen.setOrderNo(request.getParameter("orderNo"));
			getScreen.setOrderQty(request.getParameter("orderQty"));
			getScreen.setProductName(request.getParameter("productName"));
			getScreen.setProductNo(request.getParameter("productNo"));
			return getScreen;
		}

//【注文番号で検索。受注テーブル】
		private ProductOrder searchProductOrder(HttpServletRequest request) {
			ProductOrderDAO dao=new ProductOrderDAO();
			ProductOrder pobean=dao.searchProductOrder(request.getParameter("orderNo"));
			return pobean;

		}

//【品番で品名検索。品番マスタテーブル】
		private ProductMaster searchProductMaster(HttpServletRequest request,String productNo) {
			ProductMasterDAO dao=new ProductMasterDAO();
			ProductMaster pmbean=dao.searchProductMaster(productNo);
			return pmbean;
		}

//【発注テーブル更新。update】
		private int registProductorder(HttpServletRequest request,Delivery delibean) {
			ProductOrderDAO dao=new ProductOrderDAO();
			int result=dao.updateProductOrder(delibean, request);
			return result;
		}

//【在庫テーブル更新。update】
		private int registProductStock(HttpServletRequest request,Delivery delibean) {
			ProductStockDAO dao=new ProductStockDAO();
			int result=dao.updateProductStockDelivery(delibean);
			return result;
		}

//【注文数収支チェック】
		private int qtyCheck(HttpServletRequest request,Delivery delibean){
			int result=0;
			if(delibean.getOrderQty().equals(request.getParameter("dueQty"))) {
				result=1;
			}
			return result;
		}

//【入力漏れチェック】
		private String checkBlank(HttpServletRequest request) {
			String message=null;
			if(request.getParameter("dueQty").isEmpty() || request.getParameter("dueQty").isEmpty()){
				message="納入数量を入力してください。";
			}
			return message;
		}

}