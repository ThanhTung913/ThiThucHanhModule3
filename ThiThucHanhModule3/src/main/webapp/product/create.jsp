<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>Create Product</title>
    <%@include file="/dashboard/layout/head.jsp" %>
    <style>

    </style>
</head>

<body>

<!-- Begin page -->
<div id="wrapper">


    <!-- Topbar Start -->
    <%@include file="/dashboard/layout/topbar-noSearch.jsp" %>
    <!-- end Topbar -->
    <!-- ========== Left Sidebar Start ========== -->

    <!-- Left Sidebar End -->
    <%@include file="/dashboard/layout/left-sidebar.jsp" %>
    <!-- ============================================================== -->
    <!-- Start Page Content here -->
    <!-- ============================================================== -->

    <div class="content-page">
        <div class="content">

            <!-- Start Content-->

            <!-- end col -->


        </div>
        <!-- End row -->

        <!-- LỌM -->
        <div class="card-body">
            <h4>
                <a href="/products?action=list">ListProduct</a>
            </h4>
            <h4 style="text-align: center; font-size: 20px; font-family: 'American Typewriter' "
                class="header-title mb-4">CREATE PRODUCT</h4>
            <c:choose>
                <c:when test="${errors1 == null }">
                    </form>
                    <c:if test="${requestScope['success'] == true}">
                        <ul class="alert alert-success col-3" style="list-style-type: none; width: 200px">
                            <li style="font-size: 15px">Sửa thành công</li>
                        </ul>
                    </c:if>
                    <form method="post" enctype="multipart/form-data">
                        <input type="hidden" name="id" value="${product.getId()}">
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label class="col-form-label" style="color: black ">Name</label>
                                <input type="text" class="form-control" name="name"
                                       style="color: black " value="<c:out value='${product.getName()}' />">
                            </div>
                            <div class="form-group col-md-6">
                                <label class="col-form-label" style="color: black ">Price</label>
                                <input type="text" class="form-control" name="price" style="color: black "
                                       value="<c:out value='${product.getPrice()}' />">
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label class="col-form-label" style="color: black ">Quantity</label>
                                <input type="text" class="form-control" placeholder="Name product" name="quantity"
                                       style="color: black " value="<c:out value='${product.getQuantity()}' />">
                            </div>
                            <div class="form-group col-md-6">
                                <label class="col-form-label" style="color: black ">Color</label>
                                <input type="text" class="form-control" name="color" style="color: black "
                                       value="<c:out value='${product.getColor()}' />">
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label class="col-form-label" style="color: black ">Description</label>
                                <input type="text" class="form-control" placeholder="Name product" name="description"
                                       style="color: black " value="<c:out value='${product.getDescription()}' />">
                            </div>
                            <div class="form-group col-md-6">
                                <label class="col-form-label" style="color: black ">Category</label>
                                <select class="form-control" name="category" style="color: black ">
                                    <<c:forEach items="${applicationScope.listCategory }" var="category">

                                    <c:choose>
                                        <c:when test="${category.getId() == product.getCategory() }">
                                            <option value="${category.getId() }"
                                                    selected>${category.getName() }</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${category.getId() }">${category.getName() }</option>
                                        </c:otherwise>
                                    </c:choose>

                                </c:forEach>
                                </select>

                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary waves-effect waves-light btn-sm" id="sa-success">
                            SUBMIT
                        </button>

                    </form>
                    <c:if test="${check}">
                        <ul class="alert alert-danger col-3" style="list-style-type: none; width: 200px">
                            <li style="font-size: 15px">${error}</li>
                        </ul>
                    </c:if>
                    <c:if test="${!requestScope['errors'].isEmpty()}">
                        <ul style="list-style-type: none">
                            <c:forEach items="${requestScope['errors']}" var="errors">
                                <li class="alert alert-danger col-3" style="font-size: 15px">
                                        ${errors}
                                </li>
                            </c:forEach>
                        </ul>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <ul style="list-style-type: none">
                        <c:forEach items="${requestScope['errors1']}" var="errors1">
                            <li class="alert alert-danger col-3" style="font-size: 15px">
                                    ${errors1}
                            </li>
                        </c:forEach>
                    </ul>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- END LỌM -->
    </div>
    <!-- end container-fluid -->

</div>
<!-- end content -->


<!-- Footer Start -->
<footer class="footer">
    <div class="container-fluid">
        <div class="row">
            <%--            <div class="col-md-12">--%>
            <%--                2015 - 2020 &copy; Velonic theme by <a href="">Coderthemes</a>--%>
            <%--            </div>--%>
        </div>
    </div>
</footer>
<!-- end Footer -->

</div>

<!-- ============================================================== -->
<!-- End Page content -->
<!-- ============================================================== -->

</div>
<!-- END wrapper -->


<!-- Right Sidebar -->
<%@include file="/dashboard/layout/rightbar.jsp" %>
<!-- /Right-bar -->


<%@include file="/dashboard/layout/scrip.jsp" %>
</body>

</html>