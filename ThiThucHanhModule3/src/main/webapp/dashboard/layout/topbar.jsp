<%--
  Created by IntelliJ IDEA.
  User: prom1
  Date: 18/07/2022
  Time: 11:47
  To change this template use File | Settings | File Templates.
--%>
<%@include file="head.jsp" %>
<div class="navbar-custom">
    <ul class="list-unstyled topnav-menu float-right mb-0">

    </ul>

    <!-- LOGO -->
    <div class="logo-box">
        <a href="users?action=menu" class="logo text-center logo-light">
                    <span class="logo-lg">
                        <img src="/dashboard/assets/images/logo-light.png" alt="" height="18">
                        <!-- <span class="logo-lg-text-dark">Velonic</span> -->
                    </span>
            <span class="logo-sm">
                        <!-- <span class="logo-lg-text-dark">V</span> -->
                        <img src="/dashboard/assets/images/logo-sm.png" alt="" height="22">
                    </span>
        </a>
    </div>

    <!-- LOGO -->


    <ul class="list-unstyled topnav-menu topnav-menu-left m-0">
        <li>
            <button class="button-menu-mobile waves-effect">
                <i class="mdi mdi-menu"></i>
            </button>
        </li>

        <li class="d-none d-lg-block">
            <form class="app-search">
                <div class="app-search-box">
                    <div class="input-group">
                        <input type="text" class="form-control" placeholder="Search...">
                        <div class="input-group-append">
                            <button class="btn" type="submit">
                                <i class="fas fa-search"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </li>
    </ul>
</div>
<%@include file="scrip.jsp" %>
