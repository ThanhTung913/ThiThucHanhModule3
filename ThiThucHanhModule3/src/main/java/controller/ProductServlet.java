package controller;

import dao.*;
import model.Product;
import utlis.ValidateUltis;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "productServlet", urlPatterns = "/products")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 50, // 50MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB


public class ProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private IProductDAO iProductDAO;
    private ICategoryDAO iCategoryDAO;

    private String errors = "";

    public void init() {
        iProductDAO = new ProductDAO();
        iCategoryDAO = new CategoryDAO();

        if (this.getServletContext().getAttribute("listCategory") == null) {
            try {
                this.getServletContext().setAttribute("listCategory", iCategoryDAO.selectAllCategory());
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create":
                showCreateForm(req, resp);
                break;
            case "edit":
                try {
                    showEditFormProduct(req, resp);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case "delete":
                try {
                    deleteProduct(req, resp);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                try {
//                    listProduct(req, resp);
                    listNumberPage(req, resp);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        Product product = new Product();
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create":
                try {
                    insertProduct(req, resp);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case "edit":
                try {
                    editProduct(req, resp);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case "search":
                try {
                    searchUserByName(req, resp);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
//            case "delete":
//                try {
//                    deleteProduct(req, resp);
//                } catch (SQLException | ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//                break;
        }


    }

    private void deleteProduct(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ClassNotFoundException, ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        List<String> errors = new ArrayList<>();
        if (!iProductDAO.checkDuplicateById(id)) {
            errors.add("ID không tồn tại");
        }
        if (errors.size() == 0) {
            iProductDAO.deleteProduct(id);
            List<Product> listProduct = iProductDAO.selectAllProduct();
            req.setAttribute("listProduct", listProduct);
        }
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/product/listProduct.jsp");
        requestDispatcher.forward(req, resp);
    }

    private void searchUserByName(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ClassNotFoundException, ServletException, IOException {
        String searchProduct = req.getParameter("searchProduct");
        List<Product> listProduct = iProductDAO.searchByName(searchProduct);
        req.setAttribute("listProduct", listProduct);
        listNumberPage(req, resp);
    }

    private void showCreateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("Server part: " + this.getServletContext().getRealPath("/"));
        Product product = new Product();
        req.setAttribute("product", product);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/product/create.jsp");
        requestDispatcher.forward(req, resp);
    }

    private void showEditFormProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException, ClassNotFoundException, ServletException {
        resp.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("utf-8");

        RequestDispatcher dispatcher = req.getRequestDispatcher("/product/edit.jsp");
        Product product = new Product();
        List<String> errors1 = new ArrayList<>();
        String id = req.getParameter("id");
        if (!ValidateUltis.isNumber(id)) {
            errors1.add("ID không hợp lệ");
        } else {
            if (!iProductDAO.checkDuplicateById(Integer.parseInt(id))) {
                errors1.add("Id không tồn tại");
            }
        }
        if (errors1.size() == 0) {
            product = iProductDAO.selectProductById(Integer.parseInt(id));
            req.setAttribute("product", product);
        }
        if (errors1.size() > 0) {
            req.setAttribute("errors1", errors1);
        }
        dispatcher.forward(req, resp);

    }

    private void editProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException, ClassNotFoundException, ServletException {
        resp.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("utf-8");
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/product/edit.jsp");
        Product product = null;
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String price = req.getParameter("price");
        String quantity = req.getParameter("quantity");
        String color = req.getParameter("color");
        String description = req.getParameter("description");
        int category = Integer.parseInt(req.getParameter("category"));
        List<String> errors = new ArrayList<>();
        if (!ValidateUltis.isNumber(id) || !iProductDAO.checkDuplicateById(Integer.parseInt(id))) {
            req.setAttribute("error", "Không xác định được id");
            req.setAttribute("check", true);

            RequestDispatcher requestDispatcher1 = req.getRequestDispatcher("/product/errorEdit.jsp");
            requestDispatcher1.forward(req, resp);
        } else {
            List<String> errors1 = new ArrayList<>();
            product = new Product(Integer.parseInt(id), name, Integer.parseInt(price), Integer.parseInt(quantity), color, description, category);
            if (id.isEmpty() || Integer.parseInt(id) < 0) {
                errors1.add("id không tồn tại");
            }
            if (price == null || price.equals("")) {
                errors.add("Giá sản phẩm không được để trống");
            }
            if (!ValidateUltis.isNumber(price)) {
                errors.add("Giá sản phẩm không đúng định dạng (vd: 50000)");
            }
            if (Integer.parseInt(price) < 1000000 || Integer.parseInt(price) > 100000000) {
                errors.add("Giá sản phẩm phải lớn hơn 1000 hoặc nhỏ hơn 100 Triệu");
            }
            if (name.isEmpty()) {
                errors.add("Tên không được để trống");
            }

            Product nameProduct = iProductDAO.selectProductById(Integer.parseInt(id));
            String checkname = nameProduct.getName();
            if (iProductDAO.checkDuplicateNameProduct(name) && !name.equals(checkname)) {
                errors.add("Tên sản phẩm đã tồn tại");

            }
            if (errors.size() == 0) {
                product = new Product(Integer.parseInt(id), name, Integer.parseInt(price), Integer.parseInt(quantity), color, description, category);
                iProductDAO.updateProduct(product);
                req.setAttribute("product", product);
                req.setAttribute("success", true);
            }
            if (errors.size() > 0) {
                req.setAttribute("errors", errors);
                req.setAttribute("product", product);
            }
            List<Product> listPrpduct = iProductDAO.selectAllProduct();
            req.setAttribute("listProduct", listPrpduct);
            req.getRequestDispatcher("/products");
            requestDispatcher.forward(req, resp);

        }
//        String name = req.getParameter("name");
//        String price = req.getParameter("price");
//        String quantity = req.getParameter("quantity");
//        String color = req.getParameter("color");
//        String description = req.getParameter("description");
//        int category = Integer.parseInt(req.getParameter("category"));
//        Product newProduct = new Product(name, Integer.parseInt(price), Integer.parseInt(quantity), color, description, category);
//        iProductDAO.inserProduct(newProduct);
//        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/product/create.jsp");
//        requestDispatcher.forward(req, resp);
    }

    private void insertProduct(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException, SQLException, ClassNotFoundException {
        Product product = null;
        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String price = req.getParameter("price");
        String quantity = req.getParameter("quantity");
        String description = req.getParameter("description");
        String color = req.getParameter("color");
        List<String> errors = new ArrayList<>();
        int category = Integer.parseInt(req.getParameter("category"));
        product = new Product(id, name, Integer.parseInt(price), Integer.parseInt(quantity), color, description, category);
//        product = new Product(name, Integer.parseInt(price), Integer.parseInt(quantity), color, description, category);
        if (price == null || price.equals("")) {
            errors.add("Giá sản phẩm không được để trống");
        }
        if (!ValidateUltis.isNumber(price)) {
            errors.add("Giá sản phẩm không đúng định dạng (vd: 50000)");
        }
        if (Integer.parseInt(price) < 1000 || Integer.parseInt(price) > 1000000000) {
            errors.add("Giá sản phẩm phải lớn hơn 1000 hoặc nhỏ hơn 100 Triệu");
        }

        if (name.isEmpty()) {
            errors.add("Tên không được để trống");
        }

        if (iProductDAO.checkDuplicateNameProduct(name)) {
            errors.add("Tên sản phẩm đã tồn tại");
        }
        if (errors.size() == 0) {
//            product = new Product(name, Integer.parseInt(price), Integer.parseInt(quantity), color, description, category);
            product = new Product(id, name, Integer.parseInt(price), Integer.parseInt(quantity), color, description, category);
            String success = "Thêm sản phẩm thành công";
            iProductDAO.inserProduct(product);
            req.setAttribute("product", product);
            req.setAttribute("success", success);
            req.setAttribute("check", true);
        }
        if (errors.size() > 0) {
            req.setAttribute("errors", errors);
            req.setAttribute("product", product);
        }

//        List<Product> listPrpduct = iProductDAO.selectAllProduct();
//        req.setAttribute("listProduct", listPrpduct);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/product/create.jsp");
        requestDispatcher.forward(req, resp);
//        resp.sendRedirect("/products");
    }

    private void listNumberPage(HttpServletRequest req, HttpServletResponse resp) throws
            SQLException, ClassNotFoundException, ServletException, IOException {
        System.out.println("numberPage");
        int page = 1;
        int recordsPerPage = 1000;
        if (req.getParameter("page") != null) {
            page = Integer.parseInt(req.getParameter("page"));
        }
        String searchProduct = "";
        if (req.getParameter("searchProduct") != null) {
            searchProduct = req.getParameter("searchProduct");
        }
        List<Product> listProduct = iProductDAO.getNumberPage((page - 1) * recordsPerPage, recordsPerPage, searchProduct);
        int noOfRecords = iProductDAO.getNoOfRecords();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        req.setAttribute("listProduct", listProduct);
        req.setAttribute("noOfPages", noOfPages);
        req.setAttribute("currentPage", page);
        req.setAttribute("searchProduct", searchProduct);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/product/listProduct.jsp");
        requestDispatcher.forward(req, resp);
    }
}


