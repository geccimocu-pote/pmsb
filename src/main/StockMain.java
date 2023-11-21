package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Stock;
import dao.ProductMasterDAO;
import dao.ProductOrderDAO;
import dao.ProductStockDAO;
import dao.PurchaseOrderDAO;
import tool.DateUtils;

/**
 * Servlet implementation class StockMain
 */
@WebServlet("/stock")
public class StockMain extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		//Mainjavaから実行したときのエラー画面
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
			//onChenge属性使用
			String getBunki = request.getParameter("bunki");

			switch (getBunki) {
			case "seachkood":
				//Stockビーンに商品品番格納



				Stock stockmasterbean = searchproductMaster(request);

				if (stockmasterbean.getProductName() == null) {
					//品番マスタに登録のない品番
					request.setAttribute("alertMessage", "入力がないか入力された商品は登録されていません");
					request.getRequestDispatcher("stock.jsp")
							.forward(request, response);
				} else {
					//品番マスタに登録のある品番
					List<Stock> list = new ArrayList<Stock>();
					//Stockビーンのリストを作成
					list = searchproductstock(request, list);//在庫テーブル
					list = searchproductorder(request, list);//発注テーブル
					list = searchpurchaseorder(request, list);//受注テーブル

					if (list== null || list.isEmpty()) {
						request.setAttribute("alertMessage", "テーブルにエラーを発見しました。");
						request.getRequestDispatcher("calc.jsp")
								.forward(request, response);
					}

					// ソート （日付の順）
					List<Stock> sortBean = new ArrayList<>();
					sortBean = list.stream().sorted(Comparator.comparing(Stock::getYmd))
							.collect(Collectors.toList());

					list = sortBean;
					//在庫数を取得
					int zaiko = Integer.parseInt(list.get(0).getStockQty());
					//Stockビーンに格納したリストを順番に計算
					for (int i = 1; i < list.size(); i++) {
						//ひとつ前の在庫をint化し取得
						if (list.get(i).getStockQty() == null) {
							zaiko = Integer.parseInt(list.get(i - 1).getStockQty());
							//受注があるか確認
							int ponumber = 0;
							if (list.get(i).getPoQty() == null) {
							} else {
								//受注数をint化し、在庫数引く受注数の計算
								ponumber = Integer.parseInt(list.get(i).getPoQty());
								list.get(i).setStockQty(String.valueOf(zaiko - ponumber));
							}
							//発注があるか確認
							int ordernumber = 0;
							if (list.get(i).getOrderQty() == null) {
							} else {
								//発注数をint化し、在庫数足す発注数の計算
								ordernumber = Integer.parseInt(list.get(i).getOrderQty());
								list.get(i).setStockQty(String.valueOf(zaiko + ordernumber));
							}
						}
					}
					//画面に品名とStockビーンにリスト化した値をテーブルで表示
					request.setAttribute("listorder", list);
					request.setAttribute("name", stockmasterbean);

					break;

				}
			}

			request.getRequestDispatcher("stock.jsp").forward(request, response);

		}
	}

	//在庫テーブルから該当する品番の在庫を取得
	private List<Stock> searchproductstock(HttpServletRequest request, List<Stock> list) {
		String getmasterkod = request.getParameter("productNo");
		ProductStockDAO pod = new ProductStockDAO();
		Stock stockproductbean = pod.searchproductStock(getmasterkod);

		if (stockproductbean.getOrderNo() == null) {

			pod.newProductStockDataMake(DateUtils.getFormatDateYYYYMM(), getmasterkod);
			stockproductbean = pod.searchproductStock(getmasterkod);
		}

		list.add(stockproductbean);
		return list;
	}

	//受注テーブルから該当する品番の受注状況を取得
	private List<Stock> searchpurchaseorder(HttpServletRequest request, List<Stock> list) {
		String getmasterkod = request.getParameter("productNo");
		PurchaseOrderDAO pod = new PurchaseOrderDAO();
		list = pod.searchPurchase(getmasterkod, list);

		return list;

	}

	//発注テーブルから該当する品番の発注状況を取得
	private List<Stock> searchproductorder(HttpServletRequest request, List<Stock> list) {
		String getmasterkod = request.getParameter("productNo");
		ProductOrderDAO pod = new ProductOrderDAO();
		list = pod.searchOrder(getmasterkod, list);
		return list;
	}

	//品番マスターから品番の有無を確認。品名を取得。
	private Stock searchproductMaster(HttpServletRequest request) {

		String getmasterkod = request.getParameter("productNo");
		ProductMasterDAO pmd = new ProductMasterDAO();
		Stock stockmasterbean = pmd.searchStock(getmasterkod);
		return stockmasterbean;
	}

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
