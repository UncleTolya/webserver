package servlets;

import accountservice.AccountService;
import dbservice.DBException;
import dbservice.dao.UsersDAO;
import datasets.UsersDataSet;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;


public class SignUpServlet extends HttpServlet {
    private AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("login", "Гость");
        variables.put("message", "Пожалуйста зарегестрируйтесь");

        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(PageGenerator.instance()
                .getPage("index.html", variables));

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        HashMap<String, Object> variables = new HashMap<>();
        variables.put("login", login);
        variables.put("message", "some error");


        UsersDataSet userDataSet = null;
        try {
            userDataSet = accountService.getUser(login, password);
        } catch (DBException e) {
            e.printStackTrace();
        }
        if (login.isEmpty() || password.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else if (userDataSet == null) {
            try {
                accountService.addNewUser(login, password);
            } catch (DBException e) {
                e.printStackTrace();
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            variables.put("message", "has been registered, please signin");

        } else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            variables.put("message", "had registered already, please signin");
        }

        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(PageGenerator.instance()
                .getPage("index.html", variables));
    }

}
