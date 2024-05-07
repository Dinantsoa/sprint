package mg.itu.prom16;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
// FontController

public class FontController extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        try {
            procesRequest(req, res);
        } catch (Exception e) {
            // TODO: handle exception
            out.println(e.getMessage());
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        try {
            procesRequest(req, res);
        } catch (Exception e) {
            // TODO: handle exception
            out.println(e.getMessage());
        }
    }

    public void procesRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        res.setContentType("text/html;charset=UTF-8");
        try {
            out.println("<h3> the request" + req.getContextPath() + "</h3>");
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }
}